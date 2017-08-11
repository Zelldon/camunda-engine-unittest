package org.camunda.bpm.unittest;

import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.impl.bpmn.parser.AbstractBpmnParseListener;
import org.camunda.bpm.engine.impl.pvm.process.ActivityImpl;
import org.camunda.bpm.engine.impl.util.xml.Element;

/**
 *
 */
public class ParseListener extends AbstractBpmnParseListener
{

    @Override
    public void parseMultiInstanceLoopCharacteristics(Element activityElement, Element multiInstanceLoopCharacteristicsElement, ActivityImpl activity)
    {
       // activity is the multi instance activity body
        activity.addListener(ExecutionListener.EVENTNAME_END, new RecorderExecutionListener());
        activity.addListener(ExecutionListener.EVENTNAME_START, new RecorderExecutionListener());
    }
}
