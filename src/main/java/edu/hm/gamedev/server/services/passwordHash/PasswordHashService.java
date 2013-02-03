package edu.hm.gamedev.server.services.passwordHash;

/**
 * Service which hashes and verifies passwords.
 */
public interface PasswordHashService {

  /**
   * Hashes a password.
   *
   * @param password Password to hash.
   * @return Hash of the password.
   */
  String hash(String password);

  /**
   * Verifies a hash.
   *
   * @param password Plaintext password to verify.
   * @param hash     Hash to verify.
   * @return True if the hash belongs to the password.
   */
  boolean verify(String password, String hash);
}
