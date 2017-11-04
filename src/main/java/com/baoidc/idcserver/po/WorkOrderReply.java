package com.baoidc.idcserver.po;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class WorkOrderReply implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8811692336552742128L;
	
	private int id;
	private int workorderId;
	private int questionId;
	private String replyTime;
	private String replyContent;
	private int userId;
	private SysManageUser user;
	
	
	public int getWorkorderId() {
		return workorderId;
	}
	public void setWorkorderId(int workorderId) {
		this.workorderId = workorderId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getQuestionId() {
		return questionId;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	public String getReplyTime() {
		return replyTime;
	}
	public void setReplyTime(String replyTime) {
		this.replyTime = replyTime;
	}
	public String getReplyContent() {
		return replyContent;
	}
	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}
	public SysManageUser getUser() {
		return user;
	}
	public void setUser(SysManageUser user) {
		this.user = user;
	}
	
	
	
}
