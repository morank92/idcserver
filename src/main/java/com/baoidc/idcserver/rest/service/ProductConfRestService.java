package com.baoidc.idcserver.rest.service;

import java.util.List;

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

import com.baoid.idcserver.vo.IntStringClass;
import com.baoidc.idcserver.core.ErrorCode;
import com.baoidc.idcserver.core.ResponseMessage;
import com.baoidc.idcserver.core.aspect.ControllerLog;
import com.baoidc.idcserver.po.DeviceSourceParam;
import com.baoidc.idcserver.service.ProductConfService;

/*
 * 系统产品配置参数接口
 */

@Component
@Path("/productConf")
public class ProductConfRestService {
	
	@Autowired
	private ProductConfService productConfSer;
	
	@Autowired
	private DataSourceTransactionManager tx;
	@Autowired
	private DefaultTransactionDefinition def;
	
	/*
	 * 获取所有以配置的参数信息
	 */
	@GET 
	@Path("/getAllConfParam")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	public ResponseMessage getAllConfParam(@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
			return rm;
		}else{
			rm = new ResponseMessage();
			List<DeviceSourceParam> list = productConfSer.getAllConfParam();
			rm.setCode("200");
			rm.setObj(list);
			return rm;
		}
	}
	/*
	 * 删除配置项同时删除配置和产品的关系
	 */
	@GET 
	@Path("/deleteParam")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	@ControllerLog(description="删除配置参数")
	public ResponseMessage deleteParam(@QueryParam("id")int id,@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
			return rm;
		}else{
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务  
			TransactionStatus txStatus = tx.getTransaction(def);// 获得事务状态
			try {
				rm = new ResponseMessage();
				productConfSer.deleteParam(id);
				rm.setMsg("删除成功");
				rm.setCode("200");
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
	 * 获取某一种产品的配置项
	 */
	@GET 
	@Path("/getProductParam")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	public ResponseMessage getProductParam(@QueryParam("proId")int proId,@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
			return rm;
		}else{
			rm = new ResponseMessage();
			List<Integer> idList = productConfSer.getProductParam(proId);
			rm.setCode("200");
			rm.setObj(idList);
			return rm;
		}
	}
	/*
	 * 获取最外层的提供项
	 */
	@GET 
	@Path("/getServerOffer")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	public ResponseMessage getServerOffer(@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
			return rm;
		}else{
			rm = new ResponseMessage();
			List<IntStringClass> list = productConfSer.getServerOffer();
			rm.setCode("200");
			rm.setObj(list);
			return rm;
		}
	}
	/*
	 * 根据最外层的提供想 查询当前所提供的产品
	 */
	@GET 
	@Path("/getProductOffer")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	public ResponseMessage getProductOffer(@QueryParam("serverId")int serverId,@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
			return rm;
		}else{
			rm = new ResponseMessage();
			List<Integer> list = productConfSer.getProductOffer(serverId);
			rm.setCode("200");
			rm.setObj(list);
			return rm;
		}
	}
	/*
	 * 判断机房下是否有资产
	 */
	@GET 
	@Path("/checkHostRoomUse")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	public ResponseMessage checkHostRoomUse(@QueryParam("id")int id,@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
			return rm;
		}else{
			boolean flag = productConfSer.checkHostRoomUse(id);
			rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
			rm.setData(flag);
			return rm;
		}
	}
	/*
	 * 添加配置项同时修改更改的配置项
	 */
	@POST 
	@Path("/addAndUpdateParam")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	@ControllerLog(description="添加修改配置项")
	public ResponseMessage addAndUpdateParam(List<DeviceSourceParam> paramList,@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
			return rm;
		}else{
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务  
			TransactionStatus txStatus = tx.getTransaction(def);// 获得事务状态
			try {
				rm = new ResponseMessage();
				productConfSer.configParam(paramList);
				rm.setMsg("配置成功");
				rm.setCode("200");
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
	 * 配置产品
	 */
	@POST 
	@Path("/toConfProductParam")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	@ControllerLog(description="配置产品")
	public ResponseMessage toConfProductParam(List<Integer> checkedArr,@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
			return rm;
		}else{
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务  
			TransactionStatus txStatus = tx.getTransaction(def);// 获得事务状态
			try {
				rm = new ResponseMessage();
				productConfSer.toConfProductParam(checkedArr);
				rm.setMsg("配置成功");
				rm.setCode("200");
				rm.setBusinessDesc("配置产品参数信息成功");
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
