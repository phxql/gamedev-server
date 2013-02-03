package edu.hm.gamedev.server.model;

import edu.hm.gamedev.server.network.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class which models a player.
 */
public class Player {
  private static final Logger LOGGER = LoggerFactory.getLogger(Player.class);

  /**
   * Id of the player.
   */
  private final long id;
  /**
   * Connection to communicate with the player.
   */
  private final Connection connection;
  /**
   * True if the player has been authenticated.
   */
  private boolean authenticated;
  /**
   * Game of the player. Can be null.
   */
  private Game game;
  /**
   * True if the player is ready.
   */
  private boolean ready;
  /**
   * Timestamp when the player has sent the last packet.
   */
  private long lastActive;
  /**
   * Nickname of the player.
   */
  private String nickname;
  /**
   * E-Mail of the player.
   */
  private String email;
  /**
   * True if the player has finished loading.
   */
  private boolean loadingComplete;
  /**
   * Executor service to send something to the client.
   */
  private final ExecutorService sendExecutor;

  public Player(long id, Connection connection) {
    this.id = id;
    this.connection = connection;
    this.sendExecutor = startupSendExecutor();
  }

  /**
   * Starts up the send executor.
   *
   * @return Send executor.
   */
  private ExecutorService startupSendExecutor() {
    LOGGER.debug("Send executor started for client {}", id);

    return Executors.newSingleThreadExecutor();
  }

  /**
   * Shuts down the send executor.
   */
  private void shutdownSendExecutor() {
    if (!this.sendExecutor.isShutdown()) {
      LOGGER.debug("Send executor shutted down for client {}", id);
      this.sendExecutor.shutdown();
    }
  }

  /**
   * Shuts down the client and releasing all resources.
   */
  public void shutdown() {
    shutdownSendExecutor();
    this.connection.close();
  }

  /**
   * Sends some text to the client.
   *
   * @param text Text to send.
   */
  public void send(String text) {
    send(text, false);
  }

  /**
   * Sends some text to the client.
   *
   * @param text Text to send.
   * @param blocking True if the method should block until the text is delivered.
   */
  public void send(final String text, boolean blocking) {
    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        connection.send(text);
      }
    };

    if (blocking) {
      runnable.run();
    } else {
      this.sendExecutor.execute(runnable);
    }
  }

  @Override
  protected void finalize() throws Throwable {
    shutdown();
    super.finalize();
  }

  public Connection getConnection() {
    return connection;
  }

  public String getNickname() {
    return nickname;
  }

  public String getEmail() {
    return email;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public boolean isAuthenticated() {
    return authenticated;
  }

  public void setAuthenticated(boolean authenticated) {
    this.authenticated = authenticated;
  }

  public boolean isInLobby() {
    return game == null;
  }

  public boolean hasGame() {
    return game != null;
  }

  public Game getGame() {
    return game;
  }

  public void setGame(Game game) {
    this.game = game;
  }

  public void setReady(boolean ready) {
    this.ready = ready;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Player player = (Player) o;

    if (id != player.id) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return (int) (id ^ (id >>> 32));
  }

  @Override
  public String toString() {
    return "Player{" +
           "id=" + id +
           ", nickname='" + nickname + '\'' +
           '}';
  }

  public boolean isReady() {
    return ready;
  }

  public long getLastActive() {
    return lastActive;
  }

  public void setLastActive(long lastActive) {
    this.lastActive = lastActive;
  }

  public boolean isLoadingComplete() {
    return loadingComplete;
  }

  public void setLoadingComplete(boolean loadingComplete) {
    this.loadingComplete = loadingComplete;
  }
}
