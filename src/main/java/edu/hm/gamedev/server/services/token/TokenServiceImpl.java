package edu.hm.gamedev.server.services.token;

import edu.hm.gamedev.server.settings.Settings;

import java.util.Random;

/**
 * Default implementation for a token service.
 */
public class TokenServiceImpl implements TokenService {

  /**
   * Random number generator.
   */
  private final Random random;

  public TokenServiceImpl(Random random) {
    this.random = random;
  }

  @Override
  public String generate() {
    StringBuilder sb = new StringBuilder(Settings.Token.TOKEN_LENGTH);

    for (int i = 0; i < Settings.Token.TOKEN_LENGTH; i++) {
      int index = random.nextInt(Settings.Token.ALLOWED_CHARS.length());
      sb.append(Settings.Token.ALLOWED_CHARS.charAt(index));
    }

    return sb.toString();
  }
}
