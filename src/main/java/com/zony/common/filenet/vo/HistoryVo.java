package com.zony.common.filenet.vo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HistoryVo extends Vo {

	private Map<String, Object> properties = new HashMap<String, Object>();

	/**
	 * Work object number, which is the identifier of a work item in a queue.
	 */
	private String F_WorkFlowNumber;

	/**
	 * The subject of the workflow.
	 */
	private String subject;

	/**
	 * The name of the workflow.
	 */
	private String workflowName;

	private String queueName;

	/**
	 * The name of the step.
	 */
	private String stepName;

	private Date F_TimeStamp; // Complete Time

	private Integer workClassID;

	private String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getF_WorkFlowNumber() {
		return F_WorkFlowNumber;
	}

	public void setF_WorkFlowNumber(String workFlowNumber) {
		F_WorkFlowNumber = workFlowNumber;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getWorkflowName() {
		return workflowName;
	}

	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

	public String getStepName() {
		return stepName;
	}

	public void setStepName(String stepName) {
		this.stepName = stepName;
	}

	public Date getF_TimeStamp() {
		return F_TimeStamp;
	}

	public void setF_TimeStamp(Date timeStamp) {
		F_TimeStamp = timeStamp;
	}

	public Integer getWorkClassID() {
		return workClassID;
	}

	public void setWorkClassID(Integer workClassID) {
		this.workClassID = workClassID;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public Object getFieldValue(String name) {
		return properties.get(name);
	}

	public void setFieldValue(String name, Object value) {
		properties.put(name, value);
	}
}