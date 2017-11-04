package com.baoidc.idcserver.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baoidc.idcserver.core.DateUtil;
import com.baoidc.idcserver.core.SystemContant;
import com.baoidc.idcserver.core.WorkOrderStatus;
import com.baoidc.idcserver.dao.IWorkOrderDAO;
import com.baoidc.idcserver.po.UserWorkOrder;
import com.baoidc.idcserver.po.WorkOrderReply;
import com.baoidc.idcserver.po.WorkorderQuestion;
import com.baoidc.idcserver.service.IUserWorkOrderService;

@Service("userWorkOrderService")
public class UserWorkOrderServiceImpl implements IUserWorkOrderService {
	
	@Autowired
	private IWorkOrderDAO workOrderDAO;

	
	public List<UserWorkOrder> getWorkOrderByUser(int userId,int status) {
		return workOrderDAO.getUserWorkOrderList(userId,status);
	}

	//用户创建工单
	public void createNewWorkOrder(UserWorkOrder workOrder) {
		if(workOrder != null){
			Date currentDate = new Date();
			String orderNo = SystemContant.COMMON_CONTANT+DateUtil.dateToStr(currentDate, "yyyyMMddHHmmss");//这块算法需要优化,以免并发冲突
			workOrder.setOrderNo(orderNo);
			String createTime = DateUtil.dateToStr(currentDate, "yyyy-MM-dd HH:mm:ss");
			//新建工单创建时间和最后更新时间一致
			workOrder.setCreateTime(createTime);
			workOrder.setUpdateTime(createTime);
			workOrder.setOrderStatus(WorkOrderStatus.NEW.getStatusVal());
			workOrderDAO.createWorkOrder(workOrder);
			WorkorderQuestion question = new WorkorderQuestion();
			question.setQuestionContent(workOrder.getOrderDesc());
			question.setQuestionCreateTime(workOrder.getCreateTime());
			question.setWorkorderId(workOrder.getWorkOrderId());
			//创建工单的问题
			workOrderDAO.createWorkOrderQuestion(question);
			
		}
	}
	
	//删除工单
	public void deleteWorkOrder(int workOrderId){
		//删除工单
		workOrderDAO.deleteWorkOrder(workOrderId);
		//删除工单问题
		workOrderDAO.deleteWordOrderQuestion(workOrderId);
		//删除工单回复
		workOrderDAO.deleteWorkOrderReply(workOrderId);
	}

//	查询待处理工单个数
	public int getNoDealWorkOrder(int userId) {
		return workOrderDAO.getNoDealWorkOrder(userId);
	}

	//管理员查询所有工单
	public List<UserWorkOrder> getWorkOrderList() {
		return workOrderDAO.getWorkOrderList();
	}

	//查询未处理工单列表
	public List<UserWorkOrder> getWorkOrderListByStatus() {
		return workOrderDAO.getWorkOrderListByStatus();
	}
	
	//管理员对工单问题的回复
	public void createQuestionReply(WorkOrderReply reply){
		String currentTime = DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss");
		reply.setReplyTime(currentTime);
		workOrderDAO.createQuestionReply(reply);
		UserWorkOrder workOrder = new UserWorkOrder();
		workOrder.setWorkOrderId(reply.getWorkorderId());
		String updateTime = DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss");
		workOrder.setOrderStatus(1);
		workOrder.setUpdateTime(updateTime);
		workOrderDAO.updateWorkOrder(workOrder);
	}
	
	//在工单的基础上提问
	public void createQuestion(WorkorderQuestion workOrderQuestion){
		String createTime = DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss");
		workOrderQuestion.setQuestionCreateTime(createTime);
		workOrderDAO.createWorkOrderQuestion(workOrderQuestion);
	}
	
	//工单处理完成，结束会话
	public void dealWorkorderSuccess(int workorderId){
		UserWorkOrder workOrder = new UserWorkOrder();
		workOrder.setWorkOrderId(workorderId);
		String updateTime = DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss");
		workOrder.setOrderStatus(2);
		workOrder.setUpdateTime(updateTime);
		workOrderDAO.updateWorkOrder(workOrder);
	}


}
