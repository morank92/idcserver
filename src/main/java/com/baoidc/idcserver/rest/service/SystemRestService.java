package com.baoidc.idcserver.rest.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.baoidc.idcserver.po.ArticleContent;
import com.baoidc.idcserver.po.ArticleType;
import com.baoidc.idcserver.po.SysApi;
import com.baoidc.idcserver.po.SysManageUser;
import com.baoidc.idcserver.po.SysMenu;
import com.baoidc.idcserver.po.SysSource;
import com.baoidc.idcserver.po.UserRole;
import com.baoidc.idcserver.service.ISystemService;


@Component
@Path("/system")
public class SystemRestService {
	
	@Resource
	private ISystemService systemService;
	
	@Autowired
	private DataSourceTransactionManager tx;
	@Autowired
	private DefaultTransactionDefinition def;
	
	/*
	 * 根据角色Id查询系统菜单
	 */
	@GET 
	@Path("/getSysMenu")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	public ResponseMessage getSysMenu(@QueryParam("parentId") int parentId,
			@QueryParam("roleId") int roleId,@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage)request.getAttribute("isLogin");
		if(rm != null){
			return rm;
		}else{
			try{
				List<SysMenu> menu = systemService.getMenuResource(parentId,roleId);
				rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
				rm.setData(menu);
			}catch(Exception e){
				e.printStackTrace();
				rm = new ResponseMessage(ErrorCode.EXCEPTION);
			}
		}
		
		return rm;
	}
	
	/**
	 * 查询系统菜单列表
	 * @param parentId
	 * @param roleId
	 * @return
	 */
	@GET 
	@Path("/getMenuResources")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	public ResponseMessage getMenuResources(@Context HttpServletRequest request){
		ResponseMessage rm  = (ResponseMessage)request.getAttribute("isLogin");
		if(rm != null){
			return rm;
		}else{
			try{
				List<SysMenu> sysMenuList = systemService.getMenuResources();
				rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
				rm.setData(sysMenuList);
			}catch(Exception e){
				e.printStackTrace();
				rm = new ResponseMessage(ErrorCode.EXCEPTION);
			}
		}
		return rm;
	}
	
	/**
	 * 查询系统菜单树
	 * @param parentId
	 * @param roleId
	 * @return
	 */
	@GET 
	@Path("/getMenuTree")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	public ResponseMessage getMenuTree(@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage)request.getAttribute("isLogin");
		if(rm != null){
			return rm;
		}else{
			try{
				List<SysMenu> sysMenuList = systemService.getMenuTree();
				rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
				rm.setData(sysMenuList);
			}catch(Exception e){
				e.printStackTrace();
				rm = new ResponseMessage(ErrorCode.EXCEPTION);
			}
		}
		return rm;
	}
	
	@GET
	@Path("/getUserRoles")
	@Produces({MediaType.APPLICATION_JSON})
	public ResponseMessage getUserRoles(@Context HttpServletRequest request){
		ResponseMessage resMessage = (ResponseMessage)request.getAttribute("isLogin");
		if(resMessage != null){
			return resMessage;
		}else{
			try{
				List<UserRole> userRoleList = systemService.getUserRoles();
				resMessage = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
				resMessage.setData(userRoleList);
			}catch(Exception e){
				e.printStackTrace();
				resMessage = new ResponseMessage(ErrorCode.EXCEPTION);
			}
		}
		return resMessage;
	}
	
	@GET
	@Path("/getUserRoleList")
	@Produces({MediaType.APPLICATION_JSON})
	public ResponseMessage getUserRoleList(@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage)request.getAttribute("isLogin");
		if(rm != null){
			return rm;
		}else{
			try{
				List<UserRole> userRoleList = systemService.getUserRoles();
				rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
				rm.setData(userRoleList);
			}catch(Exception e){
				e.printStackTrace();
				rm = new ResponseMessage(ErrorCode.EXCEPTION);
			}
		}
		return rm;
	}
	
	@POST
	@Path("/addNewSystemMenu")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	@ControllerLog(description = "添加系统菜单")
	public ResponseMessage addNewSystemMenu(SysMenu sysMenu,@Context HttpServletRequest request){
		ResponseMessage resMessage = (ResponseMessage)request.getAttribute("isLogin");
		if(resMessage != null){
			return resMessage;
		}else{
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务  
			TransactionStatus txStatus = tx.getTransaction(def);// 获得事务状态
			try{
				systemService.addNewSysMenu(sysMenu);
				resMessage = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
				resMessage.setBusinessDesc("添加系统菜单");
				tx.commit(txStatus);
			}catch(Exception e){
				e.printStackTrace();
				resMessage = new ResponseMessage(ErrorCode.EXCEPTION);
				resMessage.setBusinessDesc("添加系统菜单失败");
				tx.rollback(txStatus);
			}
		}
		return resMessage;
	}
	
	@POST
	@Path("/addNewUserRole")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	@ControllerLog(description = "添加用户角色")
	public ResponseMessage addNewUserRole(UserRole userRole,@Context HttpServletRequest request){
		ResponseMessage resMessage = (ResponseMessage)request.getAttribute("isLogin");
		StringBuilder busiDesc = new StringBuilder();
		if(resMessage != null){
			return resMessage;
		}else{
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务  
			TransactionStatus txStatus = tx.getTransaction(def);// 获得事务状态
			try {
				systemService.createUserRole(userRole);
				resMessage = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
				busiDesc.append("添加用户角色成功，新增角色名：").append(userRole.getRoleName());
				tx.commit(txStatus);
			} catch (Exception e) {
				e.printStackTrace();
				resMessage = new ResponseMessage(ErrorCode.OTHER_ERR);
				busiDesc.append("添加用户角色失败");
				tx.rollback(txStatus);
			}
		}
		resMessage.setBusinessDesc(busiDesc.toString());
		return resMessage;
	}
	
	@POST
	@Path("/delUserRole")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	@ControllerLog(description = "删除用户角色")
	public ResponseMessage delUserRole(@QueryParam("roleId") int roleId,@Context HttpServletRequest request){
		ResponseMessage resMessage = (ResponseMessage)request.getAttribute("isLogin");
		StringBuilder busiDesc = new StringBuilder();
		if(resMessage != null){
			return resMessage;
		}else{
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务  
			TransactionStatus txStatus = tx.getTransaction(def);// 获得事务状态
			try {
				systemService.deleteUserRole(roleId);
				resMessage = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
				busiDesc.append("删除用户角色成功,新增角色ID：").append(roleId);
				tx.commit(txStatus);
			} catch (Exception e) {
				e.printStackTrace();
				tx.rollback(txStatus);
			}
		}
		resMessage.setBusinessDesc(busiDesc.toString());
		return resMessage;
	}
	
	@POST
	@Path("/modifyUserRole")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	@ControllerLog()
	public ResponseMessage modifyUserRole(UserRole userRole,@Context HttpServletRequest request){
		ResponseMessage resMessage = (ResponseMessage)request.getAttribute("isLogin");
		StringBuilder busiDesc = new StringBuilder();
		if(resMessage  !=  null){
			return resMessage;
		}else{
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务  
			TransactionStatus txStatus = tx.getTransaction(def);// 获得事务状态
			try {
				systemService.updateUserRole(userRole);
				resMessage = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
				busiDesc.append("修改用户角色成功，角色名称：").append(userRole.getRoleName());
				tx.commit(txStatus);
			} catch (Exception e) {
				e.printStackTrace();
				tx.rollback(txStatus);
			}
		}
		resMessage.setBusinessDesc(busiDesc.toString());
		return resMessage;
	}
	
	@POST
	@Path("/deleteSysMenu")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	@ControllerLog
	public ResponseMessage deleteSysMenu(@QueryParam("menuId") int menuId,@Context HttpServletRequest request){
		ResponseMessage resMessage = (ResponseMessage)request.getAttribute("isLogin");
		StringBuilder busiDesc = new StringBuilder();
		if(resMessage != null){
			return resMessage;
		}else{
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务  
			TransactionStatus txStatus = tx.getTransaction(def);// 获得事务状态
			try {
				systemService.deleteMenuByMenuId(menuId);
				resMessage = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
				busiDesc.append("删除系统菜单成功，菜单ID：").append(menuId);
				tx.commit(txStatus);
			} catch (Exception e) {
				e.printStackTrace();
				tx.rollback(txStatus);
			}
		}
		resMessage.setBusinessDesc(busiDesc.toString());
		return resMessage;
	}
	
	@POST
	@Path("/modifySystemMenu")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	@ControllerLog(description = "修改系统菜单")
	public ResponseMessage modifySystemMenu(SysMenu sysMenu,@Context HttpServletRequest request){
		ResponseMessage resMessage = (ResponseMessage)request.getAttribute("isLogin");
		StringBuilder busiDesc = new StringBuilder();
		if(resMessage != null){
			return resMessage;
		}else{
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务  
			TransactionStatus txStatus = tx.getTransaction(def);// 获得事务状态
			try {
				systemService.updateSysMenu(sysMenu);
				resMessage = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
				busiDesc.append("修改系统菜单成功，菜单名称：").append(sysMenu.getText());
				tx.commit(txStatus);
			} catch (Exception e) {
				e.printStackTrace();
				tx.rollback(txStatus);
			}
		}
		resMessage.setBusinessDesc(busiDesc.toString());
		return resMessage;
	}
	
	/**
	 * 查询系统API列表
	 * @param parentId
	 * @param roleId
	 * @return
	 */
	@GET 
	@Path("/getSysApis")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	public Map<String, List<SysApi>> getSysApis(){
		return systemService.getSysApiList();
	}
	
	@POST
	@Path("/addNewSystemApi")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	public ResponseMessage addNewSystemApi(SysApi sysApi){
		ResponseMessage resMessage = null;
		systemService.addNewSysApi(sysApi);
		if(sysApi.getId() != 0){
			resMessage = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
		}else{
			resMessage = new ResponseMessage(ErrorCode.OTHER_ERR);
		}
		return resMessage;
	}
	
	@POST
	@Path("/delSysApi")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	public ResponseMessage delSysApi(@QueryParam("apiId") int id){
		ResponseMessage resMessage = null;
		systemService.delSysApiById(id);
		resMessage = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
		return resMessage;
	}
	
	@POST
	@Path("/modifySysApi")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	public ResponseMessage modifySysApi(SysApi sysApi){
		ResponseMessage resMessage = null;
		systemService.updateSysApi(sysApi);
		resMessage = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
		return resMessage;
	}
	
	/**
	 * 查询系统API列表
	 * @param parentId
	 * @param roleId
	 * @return
	 */
	@GET 
	@Path("/getSysSource")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	public Map<String, List<SysSource>> getSysSource(){
		Map<String, List<SysSource>> sysSourceMap = new HashMap<String, List<SysSource>>();
		List<SysSource> sysSourceList = systemService.getSysSourceList();
		if(sysSourceList != null && sysSourceList.size() > 0){
			sysSourceMap.put("aaData", sysSourceList);
		}
		return sysSourceMap;
	}
	
	/**
	 * 查询管理员用户列表
	 * @param parentId
	 * @param roleId
	 * @return
	 */
	@GET 
	@Path("/getSysManageUser")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	public ResponseMessage getSysManageUser(@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage)request.getAttribute("isLogin");
		if(rm != null){
			return rm;
		}else{
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务  
			TransactionStatus txStatus = tx.getTransaction(def);// 获得事务状态
			try{
				List<SysManageUser> sysManageUserList = systemService.getSysManageUserList();
				rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
				rm.setData(sysManageUserList);
				tx.commit(txStatus);
			}catch(Exception e){
				e.printStackTrace();
				rm = new ResponseMessage(ErrorCode.EXCEPTION);
				tx.rollback(txStatus);
			}
		}
		return rm;
	}
	
	@POST
	@Path("/addNewSysManageUser")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	@ControllerLog()
	public ResponseMessage addNewSysManageUser(SysManageUser sysManageUser,@Context HttpServletRequest request){
		ResponseMessage resMessage = (ResponseMessage)request.getAttribute("isLogin");
		StringBuilder busiDesc =  new StringBuilder();
		if(resMessage != null){
			return resMessage;
		}else{
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务  
			TransactionStatus txStatus = tx.getTransaction(def);// 获得事务状态
			try {
				systemService.createSysManageUser(sysManageUser);
				resMessage = new ResponseMessage(ErrorCode.ADD_MANAGEUSER_SUCCESS);
				busiDesc.append("添加系统管理员用户成功，管理员用户名：").append(sysManageUser.getUserName());
				tx.commit(txStatus);
			} catch (Exception e) {
				e.printStackTrace();
				resMessage = new ResponseMessage(ErrorCode.OTHER_ERR);
				busiDesc.append("系统异常，添加系统管理员用户失败");
				tx.rollback(txStatus);
			}
		}
		resMessage.setBusinessDesc(busiDesc.toString());
		return resMessage;
	}
	
	@POST
	@Path("/delSysManageUser")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	@ControllerLog(description = "删除系统管理员用户")
	public ResponseMessage delSysManageUser(@QueryParam("manageUserId") int manageUserId,@Context HttpServletRequest request){
		ResponseMessage resMessage = (ResponseMessage)request.getAttribute("isLogin");
		StringBuilder busiDesc =  new StringBuilder();
		if(resMessage != null){
			return resMessage;
		}else{
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务  
			TransactionStatus txStatus = tx.getTransaction(def);// 获得事务状态
			try {
				systemService.delManageUserById(manageUserId);
				resMessage = new ResponseMessage(ErrorCode.DEL_MANAGEUSER_SUCCESS);
				busiDesc.append("删除系统管理员用户成功，管理员用户Id：").append(manageUserId);
				tx.commit(txStatus);
			} catch (Exception e) {
				e.printStackTrace();
				busiDesc.append("删除系统管理员用户失败，管理员用户Id：").append(manageUserId);
				resMessage = new ResponseMessage(ErrorCode.EXCEPTION);
				tx.rollback(txStatus);
			}
		}
		resMessage.setBusinessDesc(busiDesc.toString());
		return resMessage;
	}
	
	@POST
	@Path("/modifyManageUser")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	@ControllerLog(description = "修改系统管理员用户信息")
	public ResponseMessage modifyManageUser(SysManageUser sysManageUser,@Context HttpServletRequest request){
		ResponseMessage resMessage = (ResponseMessage)request.getAttribute("isLogin");
		StringBuilder busiDesc =  new StringBuilder();
		if(resMessage != null){
			return resMessage;
		}else{
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务  
			TransactionStatus txStatus = tx.getTransaction(def);// 获得事务状态
			try {
				systemService.modifyManageUser(sysManageUser);
				resMessage = new ResponseMessage(ErrorCode.MODIFY_MANAGEUSER_SUCCESS);
				tx.commit(txStatus);
			} catch (Exception e) {
				e.printStackTrace();
				resMessage = new ResponseMessage(ErrorCode.EXCEPTION);
				busiDesc.append("修改系统管理员用户失败，管理员用户Id：").append(sysManageUser.getId());
				tx.rollback(txStatus);
			}
		}
		resMessage.setBusinessDesc(busiDesc.toString());
		return resMessage;
	}
	
	
	@GET
	@Path("/manageLogin")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	@ControllerLog(description = "管理员登录系统")
	public ResponseMessage manageLogin(@QueryParam("userName") String userName,@QueryParam("password") String password,@Context HttpServletRequest request){
		ResponseMessage resMessage = null;
		resMessage = systemService.manageLogin(userName, password,request);
		return resMessage;
	}
	
	@GET 
	@Path("/logout")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	@ControllerLog(description="管理员注销")
	public ResponseMessage logout(@QueryParam("userName")String userName,@Context HttpServletRequest request){
		ResponseMessage rm = new ResponseMessage();
		try {
			systemService.manageLogout(userName);
			rm.setCode("200");
			rm.setBusinessDesc("管理员:"+userName+"注销成功");
			return rm;
		} catch (Exception e) {
			e.printStackTrace();
			rm.setCode("500");
			rm.setMsg("服务器异常");
			rm.setBusinessDesc("管理员:"+userName+"注销失败");
			return rm;
		}
	}
	
	/**
	 * 查询文章列表
	 * @param parentId
	 * @param roleId
	 * @return
	 */
	@GET 
	@Path("/getArticleContentList")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	public ResponseMessage getArticleContentList(@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage)request.getAttribute("isLogin");
		if(rm != null){
			return rm;
		}else{
			try{
				List<ArticleContent> articleContentList = systemService.getArticleContentList();
				rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
				rm.setData(articleContentList);
			}catch(Exception e){
				e.printStackTrace();
				rm = new ResponseMessage(ErrorCode.EXCEPTION);
			}
		}
		return rm;
	}
	
	/**
	 * 查询文章类别列表
	 * @param parentId
	 * @param roleId
	 * @return
	 */
	@GET 
	@Path("/getArticleTypeList")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	public ResponseMessage getArticleTypeList(@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage)request.getAttribute("isLogin");
		if(rm != null){
			return rm;
		}else{
			try{
				List<ArticleType> articleTypeList = systemService.getArticleTypeList();
				rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
				rm.setData(articleTypeList);
			}catch(Exception e){
				e.printStackTrace();
				rm = new ResponseMessage(ErrorCode.EXCEPTION);
			}
		}
		return rm;
	}
	
	@POST
	@Path("/addArticleType")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	@ControllerLog()
	public ResponseMessage addArticleType(ArticleType articleType,@Context HttpServletRequest request){
		ResponseMessage resMessage = (ResponseMessage)request.getAttribute("isLogin");
		StringBuilder busiDesc = new StringBuilder();
		if(resMessage != null){
			return resMessage;
		}else{
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务  
			TransactionStatus txStatus = tx.getTransaction(def);// 获得事务状态
			try {
				systemService.createArticleType(articleType);
				resMessage = new ResponseMessage(ErrorCode.ADD_ARTICLETYPE_SUCCESS);
				busiDesc.append("添加文章类别成功，文章类别名称：").append(articleType.getArticleType());
				tx.commit(txStatus);
			} catch (Exception e) {
				resMessage = new ResponseMessage(ErrorCode.EXCEPTION);
				busiDesc.append("添加文章类别失败，文章类别名称：").append(articleType.getArticleType());
				e.printStackTrace();
				tx.rollback(txStatus);
			}
		}
		resMessage.setBusinessDesc(busiDesc.toString());
		return resMessage;
	}
	
	@POST
	@Path("/delArticleType")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	@ControllerLog(description = "删除文章类别")
	public ResponseMessage delArticleType(@QueryParam("id") int id,@Context HttpServletRequest request){
		ResponseMessage resMessage = (ResponseMessage)request.getAttribute("isLogin");
		StringBuilder busiDesc = new StringBuilder();
		if(resMessage != null){
			return resMessage;
		}else{
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务  
			TransactionStatus txStatus = tx.getTransaction(def);// 获得事务状态
			try {
				systemService.deleteArticleType(id);
				resMessage = new ResponseMessage(ErrorCode.DEL_ARTICLETYPE_SUCCESS);
				busiDesc.append("删除文章类别成功，文章类别ID：").append(id);
				tx.commit(txStatus);
			} catch (Exception e) {
				e.printStackTrace();
				resMessage = new ResponseMessage(ErrorCode.EXCEPTION);
				busiDesc.append("删除文章类别失败，文章类别ID：").append(id);
				tx.rollback(txStatus);
			}
		}
		resMessage.setBusinessDesc(busiDesc.toString());
		return resMessage;
	}
	
	//添加文章
	@POST
	@Path("/addArticleContent")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	@ControllerLog(description = "添加文章")
	public ResponseMessage addArticleContent(ArticleContent articleContent,@Context HttpServletRequest request){
		ResponseMessage resMessage = (ResponseMessage)request.getAttribute("isLogin");
		StringBuilder busiDesc = new StringBuilder();
		if(resMessage != null){
			return resMessage;
		}else{
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务  
			TransactionStatus txStatus = tx.getTransaction(def);// 获得事务状态
			try {
				systemService.createArticleContent(articleContent);
				resMessage = new ResponseMessage(ErrorCode.ADD_ARTICLECONTENT_SUCCESS);
				busiDesc.append("添加文章成功，文章标题：").append(articleContent.getArticleTitle());
				tx.commit(txStatus);
			} catch (Exception e) {
				resMessage = new ResponseMessage(ErrorCode.EXCEPTION);
				busiDesc.append("添加文章失败，文章标题：").append(articleContent.getArticleTitle());
				e.printStackTrace();
				tx.rollback(txStatus);
			}
		}
		resMessage.setBusinessDesc(busiDesc.toString());
		return resMessage;
	}
	
	//删除文章
	@POST
	@Path("/delArticleContent")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	@ControllerLog(description = "删除文章")
	public ResponseMessage delArticleContent(@QueryParam("id") int id,@Context HttpServletRequest request){
		ResponseMessage resMessage = (ResponseMessage)request.getAttribute("isLogin");
		StringBuilder busiDesc = new StringBuilder();
		if(resMessage != null){
			return resMessage;
		}else{
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务  
			TransactionStatus txStatus = tx.getTransaction(def);// 获得事务状态
			try {
				systemService.deleteArticleContent(id);
				resMessage = new ResponseMessage(ErrorCode.DEL_ARTICLECONTENT_SUCCESS);
				busiDesc.append("删除文章成功，文章Id：").append(id);
				tx.commit(txStatus);
			} catch (Exception e) {
				e.printStackTrace();
				busiDesc.append("删除文章失败，文章Id：").append(id);
				resMessage = new ResponseMessage(ErrorCode.EXCEPTION);
				tx.rollback(txStatus);
			}
		}
		resMessage.setBusinessDesc(busiDesc.toString());
		return resMessage;
	}
	
	@POST
	@Path("/editArticleContent")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	@ControllerLog()
	public ResponseMessage editArticleContent(ArticleContent articleContent,@Context HttpServletRequest request){
		ResponseMessage resMessage = (ResponseMessage)request.getAttribute("isLogin");
		StringBuilder busiDesc = new StringBuilder();
		if(resMessage != null){
			return resMessage;
		}else{
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务  
			TransactionStatus txStatus = tx.getTransaction(def);// 获得事务状态
			try {
				systemService.updateArticleContent(articleContent);
				resMessage = new ResponseMessage(ErrorCode.EDIT_ARTICLECONTENT_SUCCESS);
				busiDesc.append("修改文章成功,文章标题：").append(articleContent.getArticleTitle());
				tx.commit(txStatus);
			} catch (Exception e) {
				e.printStackTrace();
				resMessage = new ResponseMessage(ErrorCode.EXCEPTION);
				busiDesc.append("修改文章失败,文章标题：").append(articleContent.getArticleTitle());
				tx.rollback(txStatus);
			}
		}
		resMessage.setBusinessDesc(busiDesc.toString());
		return resMessage;
	}
	
	/**
	 * 查询文章列表
	 * @param parentId
	 * @param roleId
	 * @return
	 */
	@GET 
	@Path("/getArticleContentListForUser")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	public ResponseMessage getArticleContentListForUser(@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage)request.getAttribute("isLogin");
		if(rm != null){
			return rm;
		}else{
			try{
				List<ArticleContent> articleContentList = systemService.getArticleContentListForUser();
				rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
				rm.setData(articleContentList);
			}catch(Exception e){
				e.printStackTrace();
				rm = new ResponseMessage(ErrorCode.EXCEPTION);
			}
		}
		return rm;
	}
	
	/**
	 * 查询文章列表
	 * @param parentId
	 * @param roleId
	 * @return
	 */
	@GET 
	@Path("/getArticleContentDetail")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	public ResponseMessage getArticleContentDetail(@QueryParam("id") int id,@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage)request.getAttribute("isLogin");
		if(rm != null){
			return rm;
		}else{
			try{
				ArticleContent articleContent = systemService.getArticleContentById(id);
				rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
				rm.setData(articleContent);
			}catch(Exception e){
				e.printStackTrace();
				rm = new ResponseMessage(ErrorCode.EXCEPTION);
			}
		}
		return rm;
	}
	
}
