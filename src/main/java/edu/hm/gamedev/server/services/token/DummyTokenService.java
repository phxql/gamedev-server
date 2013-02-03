package edu.hm.gamedev.server.services.token;

/**
 * A dummy token service.
 *
 * Always returns a dummy token.
 */
public class DummyTokenService implements TokenService {

  @Override
  public String generate() {
    return "12345";
  }
}
