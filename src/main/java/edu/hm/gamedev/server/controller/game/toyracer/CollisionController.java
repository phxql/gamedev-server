package edu.hm.gamedev.server.controller.game.toyracer;

import org.apache.commons.lang.ObjectUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;
import org.dyn4j.collision.narrowphase.Gjk;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Transform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import edu.hm.gamedev.server.controller.AbstractController;
import edu.hm.gamedev.server.model.Game;
import edu.hm.gamedev.server.model.Player;
import edu.hm.gamedev.server.model.Players;
import edu.hm.gamedev.server.packets.Packet;
import edu.hm.gamedev.server.packets.client2server.BufferedMessage;
import edu.hm.gamedev.server.packets.client2server.ClientMessage;


public class CollisionController extends AbstractController {

  public static final int FORCE_POLICE_CAR = 90;
  public static final int FORCE_FORMULA_ONE_CAR = 40;
  public static final int FORCE_BUS = 110;
  public static final int FORCE_DIGGER = 120;
  public static final int FORCE_LOCOMOTIVE = 150;
  public static final int FORCE_TRACTOR = 130;
  public static final int POLICE_CAR_ID = 0;
  public static final int FORMULA_ONE_CAR_ID = 1;
  public static final int BUS_ID = 2;
  public static final int DIGGER_ID = 3;
  public static final int LOCOMOTIVE_ID = 4;
  public static final int TRACTOR_ID = 5;
  /**
   * Logger.
   */
  private static Logger logger = LoggerFactory.getLogger(CollisionController.class);

  /**
   * Collision detector
   */
  private final Gjk collisionDetector = new Gjk();

  /**
   * Time in milli seconds that hast to be passed until a new collision can be detected
   */
  private static final int COLLISION_TIME_LIMIT = 5000;

  /**
   * Map to store the collision, so we can check that a Player cna collide only once in a while
   */
  private final Map<String, Collision> collisions = new HashMap<String, Collision>();

  /**
   * Storing the force for all participating players
   */
  private final Map<String, Integer> forces = new HashMap<String, Integer>();

  /**
   * Width of a car.
   */
  private static final float CAR_WIDTH = 6.0f;
  /**
   * Height of a car.
   */
  private static final float CAR_HEIGHT = 3.0f;

  private static final float COLLISION_POSITION_DELTA = 0.1f;

  /**
   * Key to access the location in the payload
   */
  private static final String PAYLOAD_LOCATION_KEY = "location";

  @Override
  public boolean onPacketReceived(Player player, Packet packet, boolean processed) {
    if (packet.getClass() == BufferedMessage.class) {
      handleBufferedMessagePacket(player);
      return true;
    } else if (packet.getClass() == ClientMessage.class) {
      handleClientMessagePacket(player, (ClientMessage) packet);
    }

    return false;
  }

  protected void handleClientMessagePacket(Player player, ClientMessage message) {
    if (player == null || message == null) {
      return;
    }
    JsonNode payload = message.getPayload();
    if (payload == null) {
      return;
    }
    if (message.getContentType().equals("PLAYER_INFO")) {
      String nickName = payload.get("nickname").asText();
      int carID = payload.get("carId").getIntValue();
      int forceCar = 0;

      //thi is copied from the carInfo.js in the client. So if you change something here, you have
      //to change it in the carInfo.js too.
      switch (carID) {
        case POLICE_CAR_ID:
          // Police Car
          forceCar = FORCE_POLICE_CAR;
          break;
        case FORMULA_ONE_CAR_ID:
          // Formula One Car
          forceCar = FORCE_FORMULA_ONE_CAR;
          break;
        case BUS_ID:
          // Bus
          forceCar = FORCE_BUS;
          break;
        case DIGGER_ID:
          // Digger
          forceCar = FORCE_DIGGER;
          break;
        case LOCOMOTIVE_ID:
          // Locomotive
          forceCar = FORCE_LOCOMOTIVE;
          break;
        case TRACTOR_ID:
          // Tractor
          forceCar = FORCE_TRACTOR;
          break;
        default:
      }
      int force = payload.get("force") != null ? payload.get("force").getIntValue() : 0;
      int totalForce = forceCar + force;

      forces.put(nickName, totalForce);
    }
  }

  protected void handleBufferedMessagePacket(Player player) {
    Game game = player.getGame();
    Players takers = game.getTakers().except(player);

    BufferedMessage playerMessage = game.getMessageBuffer().getMessageForPlayer(player);
    if (playerMessage != null) {
      Rectangle playerRectangle = this.rectangleForBufferedMessage();
      if (!this.validCollisionPosition(playerMessage)) {
        return;
      }
      Transform playerTransform = this.transformForBufferedMessage(playerMessage);

      for (Player taker : takers) {
        BufferedMessage takerMessage = game.getMessageBuffer().getMessageForPlayer(taker);

        if (takerMessage != null) {
          Rectangle takerRectangle = this.rectangleForBufferedMessage();
          Transform takerTransform = this.transformForBufferedMessage(takerMessage);

          boolean
              collision =
              collisionDetector
                  .detect(playerRectangle, playerTransform, takerRectangle, takerTransform);

          double
              zTaker =
              takerMessage.getPayload().get(PAYLOAD_LOCATION_KEY).get("z").asDouble();
          double
              zPlayer =
              playerMessage.getPayload().get(PAYLOAD_LOCATION_KEY).get("z").asDouble();

          if (Math.abs(zTaker - zPlayer) < 1.0 && collision) {
            logger.debug("Detected a collision between " + player.getNickname() + " and " + taker
                .getNickname());

            JsonNode takerPayload = takerMessage.getPayload();
            JsonNode playerPayload = playerMessage.getPayload();

            decideCollision(player, taker, takerPayload, playerPayload);
          }
        }
      }
    }
  }

  private void decideCollision(Player player, Player taker, JsonNode takerPayload,
                               JsonNode playerPayload) {
    double speedTaker = takerPayload.get("speed").asDouble();
    double speedPlayer = playerPayload.get("speed").asDouble();

    if (speedTaker > 0 && speedPlayer > 0) {

      //check for collisions in the past
      Date now = new Date();
      long collisionTakerTimeDiff = Long.MAX_VALUE;
      long collisionPlayerTimeDiff = Long.MAX_VALUE;
      Collision collisionTaker = collisions.get(taker.getNickname());
      if (collisionTaker != null) {
        long collisionTakerTime = collisionTaker.getTimestamp().getTime();
        collisionTakerTimeDiff = now.getTime() - collisionTakerTime;
      }
      Collision collisionPlayer = collisions.get(player.getNickname());
      if (collisionPlayer != null) {
        long collisionPlayerTime = collisionPlayer.getTimestamp().getTime();
        collisionPlayerTimeDiff = now.getTime() - collisionPlayerTime;
      }

      //get force for player and taker
      int forceTaker = forces.get(taker.getNickname());
      int forcePlayer = forces.get(player.getNickname());

      if (forceTaker > forcePlayer) {
        //player collide
        performCollision(player, playerPayload, collisionPlayerTimeDiff);
      } else if (forceTaker < forcePlayer) {
        //taker collide
        performCollision(taker, takerPayload, collisionTakerTimeDiff);
      } else {
        //taker and player have the same force, so decide on speed which will collide
        if (speedTaker > speedPlayer) {
          performCollision(player, playerPayload, collisionPlayerTimeDiff);
        } else {
          performCollision(taker, takerPayload, collisionTakerTimeDiff);
        }
      }


    }
  }

  private void performCollision(Player player, JsonNode payload,
                                long timeDiff) {
    if (timeDiff > COLLISION_TIME_LIMIT) {
      ((ObjectNode) payload).put("didCollide", true);
      collisions.put(player.getNickname(), new Collision(player));
    }
  }

  private Rectangle rectangleForBufferedMessage() {
    return new Rectangle(CAR_WIDTH, CAR_HEIGHT);
  }

  private boolean validCollisionPosition(BufferedMessage message) {
    double x = message.getPayload().get(PAYLOAD_LOCATION_KEY).get("x").asDouble();
    double y = message.getPayload().get(PAYLOAD_LOCATION_KEY).get("y").asDouble();

    return !(Math.abs(x) < COLLISION_POSITION_DELTA && Math.abs(y) < COLLISION_POSITION_DELTA);
  }

  private Transform transformForBufferedMessage(BufferedMessage message) {
    JsonNode location = message.getPayload().get(PAYLOAD_LOCATION_KEY);

    double x = location.get("x").asDouble();
    double y = location.get("y").asDouble();
    double yaw = message.getPayload().get("yaw").asDouble();

    Transform transform = new Transform();
    transform.rotate(yaw);
    transform.translate(x, y);

    return transform;
  }

  /**
   * Private Class to wrap the time a collision occurs for a player/taker
   */
  private static class Collision {

    private Player player;
    private Date timestamp;

    public Player getPlayer() {
      return player;
    }

    public void setPlayer(Player player) {
      this.player = player;
    }

    public Date getTimestamp() {
      return (Date) ObjectUtils.clone(timestamp);
    }

    public void setTimestamp(Date timestamp) {
      this.timestamp = (Date) ObjectUtils.clone(timestamp);
    }

    public Collision(Player player) {
      this.player = player;
      this.timestamp = new Date();
    }
  }
}
