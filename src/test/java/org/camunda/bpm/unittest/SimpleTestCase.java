/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.unittest;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.digest._apacheCommonsCodec.Base64;
import org.camunda.bpm.engine.impl.persistence.entity.UserEntity;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @author Christopher Zell
 */
public class SimpleTestCase {

  @Rule
  public ProcessEngineRule rule = new ProcessEngineRule();

  @Test
  public void shouldExecuteProcess() {
    // set up
    IdentityService identityService = rule.getIdentityService();
    User user1 = identityService.newUser("user1");
    user1.setFirstName("hans");
    user1.setLastName("Klock");
    user1.setPassword("pw");

    // when user is saved
    identityService.saveUser(user1);
    identityService.setAuthenticatedUserId("user1");

    // then argon2 can verify password
    UserEntity user = (UserEntity) identityService.createUserQuery().singleResult();
    Argon2 argon2 = Argon2Factory.create();
    String pwWithoutPrefix = user.getPassword().substring(8);
    assertTrue(argon2.verify(new String(Base64.decodeBase64(pwWithoutPrefix)), "pw"+user.getSalt()));

    // password manager can check the pw
    assertTrue(rule.getProcessEngineConfiguration().getPasswordManager().check("pw"+user.getSalt(), user.getPassword()));

    // and identity service can check password as well
    assertTrue(identityService.checkPassword("user1", "pw"));
  }
}
