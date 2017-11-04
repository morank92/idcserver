package com.baoid.idcserver.vo;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.baoidc.idcserver.po.UserWorkOrder;
import com.baoidc.idcserver.po.WorkOrderReply;

@XmlRootElement
public class WorkOrderVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4663489090185888463L;
	
	private UserWorkOrder workOrder;
	private List<WorkOrderReply> orderReplyList;
	
	public UserWorkOrder getWorkOrder() {
		return workOrder;
	}
	public void setWorkOrder(UserWorkOrder workOrder) {
		this.workOrder = workOrder;
	}
	public List<WorkOrderReply> getOrderReplyList() {
		return orderReplyList;
	}
	public void setOrderReplyList(List<WorkOrderReply> orderReplyList) {
		this.orderReplyList = orderReplyList;
	}

}
