package org.camunda.bpm.unittest;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.camunda.bpm.engine.impl.digest.Base64EncodedHashDigest;
import org.camunda.bpm.engine.impl.digest.PasswordEncryptor;
import org.camunda.bpm.engine.impl.digest._apacheCommonsCodec.Base64;

/**
 * @author Christopher Zell <christopher.zell@camunda.com>
 */
public class Argon2HashAlgorithm extends Base64EncodedHashDigest implements PasswordEncryptor {

  protected String getAlgorithmName() {
    return "argon2";
  }

  @Override
  public boolean check(String password, String encrypted) {

    // Create instance
    Argon2 argon2 = Argon2Factory.create();

    // Verify password
    return argon2.verify(new String(Base64.decodeBase64(encrypted)), password);
  }

  @Override
  protected byte[] createByteHash(String password) {

    // Create instance
    Argon2 argon2 = Argon2Factory.create();

    // Hash password
    // 2 iterations, 65536 Memory, 1 parallelism
    String hash = argon2.hash(2, 65536, 1, password);
    return hash.getBytes();
  }
}
