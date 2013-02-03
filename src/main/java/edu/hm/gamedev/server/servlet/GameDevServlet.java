package edu.hm.gamedev.server.servlet;

import com.google.inject.Guice;
import com.google.inject.Injector;

import edu.hm.gamedev.server.Version;
import edu.hm.gamedev.server.storage.hibernate.HibernateUtil;
import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.catalina.websocket.WsOutbound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import edu.hm.gamedev.server.api.Controller;
import edu.hm.gamedev.server.controller.*;
import edu.hm.gamedev.server.controller.game.SpecificGameController;
import edu.hm.gamedev.server.controller.game.toyracer.CollisionController;
import edu.hm.gamedev.server.di.ServerModule;
import edu.hm.gamedev.server.network.Connection;
import edu.hm.gamedev.server.network.MessageInboundConnection;
import edu.hm.gamedev.server.services.communication.CommunicationService;

/**
 * The GameDev Servlet is the main entry point for the game server. <p/> It handles the creation of
 * all needed dependencies and wraps the handling of websockets.
 */
public class GameDevServlet extends WebSocketServlet {
  private static Logger logger = LoggerFactory.getLogger(GameDevServlet.class);

  /**
   * DI container.
   */
  private final Injector injector;
  /**
   * Communication controller.
   */
  private final CommunicationService communicationService;

  /*
   * Game controller.
   */
  private final List<Controller> controller = new ArrayList<Controller>();

  public GameDevServlet() {
    try {
      injector = Guice.createInjector(new ServerModule());
      this.communicationService = injector.getInstance(CommunicationService.class);

      createController();
    } catch (RuntimeException e) {
      logger.error("Exception on startup", e);
      throw e;
    }
  }

  /**
   * Registers the controller classes.
   */
  private void createController() {
    create(ConnectionController.class);
    create(AuthenticationController.class);
    create(PingPongController.class);
    create(LobbyController.class);
    create(LogController.class);
    create(PlayerStorageController.class);
    create(MapStorageController.class);
    create(GameManagementController.class);
    create(MessageController.class);
    create(HelloController.class);
    create(ChatController.class);
    create(LoadingController.class);

    createForGame("TOY_RACER", CollisionController.class);
  }

  /**
   * Creates a given controller class.
   *
   * @param clazz Controller class to create.
   */
  private void create(Class<? extends Controller> clazz) {
    Controller instance = injector.getInstance(clazz);
    controller.add(instance);
  }

  /**
   * Creates a given controller class for a specific game.
   *
   * @param gameType Type of the game.
   * @param clazz Controller class to create.
   */
  private void createForGame(String gameType, Class<? extends Controller> clazz) {
    Controller instance = injector.getInstance(clazz);
    SpecificGameController wrapper = new SpecificGameController(gameType, instance);
    controller.add(wrapper);
  }

  @Override
  public void init() throws ServletException {
    super.init();

    HibernateUtil.init();

    try {
      for(Controller instance: controller) {
        communicationService.registerForPacketReceivedEvents(instance);
        communicationService.registerForPlayerConnectedEvents(instance);
        communicationService.registerForPlayerDisconnectedEvents(instance);

        instance.init();
      }
    } catch (RuntimeException e) {
      logger.error("Exception on initialization", e);
      throw e;
    }

    communicationService.init();
    logger.info("Servlet initialized");
    logger.info("Build Date: {}", Version.getBuildDate());
    logger.info("Build Number: {}", Version.getBuildNumber());
    logger.info("Build from Revision: {}", Version.getBuildRevision());
  }

  @Override
  public void destroy() {
    for(Controller instance: controller) {
      communicationService.unregisterForPacketReceivedEvents(instance);
      communicationService.unregisterForPlayerConnectedEvents(instance);
      communicationService.unregisterForPlayerDisconnectedEvents(instance);

      instance.shutdown();
    }

    communicationService.shutdown();
    HibernateUtil.shutdown();

    logger.info("Servlet destroyed");
    super.destroy();
  }

  @Override
  protected StreamInbound createWebSocketInbound(String subProtocol, HttpServletRequest request) {
    return new MessageInbound() {
      private final Connection connection = new MessageInboundConnection(this);

      @Override
      protected void onOpen(WsOutbound outbound) {
        communicationService.onConnectionOpened(this.connection);
      }

      @Override
      protected void onClose(int status) {
        communicationService.onConnectionClosed(this.connection);
      }

      @Override
      protected void onBinaryMessage(ByteBuffer message) throws IOException {
        throw new UnsupportedOperationException("Binary message not supported.");
      }

      @Override
      protected void onTextMessage(CharBuffer message) throws IOException {
        communicationService.onMessageReceived(this.connection, message.toString());
      }
    };
  }
}
