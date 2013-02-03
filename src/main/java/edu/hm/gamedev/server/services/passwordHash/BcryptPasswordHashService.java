package edu.hm.gamedev.server.services.passwordHash;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Hash service which hashes passwords with BCrypt.
 */
public class BcryptPasswordHashService implements PasswordHashService {

  @Override
  public String hash(String password) {
    return BCrypt.hashpw(password, BCrypt.gensalt());
  }

  @Override
  public boolean verify(String password, String hash) {
    return BCrypt.checkpw(password, hash);
  }
}
