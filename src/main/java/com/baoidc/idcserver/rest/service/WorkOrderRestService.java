package com.baoidc.idcserver.rest.service;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.baoidc.idcserver.core.ErrorCode;
import com.baoidc.idcserver.core.ResponseMessage;
import com.baoidc.idcserver.core.aspect.ControllerLog;
import com.baoidc.idcserver.po.UserWorkOrder;
import com.baoidc.idcserver.po.WorkOrderReply;
import com.baoidc.idcserver.po.WorkorderQuestion;
import com.baoidc.idcserver.service.IUserWorkOrderService;

@Component
@Path("/workOrder")
public class WorkOrderRestService {
	
	@Resource
	private IUserWorkOrderService workOrderService;
	
	@Autowired
	private DataSourceTransactionManager tx;
	@Autowired
	private DefaultTransactionDefinition def;
	
	@GET 
	@Path("/getWorkOrderListByUser")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@Transactional
	public ResponseMessage getWorkOrderListByUser(@QueryParam("userId") int userId,
			@QueryParam("status") int status,
			@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
			return rm;
		}else{
			rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
			List<UserWorkOrder> workOrderList = workOrderService.getWorkOrderByUser(userId,status);
			rm.setData(workOrderList);
			return rm;
		}
	}
	
	/*
	 * 用户创建工单
	 */
	@POST @Path("/addNewWorkOrder")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@ControllerLog
	@Transactional
	public ResponseMessage addNewWorkOrder(UserWorkOrder workOrder,@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		StringBuilder busiDesc = new StringBuilder();
		if(rm!=null){
			return rm;
		}else{
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务  
			TransactionStatus txStatus = tx.getTransaction(def);// 获得事务状态  
			try {
				workOrderService.createNewWorkOrder(workOrder);
				rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
				busiDesc.append("创建工单成功，工单标题：").append(workOrder.getOrderTitle()).append("，设备IP：").append(workOrder.getDeviceIp());
				rm.setBusinessDesc(busiDesc.toString());
				tx.commit(txStatus);
				return rm;
			} catch (Exception e) {
				rm = new ResponseMessage(ErrorCode.OTHER_ERR);
				busiDesc.append("系统错误，创建工单失败");
				rm.setBusinessDesc(busiDesc.toString());
				tx.rollback(txStatus);
				return rm;
			}
		}
		
	}
	
	//删除工单
	@GET @Path("/deleteWorkOrder")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@ControllerLog
	@Transactional
	public ResponseMessage deleteWorkOrder(@QueryParam("workOrderId") int workOrderId,@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		StringBuilder busiDesc = new StringBuilder();
		if(rm!=null){
			return rm;
		}else{
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务  
			TransactionStatus txStatus = tx.getTransaction(def);// 获得事务状态  
			try {
				workOrderService.deleteWorkOrder(workOrderId);
				rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
				busiDesc.append("删除工单成功，工单id:").append(workOrderId);
				rm.setBusinessDesc(busiDesc.toString());
				tx.commit(txStatus);
				return rm;
			} catch (Exception e) {
				e.printStackTrace();
				rm = new ResponseMessage(ErrorCode.EXCEPTION);
				tx.rollback(txStatus);
				return rm;
			}
		}
		
	}
	
	/*
	 * 查询待处理工单个数
	 */
	@GET @Path("/getNoDealWorkOrder")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@Transactional
	public ResponseMessage getNoDealWorkOrder(@QueryParam("userId")int userId,@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
			return rm;
		}else{
			int count = workOrderService.getNoDealWorkOrder(userId);
			rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
			rm.setData(count);
			return rm;
		}
	}
	
	
	/*
	 * 查询工单列表
	 */
	@GET @Path("/getWorkOrderList")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@Transactional
	public ResponseMessage getWorkOrderList(@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage)request.getAttribute("isLogin");
		if(rm != null){
			return rm;
		}else{
			try {
				List<UserWorkOrder> workOrderList = workOrderService.getWorkOrderList();
				rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
				rm.setData(workOrderList);
				return rm;
			} catch (Exception e) {
				e.printStackTrace();
				return rm;
			}
		}
	}
	
	/*
	 * 管理员对工单问题的回复
	 */
	@POST @Path("/createQuestionReply")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@Transactional
	public ResponseMessage createQuestionReply(WorkOrderReply workOrderReply,
			@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage)request.getAttribute("isLogin");
		if(rm != null){
			return rm;
		}else{
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务  
			TransactionStatus txStatus = tx.getTransaction(def);// 获得事务状态  
			try {
				workOrderService.createQuestionReply(workOrderReply);
				rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
				tx.commit(txStatus);
				return rm;
			} catch (Exception e) {
				e.printStackTrace();
				rm = new ResponseMessage(ErrorCode.EXCEPTION);
				tx.rollback(txStatus);
				return rm;
			}
		}
	}
	/*
	 * 用户对某个工单继续提出问题
	 */
	@POST @Path("/createQuestion")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@Transactional
	public ResponseMessage createQuestion(WorkorderQuestion workOrderQuestion,
			@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage)request.getAttribute("isLogin");
		if(rm != null){
			return rm;
		}else{
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务  
			TransactionStatus txStatus = tx.getTransaction(def);// 获得事务状态  
			try {
				workOrderService.createQuestion(workOrderQuestion);
				rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
				tx.commit(txStatus);
				return rm;
			} catch (Exception e) {
				e.printStackTrace();
				tx.rollback(txStatus);
				rm = new ResponseMessage(ErrorCode.EXCEPTION);
				return rm;
			}
		}
	}
	
	
	/*
	 * 查询待处理工单列表
	 */
	@GET @Path("/getWorkOrderListByStatus")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@Transactional
	public ResponseMessage getWorkOrderListByStatus(@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage)request.getAttribute("isLogin");
		if(rm != null){
			return rm;
		}else{
			List<UserWorkOrder> workOrderList = workOrderService.getWorkOrderListByStatus();
			rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
			rm.setData(workOrderList);
			return rm;
		}
	}
	
	/*
	 * 结束工单，处理完成  整个对话完成
	 */
	@POST @Path("/dealWorkorderSuccess")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@Transactional
	public ResponseMessage dealWorkorderSuccess(WorkOrderReply workOrderReply,@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage)request.getAttribute("isLogin");
		if(rm != null){
			return rm;
		}else{
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务  
			TransactionStatus txStatus = tx.getTransaction(def);// 获得事务状态  
			try {
				//给出最后的回复
				workOrderService.createQuestionReply(workOrderReply);
				workOrderService.dealWorkorderSuccess(workOrderReply.getWorkorderId());//更新状态
				rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
				tx.commit(txStatus);
				return rm;
			} catch (Exception e) {
				e.printStackTrace();
				rm = new ResponseMessage(ErrorCode.EXCEPTION);
				tx.rollback(txStatus);
				return rm;
			}
		}
	}
}
