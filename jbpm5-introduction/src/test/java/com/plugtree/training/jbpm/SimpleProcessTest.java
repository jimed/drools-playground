package com.plugtree.training.jbpm;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.impl.ClassPathResource;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.ProcessInstance;
import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemHandler;
import org.drools.runtime.process.WorkItemManager;
import org.drools.runtime.process.WorkflowProcessInstance;
import org.junit.Test;

public class SimpleProcessTest {

	@Test
	public void testSimpleProcess() throws Exception {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(new ClassPathResource("testProcess.bpmn2"), ResourceType.BPMN2);
		if (kbuilder.hasErrors()){
			for (KnowledgeBuilderError error : kbuilder.getErrors()) {
				System.out.println(error);
			}
			throw new IllegalStateException("testProcess.bpmn2 has errors");
		}
		KnowledgeBase kbase = kbuilder.newKnowledgeBase();
		StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
		TestAsyncWorkItemHandler handler1 = new TestAsyncWorkItemHandler();
		TestAsyncWorkItemHandler handler2 = new TestAsyncWorkItemHandler();
		ksession.getWorkItemManager().registerWorkItemHandler("task1", handler1);
		ksession.getWorkItemManager().registerWorkItemHandler("task2", handler2);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("param1", "myInput");
		params.put("param2", "myOtherInput");
		ProcessInstance instance = ksession.startProcess("com.plugtree.training.testProcess", params);

		//the state of the instance is active, because it is waiting for an activity
		//to finish its execution 
		Assert.assertEquals(ProcessInstance.STATE_ACTIVE, instance.getState());
		//first activity is where it is waiting
		WorkItem item1 = handler1.getItem();
		Assert.assertNotNull(item1);
		//first activity has an input mapped to param1, so they should be the same
		Assert.assertNotNull(item1.getParameter("input1"));
		Assert.assertEquals(params.get("param1"), item1.getParameter("input1"));

		//create a map of results for the first activity
		Map<String, Object> results1 = new HashMap<String, Object>();
		results1.put("output1", "myOutput");
		//continue the execution
		ksession.getWorkItemManager().completeWorkItem(item1.getId(), results1);
		
		//the state of the instance is active, because it is waiting for an activity
		//to finish its execution 
		Assert.assertEquals(ProcessInstance.STATE_ACTIVE, instance.getState());
		//second activity is where it is waiting
		WorkItem item2 = handler2.getItem();
		Assert.assertNotNull(item2);
		//second activity has an input mapped to param2, so they should be the same
		Assert.assertNotNull(item2.getParameter("input2"));
		Assert.assertEquals(params.get("param2"), item2.getParameter("input2"));
		
		//also, the output from the first activity went to a process variable, so let's see it!
		WorkflowProcessInstance wfInstance = (WorkflowProcessInstance) instance;
		Assert.assertNotNull(wfInstance.getVariable("newParam"));
		//and it should be the same as the output of the first activity
		Assert.assertEquals(wfInstance.getVariable("newParam"), results1.get("output1"));
		
		//the second activity has no outputs, so we can continue without them
		ksession.getWorkItemManager().completeWorkItem(item2.getId(), null);
		
		//since handler2 was the last async activity, the process continued until completion:
		Assert.assertEquals(ProcessInstance.STATE_COMPLETED, instance.getState());
	}
	
	private static class TestAsyncWorkItemHandler implements WorkItemHandler {
		private WorkItem item;
		
		@Override
		public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
			this.item = null;
		}
		
		@Override
		public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
			this.item = workItem;
			System.out.println("execution of work item. Parameters:");
			for (Map.Entry<String, Object> entry : workItem.getParameters().entrySet()) {
				System.out.println("--> " + entry.getKey() + ": " + String.valueOf(entry.getValue()));
			}
		}
		
		public WorkItem getItem() {
			WorkItem myItem = this.item;
			this.item = null;
			return myItem;
		}
	}
}
