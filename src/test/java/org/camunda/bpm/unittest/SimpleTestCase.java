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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Daniel Meyer
 * @author Martin Schimak
 */
public class SimpleTestCase {

  @Rule
  public ProcessEngineRule rule = new ProcessEngineRule();

  @Test
  @org.camunda.bpm.engine.test.Deployment(resources = {"diagram_1.bpmn"})
  public void shouldExecuteProcess() {
    rule.getRuntimeService().startProcessInstanceByKey("testProc");

    final List<RecorderExecutionListener.RecordedEvent> recordedEvents = RecorderExecutionListener.getRecordedEvents();

    assertThat(recordedEvents.size()).isEqualTo(2);

    assertThat(recordedEvents.get(0).getEventName()).isEqualTo("start");
    assertThat(recordedEvents.get(0).getActivityId()).contains("Task").contains("multiInstanceBody");

    assertThat(recordedEvents.get(1).getEventName()).isEqualTo("end");
    assertThat(recordedEvents.get(1).getActivityId()).contains("Task").contains("multiInstanceBody");

  }
}
