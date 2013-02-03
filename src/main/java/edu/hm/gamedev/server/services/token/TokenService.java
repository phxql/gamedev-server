package edu.hm.gamedev.server.services.token;

/**
 * Servce to generate tokens.
 */
public interface TokenService {

  /**
   * Generate a token.
   *
   * @return Token.
   */
  String generate();
}
