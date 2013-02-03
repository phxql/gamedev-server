package edu.hm.gamedev.server.storage.hibernate;

import edu.hm.gamedev.server.storage.dto.PlayerDto;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import edu.hm.gamedev.server.storage.PlayerStorage;
import edu.hm.gamedev.server.storage.hibernate.entities.PlayerEntity;
import edu.hm.gamedev.server.storage.hibernate.entities.PlayerStorageEntity;
import edu.hm.gamedev.server.storage.hibernate.exception.PlayerNotFoundException;

public class HibernatePlayerStorage extends HibernateStorage implements PlayerStorage {

  /**
   * JSON (de-)serializer.
   */
  private final ObjectMapper mapper = new ObjectMapper();

  /**
   * Logger.
   */
  private static Logger logger = LoggerFactory.getLogger(HibernatePlayerStorage.class);

  @Override
  public void insert(PlayerDto dto) {
    final PlayerEntity entity = toEntity(dto);

    withSession(new WithSession() {
      @Override
      public void apply(Session session) {
        session.save(entity);
      }
    });
  }

  @Override
  public void verifyEmail(final PlayerDto dto) {
    withSession(new WithSession() {
      @Override
      public void apply(Session session) {
        PlayerEntity entity = findEntityByEmail(dto.getEmail());
        if (entity == null) {
          throw new PlayerNotFoundException();
        }

        logger.debug("Set {} to verified", dto.getEmail());
        entity.setEmailVerifyToken(null);
        entity.setEmailVerified(true);
        session.update(entity);
      }
    });
  }

  private PlayerEntity findEntityByNickname(final String nickname) {
    return withSession(new WithSessionR<PlayerEntity>() {
      @Override
      public PlayerEntity apply(Session session) {
        return (PlayerEntity) session.createCriteria(PlayerEntity.class)
            .add(Restrictions.eq("nickname", nickname).ignoreCase())
            .uniqueResult();
      }
    });
  }

  /**
   * Loads a player entity identified by a given email address.
   *
   * @param email Email address.
   * @return Player entity.
   */
  private PlayerEntity findEntityByEmail(final String email) {
    return withSession(new WithSessionR<PlayerEntity>() {
      @Override
      public PlayerEntity apply(Session session) {
        return findEntityByEmail(session, email);
      }
    });
  }

  /**
   * Loads a player entity identified by a given email address.
   *
   * @param email   Email address.
   * @param session Hibernate session.
   * @return Player entity.
   */
  private PlayerEntity findEntityByEmail(Session session, final String email) {
    return (PlayerEntity) session.createCriteria(PlayerEntity.class)
        .add(Restrictions.eq("email", email).ignoreCase())
        .uniqueResult();
  }

  @Override
  public PlayerDto findByNickname(final String nickname) {
    PlayerEntity entity = findEntityByNickname(nickname);

    return entity == null ? null : toDto(entity);
  }

  @Override
  public PlayerDto findByEmail(final String email) {
    PlayerEntity entity = findEntityByEmail(email);

    return entity == null ? null : toDto(entity);
  }

  @Override
  public void updatePasswordResetToken(PlayerDto player, String token) {
    PlayerEntity entity = findEntityByEmail(player.getEmail());
    if (entity == null) {
      throw new PlayerNotFoundException();
    }

    entity.setResetPasswordToken(token);
    updateEntity(entity);
  }

  /**
   * Returns all storage entities for a given player.
   *
   * @param player  Player.
   * @param session Hibernate session.
   * @return Storage entities for a given player.
   */
  @SuppressWarnings("unchecked")
  private Iterable<PlayerStorageEntity> findStorageEntitiesByPlayer(Session session,
                                                                    final PlayerEntity player) {
    return session.createCriteria(PlayerStorageEntity.class)
        .add(Restrictions.eq("player", player))
        .list();
  }

  private PlayerStorageEntity findStorageEntityByPlayerAndGame(final PlayerEntity player,
                                                               final String game) {
    return withSession(new WithSessionR<PlayerStorageEntity>() {
      @Override
      public PlayerStorageEntity apply(Session session) {
        return (PlayerStorageEntity) session.createCriteria(PlayerStorageEntity.class)
            .add(Restrictions.eq("game", game))
            .add(Restrictions.eq("player", player))
            .uniqueResult();
      }
    });
  }

  @Override
  public JsonNode loadStorage(final String email, final String game) {
    logger.debug("Loading game storage for player with email {} and game {}", email, game);

    PlayerEntity playerEntity = findEntityByEmail(email);
    if (playerEntity == null) {
      throw new PlayerNotFoundException();
    }

    PlayerStorageEntity entity = findStorageEntityByPlayerAndGame(playerEntity, game);

    try {
      return entity == null ? null : mapper.readTree(entity.getContent());
    } catch (IOException e) {
      logger.warn("Error while loading player storage", e);
      return null;
    }
  }

  @Override
  public void saveStorage(final String email, final String game, final JsonNode content) {
    logger
        .debug("Saving game storage for player with email {} and game {}, content: {}", email, game,
               content);

    PlayerEntity playerEntity = findEntityByEmail(email);
    if (playerEntity == null) {
      throw new PlayerNotFoundException();
    }

    try {
      String contentAsString = mapper.writeValueAsString(content);

      PlayerStorageEntity entity = findStorageEntityByPlayerAndGame(playerEntity, game);
      if (entity == null) {
        PlayerStorageEntity toInsert = new PlayerStorageEntity();
        toInsert.setPlayer(playerEntity);
        toInsert.setContent(contentAsString);
        toInsert.setGame(game);

        insertStorage(toInsert);
      } else {
        entity.setContent(contentAsString);
        updateStorage(entity);
      }
    } catch (IOException e) {
      logger.warn("Error while saving player storage", e);
    }
  }

  @Override
  public void setPassword(PlayerDto player, String password) {
    PlayerEntity playerEntity = findEntityByEmail(player.getEmail());
    if (playerEntity == null) {
      throw new PlayerNotFoundException();
    }

    playerEntity.setPassword(password);
    updateEntity(playerEntity);
  }

  @Override
  public void remove(final PlayerDto player) {
    withSession(new WithSession() {
      @Override
      public void apply(Session session) {
        PlayerEntity playerEntity = findEntityByEmail(session, player.getEmail());
        if (playerEntity != null) {
          logger.debug("Deleting player with email {}", player.getEmail());

          Iterable<PlayerStorageEntity>
              associatedPlayerStorageEntities =
              findStorageEntitiesByPlayer(session, playerEntity);
          for (PlayerStorageEntity associatedPlayerStorageEntity : associatedPlayerStorageEntities) {
            session.delete(associatedPlayerStorageEntity);
          }

          session.delete(playerEntity);
        }
      }
    });
  }

  private void updateStorage(final PlayerStorageEntity entity) {
    withSession(new WithSession() {
      @Override
      public void apply(Session session) {
        session.update(entity);
      }
    });
  }

  private void insertStorage(final PlayerStorageEntity entity) {
    withSession(new WithSession() {
      @Override
      public void apply(Session session) {
        session.save(entity);
      }
    });
  }

  /**
   * Updates entity.
   *
   * @param entity Entity.
   */
  private void updateEntity(final PlayerEntity entity) {
    withSession(new WithSession() {
      @Override
      public void apply(Session session) {
        session.update(entity);
      }
    });
  }

  /**
   * Converts an entity to a dto.
   *
   * @param entity Entity to convert.
   * @return DTO.
   */
  private PlayerDto toDto(PlayerEntity entity) {
    return new PlayerDto(entity.getNickname(), entity.getEmail(), entity.getPassword(),
                         entity.isEmailVerified(), entity.getEmailVerifyToken(),
                         entity.getResetPasswordToken());
  }

  /**
   * Converts a dto to an entity.
   *
   * @param dto DTO to convert,
   * @return Entity.
   */
  private PlayerEntity toEntity(PlayerDto dto) {
    PlayerEntity entity = new PlayerEntity();

    entity.setEmail(dto.getEmail());
    entity.setNickname(dto.getNickname());
    entity.setPassword(dto.getPassword());
    entity.setEmailVerified(dto.isEmailVerified());
    entity.setEmailVerifyToken(dto.getEmailVerifyToken());
    entity.setResetPasswordToken(dto.getResetPasswordToken());

    return entity;
  }
}
