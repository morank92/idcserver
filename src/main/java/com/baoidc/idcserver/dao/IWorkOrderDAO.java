package com.baoidc.idcserver.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baoidc.idcserver.po.UserWorkOrder;
import com.baoidc.idcserver.po.WorkOrderReply;
import com.baoidc.idcserver.po.WorkorderQuestion;

public interface IWorkOrderDAO {
	
	//获取用户工单列表
	public List<UserWorkOrder> getUserWorkOrderList(@Param("userId") int userId,@Param("status") int status);
	
	//获取所有工单列表
	public List<UserWorkOrder> getWorkOrderList();
	
	//获取工单详情
	public UserWorkOrder getWorkOrderDetail(int workOrderId);
	
	//创建工单
	public int createWorkOrder(UserWorkOrder userWorkOrder);
	
	//修改工单状态 更新时间
	public void updateWorkOrder(UserWorkOrder userWorkOrder);
	
	//删除工单
	public void deleteWorkOrder(int workOrderId);
	
	//删除工单回复
	public void deleteWorkOrderReply(int workOrderId);
	
	//删除工单问题
	public void deleteWordOrderQuestion(int workOrderId);
	
//	查询待处理工单个数
	public int getNoDealWorkOrder(int userId);
	
	public List<UserWorkOrder> getWorkOrderListByStatus();

	//创建工单的问题
	public void createWorkOrderQuestion(WorkorderQuestion question);
	
	//管理员对工单问题的回复
	public void createQuestionReply(WorkOrderReply reply);

}
