package edu.hm.gamedev.server.services.communication;

import com.google.inject.Inject;
import edu.hm.gamedev.server.helper.StackTraceHelper;
import edu.hm.gamedev.server.model.Player;
import edu.hm.gamedev.server.model.Players;
import edu.hm.gamedev.server.network.Connection;
import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.Ping;
import edu.hm.gamedev.server.packets.exceptions.PacketDeserializationException;
import edu.hm.gamedev.server.packets.exceptions.PacketSerializationException;
import edu.hm.gamedev.server.packets.serialization.PacketDeserializer;
import edu.hm.gamedev.server.packets.serialization.PacketSerializer;
import edu.hm.gamedev.server.packets.server2client.InternalServerError;
import edu.hm.gamedev.server.packets.server2client.InvalidPacket;
import edu.hm.gamedev.server.settings.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class CommunicationServiceImpl implements CommunicationService {

  private static Logger logger = LoggerFactory.getLogger(CommunicationServiceImpl.class);

  private final Players players;
  private final List<PacketReceivedEventHandler>
      packetReceivedEventHandler = Collections.synchronizedList(new ArrayList<PacketReceivedEventHandler>());
  private final List<PlayerConnectedEventHandler>
      playerConnectedEventHandler = Collections.synchronizedList(new ArrayList<PlayerConnectedEventHandler>());
  private final List<PlayerDisconnectedEventHandler>
      playerDisconnectedEventHandler = Collections.synchronizedList(new ArrayList<PlayerDisconnectedEventHandler>());

  /**
   * Executor service to find dead clients.
   */
  private ScheduledExecutorService deadClientExecutor;

  private final PacketDeserializer packetDeserializer;
  private final PacketSerializer packetSerializer;
  private final AtomicLong nextClientId = new AtomicLong(0);

  @Inject
  public CommunicationServiceImpl(Players players, PacketDeserializer packetDeserializer,
                                  PacketSerializer packetSerializer) {
    this.players = players;
    this.packetDeserializer = packetDeserializer;
    this.packetSerializer = packetSerializer;
  }

  @Override
  public void init() {
    if (Settings.DeadClientDetection.ENABLED) {
      startupDeadClientExecutor();
    }

    logger.debug("Initialized");
  }

  @Override
  public void shutdown() {
    if (Settings.DeadClientDetection.ENABLED) {
      shutdownDeadClientExecutor();
    }

    logger.debug("Shutted down");
  }

  /**
   * Shuts down the dead client executor.
   */
  private void shutdownDeadClientExecutor() {
    logger.debug("Shutting down dead client executor...");

    this.deadClientExecutor.shutdown();
    try {
      if (!this.deadClientExecutor.awaitTermination(1, TimeUnit.MINUTES)) {
        this.deadClientExecutor.shutdownNow();
      }
    } catch (InterruptedException e) {
      this.deadClientExecutor.shutdownNow();
      Thread.currentThread().interrupt();
    }

    logger.debug("Dead client executor shutted down");
  }

  /**
   * Starts the dead client executor.
   */
  private void startupDeadClientExecutor() {
    logger.debug("Starting dead client executor...");

    this.deadClientExecutor = Executors.newSingleThreadScheduledExecutor();
    this.deadClientExecutor.scheduleWithFixedDelay(new Runnable() {
      @Override
      public void run() {
        onDeadClientExecutorTick();
      }
    }, Settings.DeadClientDetection.CHECK_INTERVAL_MS, Settings.DeadClientDetection.CHECK_INTERVAL_MS, TimeUnit.MILLISECONDS);

    logger.debug("Dead client executor started");
  }

  private void onDeadClientExecutorTick() {
    Collection<Player> deadClients = new ArrayList<Player>();

    for (Player player : this.players) {
      long delta = System.currentTimeMillis() - player.getLastActive();

      if (delta > Settings.DeadClientDetection.REMOVE_THRESHOLD_MS) {
        logger.debug("Player {} is dead", player);
        deadClients.add(player);
      } else if (delta > Settings.DeadClientDetection.PING_THRESHOLD_MS) {
        logger.debug("Pinging player {}", player);
        unicast(player, new Ping());
      }
    }

    for (Player deadClient : deadClients) {
      deadClient.getConnection().close();
      onConnectionClosed(deadClient.getConnection());
    }

    int deadClientCount = deadClients.size();
    if (deadClientCount > 0) {
      logger.debug("Removed {} dead players", deadClientCount);
    }
  }


  @Override
  public void unicast(Player player, Packet packet) {
    unicast(player, packet, Settings.ClientThreading.CLIENT_SENDING_BLOCKS_DEFAULT);
  }

  @Override
  public void unicast(final Player player, Packet packet, boolean blocking) {
    logger.trace("Sending packet {} to player {}", packet, player);
    try {
      String serializedPacket = packetSerializer.build(packet);

      player.send(serializedPacket, blocking);
    } catch (PacketSerializationException e) {
      logger.warn("Exception in method unicast", e);
    }
  }

  @Override
  public void multicast(Iterable<Player> players, Packet packet) {
    multicast(players, packet, Settings.ClientThreading.CLIENT_SENDING_BLOCKS_DEFAULT);
  }

  @Override
  public void multicast(Iterable<Player> players, Packet packet, boolean blocking) {
    logger.trace("Sending packet {} to players {}", packet, players);

    for (Player player : players) {
      unicast(player, packet, blocking);
    }
  }

  private void updateLastActive(Player player) {
    player.setLastActive(System.currentTimeMillis());
  }

  @Override
  public void onConnectionOpened(Connection connection) {
    Player player = new Player(nextClientId.incrementAndGet(), connection);

    logger.debug("Player {} connected", player);

    updateLastActive(player);
    firePlayerConnectedEvent(player);
  }

  private void firePlayerConnectedEvent(Player player) {
    for (PlayerConnectedEventHandler handler : playerConnectedEventHandler) {
      handler.onConnected(player);
    }
  }

  @Override
  public void onConnectionClosed(Connection connection) {
    Player player = players.findPlayerByConnection(connection);

    logger.debug("Player {} disconnected", player);

    if (player != null) {
      firePlayerDisconnectedEvent(player);
      player.shutdown();
    }
  }

  private void firePlayerDisconnectedEvent(Player player) {
    for (PlayerDisconnectedEventHandler handler : playerDisconnectedEventHandler) {
      handler.onDisconnected(player);
    }
  }

  @Override
  public void onMessageReceived(Connection connection, String message) {
    Player player = players.findPlayerByConnection(connection);

    if (player == null) {
      logger.warn("Message from connection which doesn't belong to any player");
    } else {
      try {
        updateLastActive(player);

        try {
          Packet packet = packetDeserializer.build(message);

          logger.trace("Reveiced packet {} from player {}", packet, player);

          boolean handled = firePacketReceivedEvent(player, packet);
          if (!handled) {
            logger.info("Unhandled packet {} from player {}", packet, player);
          }
        } catch (PacketDeserializationException e) {
          logger.warn("Can not deserialize packet from string " + message, e);
          unicast(player, new InvalidPacket(message));
        }
      } catch (RuntimeException e) {
        logger.error("Unhandled exception", e);

        String stackTrace;
        if (Settings.ErrorReporting.STACK_TRACE_IN_PACKET) {
          stackTrace = StackTraceHelper.getStackTrace(e);
        } else {
          stackTrace = "";
        }

        unicast(player, new InternalServerError(stackTrace), true);
        firePlayerDisconnectedEvent(player);
        connection.close();
        throw e;
      }
    }
  }

  private boolean firePacketReceivedEvent(Player player, Packet packet) {
    boolean handled = false;

    for (PacketReceivedEventHandler handler : packetReceivedEventHandler) {
      if (handler.onPacketReceived(player, packet, handled)) {
        handled = true;
      }
    }

    return handled;
  }

  @Override
  public void registerForPacketReceivedEvents(PacketReceivedEventHandler handler) {
    packetReceivedEventHandler.add(handler);
  }

  @Override
  public void registerForPlayerConnectedEvents(PlayerConnectedEventHandler handler) {
    playerConnectedEventHandler.add(handler);
  }

  @Override
  public void registerForPlayerDisconnectedEvents(PlayerDisconnectedEventHandler handler) {
    playerDisconnectedEventHandler.add(handler);
  }

  @Override
  public void unregisterForPacketReceivedEvents(PacketReceivedEventHandler handler) {
    packetReceivedEventHandler.remove(handler);
  }

  @Override
  public void unregisterForPlayerConnectedEvents(PlayerConnectedEventHandler handler) {
    playerConnectedEventHandler.remove(handler);
  }

  @Override
  public void unregisterForPlayerDisconnectedEvents(PlayerDisconnectedEventHandler handler) {
    playerDisconnectedEventHandler.remove(handler);
  }

}
