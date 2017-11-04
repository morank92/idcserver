package com.baoidc.idcserver.po;

import java.util.List;

public class WorkorderQuestion {
	private int questionId;
	private int workorderId;
	private  String questionCreateTime;
	private String questionContent;
	private List<WorkOrderReply> replyList;
	
	
	public List<WorkOrderReply> getReplyList() {
		return replyList;
	}
	public void setReplyList(List<WorkOrderReply> replyList) {
		this.replyList = replyList;
	}
	public int getQuestionId() {
		return questionId;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	public int getWorkorderId() {
		return workorderId;
	}
	public void setWorkorderId(int workorderId) {
		this.workorderId = workorderId;
	}
	public String getQuestionCreateTime() {
		return questionCreateTime;
	}
	public void setQuestionCreateTime(String questionCreateTime) {
		this.questionCreateTime = questionCreateTime;
	}
	public String getQuestionContent() {
		return questionContent;
	}
	public void setQuestionContent(String questionContent) {
		this.questionContent = questionContent;
	}
	
	
}
