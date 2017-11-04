package com.baoidc.idcserver.rest.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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

import com.baoid.idcserver.vo.ConsumeAnalyze;
import com.baoid.idcserver.vo.FinanceAnalyze;
import com.baoidc.idcserver.core.ErrorCode;
import com.baoidc.idcserver.core.ResponseMessage;
import com.baoidc.idcserver.core.aspect.ControllerLog;
import com.baoidc.idcserver.po.EncashmentRecord;
import com.baoidc.idcserver.service.FinanceManageService;

/*
 * 财务管理接口
 */

@Component
@Path("/financeManage")
public class FinanceManageRestService {
	
	@Autowired 
	private FinanceManageService financeManageService;
	
	@Autowired
	private DataSourceTransactionManager tx;
	@Autowired
	private DefaultTransactionDefinition def;
	
	/*
	 *财务分析  查询近半年的充值提现记录 
	 */
	@GET 
	@Path("/getChargeEnshmentHalf")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	public ResponseMessage getChargeEnshmentHalf(@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
			return rm;
		}else{
			List<Object> list = financeManageService.getChargeEnshmentHalf();
			rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
			rm.setData(list);
			return rm;
		}
	}
	/*
	 *财务分析  查询近半年的消费情况
	 */
	@GET 
	@Path("/getConsumeHalf")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	public ResponseMessage getConsumeHalf(@QueryParam("userId")int userId,@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
			return rm;
		}else{
			List<Object> list = financeManageService.getConsumeHalf(userId);
			rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
			rm.setData(list);
			return rm;
		}
	}
	
	/*
	 * 财务分析  获取每月充值  提现记录
	 */
	@GET 
	@Path("/getChargeEnshment")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	public ResponseMessage getChargeEnshment(@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
			return rm;
		}else{
			List<FinanceAnalyze> list = financeManageService.getFinanceAnalyzeByMonth();
			rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
			rm.setData(list);
			return rm;
		}
	}
	
	/*
	 * 消费分析，获取消费信息
	 */
	@GET 
	@Path("/getConsumeAnalyze")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	public ResponseMessage getConsumeAnalyze(@QueryParam("startTime")String startTime,@QueryParam("endTime")String endTime,
			@QueryParam("userId")int userId,@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
			return rm;
		}else{
			List<ConsumeAnalyze> list = financeManageService.getAllConsumeRecord(startTime,endTime,userId);
			rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
			rm.setData(list);
			return rm;
		}
	}
	
	/*
	 * 提现管理--查询所有提现记录
	 */
	@GET 
	@Path("/getEncashmentRecord")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	public ResponseMessage getEncashmentRecord(@QueryParam("userId")int userId,@QueryParam("startTime")String startTime,
				@QueryParam("endTime")String endTime,@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
			return rm;
		}else{
			List<EncashmentRecord> list = financeManageService.getEncashmentRecord(userId,startTime,endTime);
			rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
			rm.setData(list);
			return rm;
		}
	}
	
	/*
	 * 提现管理--处理提现   打款操作
	 */
	@GET 
	@Path("/updateEncashmentStatus")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	@ControllerLog(description="处理提现打款")
	public ResponseMessage updateEncashmentStatus(@QueryParam("id")int id,@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		StringBuilder busiDesc = new StringBuilder();
		if(rm!=null){
			return rm;
		}else{
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务  
			TransactionStatus txStatus = tx.getTransaction(def);// 获得事务状态  
			try {
				financeManageService.updateEncashmentRecordStatus(id);
				rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
				busiDesc.append("提现处理成功，");
				EncashmentRecord er = financeManageService.getEncashmentById(id);
				if(er != null){
					busiDesc.append("提现用户：").append(er.getUser().getUserName()).append(",提现金额：").append(er.getEncashmentAmt());
				}
				rm.setBusinessDesc(busiDesc.toString());
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
	 * 提现管理--删除提现记录
	 */
	@GET 
	@Path("/deleteEncashmentRecord")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	@ControllerLog(description="删除提现记录")
	public ResponseMessage deleteEncashmentRecord(@QueryParam("id")int id,@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		StringBuilder busiDesc = new StringBuilder();
		if(rm!=null){
			return rm;
		}else{
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务  
			TransactionStatus txStatus = tx.getTransaction(def);// 获得事务状态  
			try {
				financeManageService.deleteEncashmentRecord(id);
				rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
				busiDesc.append("删除提现记录成功，提现记录ID：").append(id);
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
}
