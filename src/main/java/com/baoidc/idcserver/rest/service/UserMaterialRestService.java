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

import com.baoidc.idcserver.core.CustomUtil;
import com.baoidc.idcserver.core.ErrorCode;
import com.baoidc.idcserver.core.RedisUtil;
import com.baoidc.idcserver.core.ResponseMessage;
import com.baoidc.idcserver.core.aspect.ControllerLog;
import com.baoidc.idcserver.po.CustomerManager;
import com.baoidc.idcserver.po.SysManageUser;
import com.baoidc.idcserver.po.User;
import com.baoidc.idcserver.po.UserContacts;
import com.baoidc.idcserver.service.ISystemService;
import com.baoidc.idcserver.service.IUserMaterialService;
import com.baoidc.idcserver.service.IUserService;


@Component
@Path("/userMaterial")
public class UserMaterialRestService {
	
	@Autowired 
	private IUserMaterialService userMaterialService;
	
	@Autowired
	private RedisUtil redisUtil;
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private ISystemService systemService;
	
	@Autowired
	private DataSourceTransactionManager tx;
	@Autowired
	private DefaultTransactionDefinition def;
	
	/*
	 * 添加联系人
	 */
	@POST 
	@Path("/addUserMaterial")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	@ControllerLog()
	public ResponseMessage addUserMaterial(UserContacts userContacts,@Context HttpServletRequest request) throws Exception{
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		StringBuilder busiDesc = new StringBuilder();
		if(rm!=null){
			return rm;
		}else{
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
			TransactionStatus txStatus = tx.getTransaction(def);
			try{
				userMaterialService.addUserContact(userContacts);
				rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
				int userId = userContacts.getUserId();
				User user = userMaterialService.getUserByUserId(userId);
				busiDesc.append("用户").append(user.getUserName()).append("添加联系人成功").append("，新增联系人姓名：").append(userContacts.getRealName());
				tx.commit(txStatus);
			}catch(Exception e){
				e.printStackTrace();
				rm = new ResponseMessage(ErrorCode.EXCEPTION);
				busiDesc.append("系统异常，添加联系人失败");
				tx.rollback(txStatus);
			}
			rm.setBusinessDesc(busiDesc.toString());
			return rm;
		}
	}
	
	/*
	 * 获取所有联系人
	 */
	@GET 
	@Path("/getUserMaterial")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	public ResponseMessage getUserMaterial(@QueryParam("userId")Integer userId,@Context HttpServletRequest request) throws Exception{
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
			return rm;
		}else{
			List<UserContacts> userContactsList = userMaterialService.getUserContacts(userId);
			rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
			rm.setData(userContactsList);
			return rm;
		}
	}
	
	/*
	 * 删除联系人
	 */
	@POST 
	@Path("/deleteUserMaterial")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	@ControllerLog(description="删除联系人")
	public ResponseMessage deleteUserMaterial(Integer[] idsjson,@Context HttpServletRequest request) throws Exception{
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		StringBuilder busiDesc = new StringBuilder();
		if(rm!=null){
			return rm;
		}else{
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务  
			TransactionStatus txStatus = tx.getTransaction(def);// 获得事务状态
			try{
				userMaterialService.deleteUserContact(idsjson);
				rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
				busiDesc.append("删除联系人成功，本次删除").append(idsjson.length).append("个联系人");
				tx.commit(txStatus);
			}catch(Exception e){
				e.printStackTrace();
				rm = new ResponseMessage(ErrorCode.EXCEPTION);
				busiDesc.append("系统异常，删除联系人失败");
				tx.rollback(txStatus);
			}
			rm.setBusinessDesc(busiDesc.toString());
			return rm;
		}
	}
	/*
	 * 修改联系人
	 */
	@POST 
	@Path("/updateUserMaterial")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	@ControllerLog(description="修改联系人")
	public ResponseMessage updateUserMaterial(UserContacts userContacts,@Context HttpServletRequest request) throws Exception{
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		StringBuilder busiDesc = new StringBuilder();
		if(rm!=null){
			return rm;
		}else{
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务  
			TransactionStatus txStatus = tx.getTransaction(def);// 获得事务状态
			try{
				userMaterialService.updateUserContact(userContacts);
				rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
				busiDesc.append("修改联系人成功，修改联系人名称:").append(userContacts.getRealName());
				tx.commit(txStatus);
			}catch(Exception e){
				e.printStackTrace();
				rm = new ResponseMessage(ErrorCode.EXCEPTION);
				busiDesc.append("系统异常修改联系人失败");
				tx.rollback(txStatus);
			}
			rm.setBusinessDesc(busiDesc.toString());
			return rm;
		}
	}
	
	/*
	 * 查询客户经理
	 */
	@GET 
	@Path("/getCustomerManager")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	public ResponseMessage getCustomerManager(@QueryParam("userId")Integer userId,@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
			return rm;
		}else{
			SysManageUser sysm = userMaterialService.getCustomerManager(userId);
			rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
			rm.setData(sysm);
			return rm;
		}
	}
	/*
	 * 查询出所有的客户经理
	 */
	@GET 
	@Path("/getAllCustomerManager")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	public ResponseMessage getAllCustomerManager(@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
			return rm;
		}else{
			List<SysManageUser> list = userMaterialService.getAllCustomerManager();
			rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
			rm.setData(list);
			return rm;
		}
	}
	
	/*
	 * 根据用户id获取用户的详细信息
	 */
	@GET 
	@Path("/getUserByUserId")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	public ResponseMessage getUserByUserId(@QueryParam("userId")Integer userId,@Context HttpServletRequest request) throws Exception{
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
			return rm;
		}else{
			User user = userMaterialService.getUserByUserId(userId);
			rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
			rm.setData(user);
			return rm;
		}
	}
	
	/*
	 * 修改用户基本信息
	 */
	@POST 
	@Path("/updateBasicUser")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	@ControllerLog(description="修改用户基本信息")
	public ResponseMessage updateBasicUser(User user,@Context HttpServletRequest request)throws Exception{
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		StringBuilder busiDesc = new StringBuilder();
		if(rm!=null){
			return rm;
		}else{
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务  
			TransactionStatus txStatus = tx.getTransaction(def);// 获得事务状态
			try{
				userMaterialService.updateUser(user);
				rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
				busiDesc.append("修改用户基本信息成功，用户登录名：").append(user.getEmail());
				tx.commit(txStatus);
			}catch(Exception e){
				e.printStackTrace();
				rm = new ResponseMessage(ErrorCode.EXCEPTION);
				busiDesc.append("系统异常，修改用户信息失败");
				tx.rollback(txStatus);
			}
			rm.setBusinessDesc(busiDesc.toString());
			return rm;
		}
	}
	/*
	 * 修改用户密码
	 */
	@GET 
	@Path("/updateUserPassword")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	@ControllerLog(description="修改用户密码")
	public ResponseMessage updateUserPassword(@QueryParam("code")String code,@QueryParam("userId")Integer userId,@QueryParam("password")String password,@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		StringBuilder busiDesc = new StringBuilder();
		if(rm!=null){
			return rm;
		}else{
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务  
			TransactionStatus txStatus = tx.getTransaction(def);// 获得事务状态
			try {
				String ipAddr = CustomUtil.getIpAddr(request);
				String key = ipAddr+"phoneCode";
				String phoneCode = redisUtil.get(key);
				if(phoneCode==null || !phoneCode.equalsIgnoreCase(code)){
					rm = new ResponseMessage(ErrorCode.PHONECODE_EXPIRED);
				}else{
					userMaterialService.updateUserPassword(userId, password);
					rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
					busiDesc.append("修改密码成功");
					rm.setBusinessDesc(busiDesc.toString());
				}
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
	 * 修改用户手机号码
	 */
	@GET 
	@Path("/updateUserPhone")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	@ControllerLog(description="修改手机号码")
	public ResponseMessage updateUserPhone(@QueryParam("code")String code,@QueryParam("userId")Integer userId,@QueryParam("phone")String phone,@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		StringBuilder busiDesc = new StringBuilder();
		if(rm!=null){
			return rm;
		}else{
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务  
			TransactionStatus txStatus = tx.getTransaction(def);// 获得事务状态
			try {
				String ipAddr = CustomUtil.getIpAddr(request);
				String key = ipAddr+"phoneCode";
				String phoneCode = redisUtil.get(key);
				if(phoneCode==null || !phoneCode.equalsIgnoreCase(code)){
					rm = new ResponseMessage(ErrorCode.PHONECODE_EXPIRED);
				}else{
					userMaterialService.updateUserPhone(userId, phone);
					rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
					busiDesc.append("修改手机号码成功，新手机号码：").append(phone);
				}
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
	 * 修改用户QQ号码
	 */
	@GET 
	@Path("/updateUserQQNum")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	@ControllerLog(description="修改QQ号码")
	public ResponseMessage updateUserQQNum(@QueryParam("userId")Integer userId,@QueryParam("qqNum")String qqNum,@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		StringBuilder busiDesc = new StringBuilder();
		if(rm!=null){
			return rm;
		}else{
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务  
			TransactionStatus txStatus = tx.getTransaction(def);// 获得事务状态
			try {
				userMaterialService.updateUserQQNum(userId, qqNum);
				rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
				busiDesc.append("修改qq号码成功，新qq号码:").append(qqNum);
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
	 * 修改用户支付宝账号
	 */
	@GET 
	@Path("/updateUserzfbao")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	@ControllerLog(description="修改支付宝账号")
	public ResponseMessage updateUserzfbao(@QueryParam("code")String code,@QueryParam("userId")Integer userId,@QueryParam("zfbao")String zfbao,@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		StringBuilder busiDesc = new StringBuilder();
		if(rm!=null){
			return rm;
		}else{
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务  
			TransactionStatus txStatus = tx.getTransaction(def);// 获得事务状态
			try {
				String ipAddr = CustomUtil.getIpAddr(request);
				String key = ipAddr+"phoneCode";
				String phoneCode = redisUtil.get(key);
				if(phoneCode==null || !phoneCode.equalsIgnoreCase(code)){
					rm = new ResponseMessage(ErrorCode.PHONECODE_EXPIRED);
				}else{
					userMaterialService.updateUserzfbao(userId, zfbao);
					rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
					busiDesc.append("修改支付宝账号成功，新支付宝账号:").append(zfbao);
					rm.setBusinessDesc(busiDesc.toString());
				}
				tx.commit(txStatus);
			} catch (Exception e) {
				e.printStackTrace();
				rm = new ResponseMessage(ErrorCode.EXCEPTION);
				tx.rollback(txStatus);
			}
			return rm;
		}
	}
	/*
	 * 修改用户密码
	 */
	@GET 
	@Path("/updateUserPasswordAdmin")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	@ControllerLog(description="修改登陆密码")
	public ResponseMessage updateUserPasswordAdmin(@QueryParam("userId")Integer userId,@QueryParam("password")String password,@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		StringBuilder busiDesc = new StringBuilder();
		if(rm!=null){
			return rm;
		}else{
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务  
			TransactionStatus txStatus = tx.getTransaction(def);// 获得事务状态
			try {
				userMaterialService.updateUserPassword(userId, password);
				rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
				busiDesc.append("修改密码成功");
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
	 * 修改用户手机号码
	 */
	@GET 
	@Path("/updateUserPhoneNum")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	@ControllerLog()
	public ResponseMessage updateUserPhoneNum(@QueryParam("userId")Integer userId,@QueryParam("phoneNum")String phoneNum,@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		StringBuilder busiDesc = new StringBuilder();
		if(rm!=null){
			return rm;
		}else{
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务  
			TransactionStatus txStatus = tx.getTransaction(def);// 获得事务状态
			try {
				userMaterialService.updateUserPhone(userId, phoneNum);
				rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
				busiDesc.append("修改手机号码成功，新手机号码：").append(phoneNum);
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
	 * 给用户分配客户经理
	 */
	@GET 
	@Path("/allotManage")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	@ControllerLog(description="分配客户经理")
	public ResponseMessage allotManage(@QueryParam("userId")Integer userId,@QueryParam("manageId")int manageId,@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		StringBuilder busiDesc = new StringBuilder();
		if(rm!=null){
			return rm;
		}else{
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务  
			TransactionStatus txStatus = tx.getTransaction(def);// 获得事务状态
			try {
				User u = userService.getUserInfoById(userId);
				
				SysManageUser cm = systemService.getSysManageUserById(manageId);//通过管理员id获取其对象
				
				userMaterialService.allotManage(userId, manageId);
				rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
				
				String uname = "";//用户的名称
				String mname = "";//经理的名称
				if(u != null){
					if(u.getUserName() != null){
						uname = u.getUserName();
					}
				}
				if(cm != null){
					if(cm.getUserName() != null){
						mname = cm.getUserName();
					}
					busiDesc.append("分配客户经理；处理成功；");
					//busiDesc.append(uname+"被分配给 "+mname+"将客户经理");
					busiDesc.append("将客户经理").append(mname).append("分配给客户").append(uname);
				}else{
					busiDesc.append("分配客户经理；处理成功；");
					busiDesc.append(uname+"目前与客户经理处于解绑的状态");
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
	 * 添加用户
	 */
	@POST 
	@Path("/addOneUser")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	@ControllerLog(description="添加用户")
	public ResponseMessage allotManage(User user,@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		StringBuilder busiDesc = new StringBuilder();
		if(rm!=null){
			return rm;
		}else{
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务  
			TransactionStatus txStatus = tx.getTransaction(def);// 获得事务状态
			try {
				userService.addNewUser(user,1);
				rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
				busiDesc.append("新增用户，用户登录名：").append(user.getEmail());
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
	
}
