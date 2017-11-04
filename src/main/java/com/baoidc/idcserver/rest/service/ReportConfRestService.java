package com.baoidc.idcserver.rest.service;

import java.util.ArrayList;
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
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.baoidc.idcserver.core.ErrorCode;
import com.baoidc.idcserver.core.ResponseMessage;
import com.baoidc.idcserver.core.SendEmailUtil;
import com.baoidc.idcserver.core.aspect.ControllerLog;
import com.baoidc.idcserver.po.EmailParam;
import com.baoidc.idcserver.po.ReportConf;
import com.baoidc.idcserver.service.IReportConfService;

@Component
@Path("/reportConf")
public class ReportConfRestService {
	
	@Resource
	private  IReportConfService confService;
	
	@Autowired
	private DataSourceTransactionManager tx;
	@Autowired
	private DefaultTransactionDefinition def;
	
	/*
	 * 用户告警配置联系人
	 */
	@POST 
	@Path("/addUserReportConf")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@ControllerLog(description="用户告警配置")
	public ResponseMessage addUserReportConf(List<ReportConf> reportConfArr,@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
			return rm;
		}else{
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务  
			TransactionStatus txStatus = tx.getTransaction(def);// 获得事务状态
			try {
				List<ReportConf> tempList = new ArrayList<ReportConf>();
				tempList.addAll(reportConfArr);
				StringBuilder sb = new StringBuilder();
				confService.addUserReportConf(reportConfArr);
				rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
				ReportConf remove = tempList.remove(0);
				if(remove.getType().equals("note")){
					if(reportConfArr.size()==0){
						sb.append("取消了所有短信告警联系人");
					}else{
						sb.append("重新设置短信告警人为：");
						for (ReportConf reportConf : tempList) {
							sb.append(reportConf.getUsername()+"、");
						}
					}
				}else{
					if(reportConfArr.size()==0){
						sb.append("取消了所有邮件告警联系人");
					}else{
						sb.append("重新设置邮件告警人为：");
						for (ReportConf reportConf : tempList) {
							sb.append(reportConf.getUsername()+"、");
						}
					}
				}
				
				rm.setBusinessDesc(sb.toString());
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
	 * 获取到用户告警配置联系人
	 */
	@GET 
	@Path("/queryUserReportConf")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public ResponseMessage queryUserReportConf(@QueryParam("userId")int userId,@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
			return rm;
		}else{
			List<ReportConf> list = confService.queryUserReportConf(userId);
			rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
			rm.setData(list);
			return rm;
		}
	}
	
	/*
	 * 获取到管理员邮箱告警配置参数
	 */
	@GET 
	@Path("/queryManageEmailParam")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public ResponseMessage queryManageEmailParam(@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
			return rm;
		}else{
			EmailParam param= confService.queryManageEmailParam();
			rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
			rm.setData(param);
			return rm;
		}
	}
	/*
	 * 删除告警配置参数
	 */
	@GET 
	@Path("/deleteManageEmailParam")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public ResponseMessage deleteManageEmailParam(@QueryParam("id")int id,@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
			return rm;
		}else{
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务  
			TransactionStatus txStatus = tx.getTransaction(def);// 获得事务状态
			try {
				confService.deleteManageEmailParam(id);
				rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
				tx.commit(txStatus);
				return rm;
			} catch (Exception e) {
				e.printStackTrace();
				rm = new ResponseMessage(ErrorCode.EXCEPTION);
				return rm;
			}
		}
	}
	/*
	 * 修改邮箱告警配置参数
	 */
	@POST 
	@Path("/updateManageEmailParam")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public ResponseMessage queryManageEmailParam(EmailParam emailParam,@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
			return rm;
		}else{
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务  
			TransactionStatus txStatus = tx.getTransaction(def);// 获得事务状态
			try {
				confService.updateManageEmailParam(emailParam);
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
	 * 测试邮件告警配置是否成功
	 */
	@POST 
	@Path("/testManageEmailParam")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public ResponseMessage testManageEmailParam(EmailParam emailParam,@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
			return rm;
		}else{
			rm = new ResponseMessage();
			try {
				SendEmailUtil.sendMimeMessage(emailParam,emailParam.getReceiveEmail(),"测试邮件","邮件告警配置参数设置成功。");
				rm.setCode("000");
				rm.setMsg("邮件发送成功，请查收");
				return rm;
			} catch (Exception e) {
				e.printStackTrace();
				rm.setCode("500");
				rm.setMsg("邮件发送失败，请核对配置信息");
				return rm;
			}
		}
	}
	/*
	 * 添加邮箱告警配置参数
	 */
	@POST 
	@Path("/insertManageEmailParam")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public ResponseMessage insertManageEmailParam(EmailParam emailParam,@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
			return rm;
		}else{
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务  
			TransactionStatus txStatus = tx.getTransaction(def);// 获得事务状态
			try {
				confService.insertManageEmailParam(emailParam);
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
	
}
