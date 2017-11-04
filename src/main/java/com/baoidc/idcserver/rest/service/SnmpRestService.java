package com.baoidc.idcserver.rest.service;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baoidc.idcserver.core.DateUtil;
import com.baoidc.idcserver.core.ErrorCode;
import com.baoidc.idcserver.core.ResponseMessage;
import com.baoidc.idcserver.core.aspect.ControllerLog;
import com.baoidc.idcserver.po.SnmpConfUser;
import com.baoidc.idcserver.po.ipmiUser;
import com.baoidc.idcserver.service.ISnmpService;
import com.baoidc.idcserver.vo.query.SnmpResult;

@Component
@Path("/snmp")
public class SnmpRestService { //controller
	
	@Autowired
	private ISnmpService snmpService;
	//查询snmp
	@POST @Path("/getSnmpResult")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@Transactional
	public ResponseMessage getSnmpResultBySnmpQuery(SnmpConfUser snmpConf,@Context HttpServletRequest request) {
		
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
		  return rm;
		}else{
			  SnmpResult result = snmpService.getSnmpResultBySnmpConfUser(snmpConf);
			  rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);//
			  rm.setData(result);
			  return rm;
		}
	}
	
	//创建或者更新用户snmp配置
	@POST @Path("/setSnmpMessage")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@Transactional
	@ControllerLog(description="更新snmp的配置信息")
	public ResponseMessage setSnmpMessage(SnmpConfUser snmpConf,@Context HttpServletRequest request) {
		SnmpConfUser snmp = new SnmpConfUser();
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		StringBuilder busiDesc = new StringBuilder();
		if(rm!=null){
		  return rm;
		}else{
			SnmpConfUser calle = snmpService.getSnmpConf(snmpConf);
			if (calle != null) {// 之前有配置，所以更新
				snmpService.updateSnmpConf(snmpConf);
				snmp = calle;
			} else {// 新增
				SnmpConfUser e = snmpService.setSnmpMessage(snmpConf);
				snmp = e;
				calle = e;
			}
			rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
			rm.setData(snmp);
			busiDesc.append("更新snmp配置信息成功，用户Id:").append(calle.getUserId())
					.append("，实例Id：").append(calle.getInstanceId())
					.append("，snmp ip：").append(calle.getSnmpIp());
			rm.setBusinessDesc(busiDesc.toString());
			return rm;
		}
	}
	
	//创建用户snmp配置
	@POST @Path("/getSnmpConf")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@Transactional
	public ResponseMessage getSnmpConf(SnmpConfUser snmpConf,@Context HttpServletRequest request) {
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
		  return rm;
		}else{
			SnmpConfUser se = snmpService.getSnmpConf(snmpConf);
			rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);//
			rm.setData(se);
			return rm;
		}
	}
	
	/*//
	@POST @Path("/toConfirmIpmi")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@Transactional
	public ResponseMessage toConfirmIpmi(ipmiUser ipuser) {
		snmpService.toConfirmIpmi(ipuser);
		
		
		
	}*/
	//开机
	@POST @Path("/toStartByIpmi")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor=Exception.class,timeout=3000)
	@ControllerLog
	public ResponseMessage toStartByIpmi(ipmiUser ipmiUser,@Context HttpServletRequest request) throws Exception {
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		StringBuilder busiDesc = new StringBuilder();
	    if(rm!=null){
	      return rm;
	    }else{
			boolean flag = snmpService.toStartByIpmi(ipmiUser);
			if (flag) {
				rm = new ResponseMessage(ErrorCode.START_SUCCESS_IPMI);// 开机成功
				busiDesc.append("远程开机成功，管理IP：").append(ipmiUser.getIpStr())
						.append("，开机时间：").append(DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
			} else {
				rm = new ResponseMessage(ErrorCode.START_FAILD_IPMI);// 当前机器处于开机状态
				busiDesc.append("机器已处理开机状态，操作时间：").append(DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
			}
			rm.setBusinessDesc(busiDesc.toString());
			return rm;
	    }
	}
	
	//关机
	@POST @Path("/toEndByIpmi")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor=Exception.class,timeout=3000)
	@ControllerLog
	public ResponseMessage toEndByIpmi(ipmiUser ipmiUser,@Context HttpServletRequest request) throws Exception {
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		StringBuilder busiDesc = new StringBuilder();
	    if(rm!=null){
	      return rm;
	    }else{
			boolean flag = snmpService.toEndByIpmi(ipmiUser);
			if (flag) {
				rm = new ResponseMessage(ErrorCode.END_SUCCESS_IPMI);// 关机成功
				busiDesc.append("远程关机成功，管理IP：")
						.append(ipmiUser.getIpStr())
						.append("，关机时间：")
						.append(DateUtil.dateToStr(new Date(),
								"yyyy-MM-dd HH:mm:ss"));
			} else {
				rm = new ResponseMessage(ErrorCode.END_FAILD_IPMI);// 当前机器处于关机状态，关机失败
				busiDesc.append("机器已处理关机状态，操作时间：").append(
						DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
			}
			rm.setBusinessDesc(busiDesc.toString());
			return rm;
	    }
	}
	
}
