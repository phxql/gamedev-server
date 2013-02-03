package edu.hm.gamedev.server.storage.hibernate;

import edu.hm.gamedev.server.storage.dto.LogEntryDto;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.hm.gamedev.server.storage.ClientLogStorage;
import edu.hm.gamedev.server.storage.hibernate.entities.LogEntryEntity;
import edu.hm.gamedev.server.storage.hibernate.entities.PlayerEntity;
import edu.hm.gamedev.server.storage.hibernate.exception.PlayerNotFoundException;

/**
 * Client log storage which stores the log entries with hibernate.
 */
public class HibernateClientLogStorage extends HibernateStorage implements ClientLogStorage {

  private static Logger logger = LoggerFactory.getLogger(HibernateStorage.class);

  @Override
  public void insert(LogEntryDto dto) {
    logger.debug("Inserting log entry");

    final LogEntryEntity entity = toEntity(dto);

    withSession(new WithSession() {
      @Override
      public void apply(Session session) {
        session.save(entity);
      }
    });
  }

  private PlayerEntity findPlayerEntity(final String email) {
    return withSession(new WithSessionR<PlayerEntity>() {
      @Override
      public PlayerEntity apply(Session session) {
        return (PlayerEntity) session.createCriteria(PlayerEntity.class)
            .add(Restrictions.eq("email", email).ignoreCase())
            .uniqueResult();
      }
    });
  }

  /**
   * Converts a log entry DTO to an log entry entity.
   *
   * @param dto DTO.
   * @return Entity.
   */
  private LogEntryEntity toEntity(LogEntryDto dto) {
    LogEntryEntity entity = new LogEntryEntity();

    entity.setLevel(dto.getLevel());
    entity.setMessage(dto.getMessage());
    entity.setTimestamp(dto.getTimestamp());

    PlayerEntity player = findPlayerEntity(dto.getEmail());
    if (player == null) {
      throw new PlayerNotFoundException();
    }
    entity.setPlayer(player);

    return entity;
  }
}
