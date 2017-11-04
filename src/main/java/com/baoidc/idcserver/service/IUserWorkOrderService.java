package com.baoidc.idcserver.service;

import java.util.List;

import com.baoidc.idcserver.po.UserWorkOrder;
import com.baoidc.idcserver.po.WorkOrderReply;
import com.baoidc.idcserver.po.WorkorderQuestion;

public interface IUserWorkOrderService {
	
	public List<UserWorkOrder> getWorkOrderByUser(int userId,int status);
	
	public void createNewWorkOrder(UserWorkOrder workOrder);
	
	public void deleteWorkOrder(int workOrderId);
	
	public int getNoDealWorkOrder(int userId);
	
	public List<UserWorkOrder> getWorkOrderList();
	
	
	public List<UserWorkOrder> getWorkOrderListByStatus();
	
	//管理员对工单问题的回复
	public void createQuestionReply(WorkOrderReply reply);

	//工单处理完成，结束会话
	public void dealWorkorderSuccess(int workorderId);

	//在工单的基础上提问
	public void createQuestion(WorkorderQuestion workOrderQuestion);

}
