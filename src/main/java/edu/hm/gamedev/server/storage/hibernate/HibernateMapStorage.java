package edu.hm.gamedev.server.storage.hibernate;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import edu.hm.gamedev.server.storage.MapStorage;
import edu.hm.gamedev.server.storage.hibernate.entities.MapStorageEntity;

public class HibernateMapStorage extends HibernateStorage implements MapStorage {

  /**
   * JSON (de-)serializer.
   */
  private final ObjectMapper mapper = new ObjectMapper();

  /**
   * Logger.
   */
  private static Logger logger = LoggerFactory.getLogger(HibernateMapStorage.class);

  private MapStorageEntity findEntityByGameAndMap(final String game, final String map) {
    return withSession(new WithSessionR<MapStorageEntity>() {
      @Override
      public MapStorageEntity apply(Session session) {
        return (MapStorageEntity) session.createCriteria(MapStorageEntity.class)
            .add(Restrictions.eq("game", game))
            .add(Restrictions.eq("map", map))
            .uniqueResult();
      }
    });
  }

  @Override
  public JsonNode load(String game, String map) {
    logger.debug("Loading map storage for game {} and map {}", game, map);

    MapStorageEntity entity = findEntityByGameAndMap(game, map);

    try {
      return entity == null ? null : mapper.readTree(entity.getContent());
    } catch (IOException e) {
      logger.warn("Error while loading map storage", e);
      return null;
    }
  }

  @Override
  public void save(String game, String map, JsonNode content) {
    logger.debug("Saving map storage for game {} and map {}, content: {}", game, map, content);

    try {
      String contentAsString = mapper.writeValueAsString(content);

      MapStorageEntity entity = findEntityByGameAndMap(game, map);
      if (entity == null) {
        MapStorageEntity toInsert = new MapStorageEntity();
        toInsert.setGame(game);
        toInsert.setMap(map);
        toInsert.setContent(contentAsString);

        insert(toInsert);
      } else {
        entity.setContent(contentAsString);
        update(entity);
      }
    } catch (IOException e) {
      logger.warn("Error while saving map storage", e);
    }
  }

  private void update(final MapStorageEntity entity) {
    withSession(new WithSession() {
      @Override
      public void apply(Session session) {
        session.update(entity);
      }
    });
  }

  private void insert(final MapStorageEntity entity) {
    withSession(new WithSession() {
      @Override
      public void apply(Session session) {
        session.save(entity);
      }
    });
  }
}
