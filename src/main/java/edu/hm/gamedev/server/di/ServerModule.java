package edu.hm.gamedev.server.di;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import java.security.SecureRandom;

import edu.hm.gamedev.server.model.Games;
import edu.hm.gamedev.server.model.Players;
import edu.hm.gamedev.server.packets.serialization.PacketDeserializer;
import edu.hm.gamedev.server.packets.serialization.PacketSerializer;
import edu.hm.gamedev.server.services.communication.CommunicationService;
import edu.hm.gamedev.server.services.communication.CommunicationServiceImpl;
import edu.hm.gamedev.server.services.messaging.EmailMessagingService;
import edu.hm.gamedev.server.services.messaging.MessagingService;
import edu.hm.gamedev.server.services.passwordHash.BcryptPasswordHashService;
import edu.hm.gamedev.server.services.passwordHash.PasswordHashService;
import edu.hm.gamedev.server.services.token.TokenService;
import edu.hm.gamedev.server.services.token.TokenServiceImpl;
import edu.hm.gamedev.server.storage.ClientLogStorage;
import edu.hm.gamedev.server.storage.MapStorage;
import edu.hm.gamedev.server.storage.PlayerStorage;
import edu.hm.gamedev.server.storage.hibernate.HibernateClientLogStorage;
import edu.hm.gamedev.server.storage.hibernate.HibernateMapStorage;
import edu.hm.gamedev.server.storage.hibernate.HibernatePlayerStorage;

/**
 * Google Guice DI module.
 */
public class ServerModule extends AbstractModule {

  protected void configure() {
    bind(PacketDeserializer.class).in(Singleton.class);
    bind(PacketSerializer.class).in(Singleton.class);

    bind(Players.class).in(Singleton.class);
    bind(Games.class).in(Singleton.class);

    bind(CommunicationService.class).to(CommunicationServiceImpl.class).in(Singleton.class);
    bind(PasswordHashService.class).to(BcryptPasswordHashService.class).in(Singleton.class);
    bind(MessagingService.class).to(EmailMessagingService.class).in(Singleton.class);
    bind(TokenService.class).toInstance(new TokenServiceImpl(new SecureRandom()));

    bind(PlayerStorage.class).to(HibernatePlayerStorage.class).in(Singleton.class);
    bind(ClientLogStorage.class).to(HibernateClientLogStorage.class).in(Singleton.class);
    bind(MapStorage.class).to(HibernateMapStorage.class).in(Singleton.class);
  }
}
