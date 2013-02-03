package edu.hm.gamedev.server.services.passwordHash;

/**
 * A dummy hash service.
 *
 * Returns the plaintext password as hash.
 */
public class DummyPasswordHashService implements PasswordHashService {

  @Override
  public String hash(String password) {
    return password;
  }

  @Override
  public boolean verify(String password, String hash) {
    if (password == null) {
      return hash == null;
    }

    return password.equals(hash);
  }
}
