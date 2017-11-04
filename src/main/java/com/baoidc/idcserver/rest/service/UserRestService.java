package com.baoidc.idcserver.rest.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.baoidc.idcserver.core.CustomUtil;
import com.baoidc.idcserver.core.ErrorCode;
import com.baoidc.idcserver.core.RedisUtil;
import com.baoidc.idcserver.core.ResponseMessage;
import com.baoidc.idcserver.core.SystemContant;
import com.baoidc.idcserver.core.aspect.ControllerLog;
import com.baoidc.idcserver.po.User;
import com.baoidc.idcserver.service.IUserService;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

@Component
@Path("/user")
public class UserRestService {
	
	@Autowired
	private RedisUtil redisUtil;
	
	@Resource
	private IUserService userService;
	
	@POST 
	@Path("/regist")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	public ResponseMessage createUser(User user,@QueryParam("userType") int userType) {
		ResponseMessage rm = new ResponseMessage();
		//判断该邮箱是否被注册过
		int flag = userService.checkEmail(user.getEmail());
		if(flag ==1){
			rm.setCode("101");
			rm.setMsg("邮箱已被注册");
			return rm;
		}
		userService.addNewUser(user,userType);
		return rm;	
	}
	
	//注册验证验证码和手机短信验证码
	@GET 
	@Path("/checkDoubleCode")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	public ResponseMessage checkDoubleCode(
			@QueryParam("phoneCode")String phoneCode,
			@Context HttpServletRequest request){
		ResponseMessage resMessage = null;
		String localAddr = CustomUtil.getIpAddr(request);
		//String codeCheck = redisUtil.get(localAddr+"code");
		String codePhone = redisUtil.get(localAddr+"phoneCode");
		/*ObjectMapper jackobj = new ObjectMapper();
		if(codeCheck ==null || !codeCheck.equalsIgnoreCase(codeCheck)){
			return jackobj.writeValueAsString("验证码错误");
		}else if(codePhone==null || !codePhone.equalsIgnoreCase(phoneCode)){
			return jackobj.writeValueAsString("手机验证码错误");
		}else{
			return jackobj.writeValueAsString("OK");
		}*/
		
		//System.err.println("用户输入验证码:" + checkCode + ",系统保存验证码:" + codeCheck);
		
		/*if(codeCheck == null){
			resMessage = new ResponseMessage(ErrorCode.CODECHECK_EXPIRED); //验证码过期
			return resMessage;
		}else if(!codeCheck.equalsIgnoreCase(checkCode)){
			resMessage = new ResponseMessage(ErrorCode.CODECHECK_WRONG); //验证码错误
			return resMessage;
		}*/
		if(codePhone == null){
			resMessage = new ResponseMessage(ErrorCode.PHONECODE_EXPIRED); //手机验证码过期
			return resMessage;
		}else if(!codePhone.equalsIgnoreCase(phoneCode)){
			resMessage = new ResponseMessage(ErrorCode.PHONECODE_WRONG); //手机验证码错误
			return resMessage;
		}
		resMessage = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS); //验证通过
		return resMessage;
	}
	
	@GET 
	@Path("/getUserInfoById")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	public ResponseMessage getUserInfoById(@QueryParam("userId") int userId,@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage)request.getAttribute("isLogin");
		if(rm != null){
			return rm;
		}else{
			try{
				User user = userService.getUserInfoById(userId);
				rm  = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
				rm.setData(user);
			}catch(Exception e){
				e.printStackTrace();
				rm = new ResponseMessage(ErrorCode.EXCEPTION);
			}
		}
		return rm;
	}
	
	@GET @Path("/allUser")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	public ResponseMessage getAllUser(@QueryParam("qqNum") String qqNum,@QueryParam("userId")int userId,@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
			return rm;
		}else{
			try {
				List<User> userList = userService.getAllUser(qqNum,userId);
				
				//验证是否为客户经理访问该路径
				String t = request.getHeader("TOKEN");
				String cusIdLogin = request.getHeader("USER-ID");//当前登录的用户id
				String role = redisUtil.get(t);//登录时放入redis中的角色id；
				if(role != null && role.equals("3")){//目前将客户经理角色id暂定于3
					ArrayList<User> nlist = new ArrayList<User>();
					for(int i=0;i<userList.size();i++){
						int cusId = userList.get(i).getCustomerManagerId();
						if((cusId+"").equals(cusIdLogin)){//当前对象为当前客户经理的
							nlist.add(userList.get(i));
						}
					}
					userList = nlist;//重新赋值
				}
				
				rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
				rm.setData(userList);
				return rm;
			} catch (Exception e) {
				e.printStackTrace();
				rm = new ResponseMessage(ErrorCode.EXCEPTION);
				return rm;
			}
		}
	}
	@GET @Path("/getAllUserForSelect")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	public ResponseMessage getAllUserForSelect(){
		ResponseMessage rm = new ResponseMessage();
		
		try {
			List<User> userList = userService.getAllUser("",0);
			rm.setCode("200");
			rm.setData(userList);
			return rm;
		} catch (Exception e) {
			rm.setCode("500");
			rm.setMsg("网络异常");
			e.printStackTrace();
			return rm;
		}
	}
	
	@POST @Path("/updateUser")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	public String updateUserInfo(User user) {
		userService.updateUserInfo(user);
		
		String retVal = "{\"status\":201,\"info\":\"A new podcast/resource has been created\"}";
		
		return retVal;
	}
	
	@GET @Path("/login")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	@ControllerLog(description="用户登录")
	public ResponseMessage login(@QueryParam("email") String email,
			@QueryParam("password") String password,@Context HttpServletRequest request) throws Exception{
		/*String localAddr = CustomUtil.getIpAddr(request);
		String key = localAddr+"code";
		String code = redisUtil.get(key);
		if(code == null){
			return new ResponseMessage(ErrorCode.CODECHECK_EXPIRED);//验证码过期
		}
		if(!code.equalsIgnoreCase(checkCode)){
			//throw new ServerException(ErrorCode.ERROR_CODE); //验证错误
			return new ResponseMessage(ErrorCode.ERROR_CODE);
		}*/
		try {
			return userService.doLogin(email, password,request);
		} catch (Exception e) {
			e.printStackTrace();
			ResponseMessage rm = new ResponseMessage();
			rm.setMsg("服务器异常");
			return rm;
		}
	}
	
	@GET 
	@Path("/logout")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	@ControllerLog(description="用户注销")
	public ResponseMessage logout(@QueryParam("userEmail")String userEmail,@Context HttpServletRequest request){
		ResponseMessage rm = new ResponseMessage();
		StringBuilder busiDesc = new StringBuilder();
		try {
			userService.logout(userEmail);
			rm.setCode("200");
			busiDesc.append("用户注销成功！");
			rm.setBusinessDesc(busiDesc.toString());
			return rm;
		} catch (Exception e) {
			e.printStackTrace();
			rm.setCode("500");
			rm.setMsg("服务器异常");
			busiDesc.append("服务器异常注销失败！");
			rm.setBusinessDesc(busiDesc.toString());
			return rm;
		}
	}
	
	/*
	 * 网页验证码
	 */
	/*@GET 
	@Path("/getCheckCode")
	public void getCheckCode(@Context HttpServletRequest request,@Context HttpServletResponse response)throws Exception{
		
		
		String code = SecurityCode.getSecurityCode(4, SecurityCodeLevel.Simple, false);
		String localAddr = CustomUtil.getIpAddr(request);
		String key = localAddr+"code";
		redisUtil.set(key, code, 120);
		BufferedImage image = SecurityImage.createImage(code);
		ServletOutputStream os = response.getOutputStream();
		ImageIO.write(image, "jpg", os);
	}*/
	/*
	 * 手机短信验证码
	 */
	@GET 
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Path("/getMobileCheckCode")
	public String getMobileCheckCode(@QueryParam("phone") String phone,@QueryParam("flag")Integer flag,@Context HttpServletRequest request)throws Exception{
		
//		String serverUrl = "http://gw.api.taobao.com/router/rest";
		String serverUrl = SystemContant.serverUrl;
//		String appKey = "23657109";    //账号上
		String appKey = SystemContant.appKey;    //账号上
//		String appSecret = "a5fb56c53f6996a595ee2aeee3a914be";  //账号上
		String appSecret = SystemContant.appSecret;  //账号上
		TaobaoClient client = new DefaultTaobaoClient(serverUrl, appKey, appSecret);
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		
		//动态数据
		Random random = new Random();
		String phoneCode = "";
		for (int i = 0; i < 6; i++) {
			int j = random.nextInt(10);
			phoneCode+=j;
		}
		String localAddr = CustomUtil.getIpAddr(request);
		String key = localAddr+"phoneCode";
		redisUtil.set(key, phoneCode, 60*5);
		System.out.println(phoneCode);
		
		req.setSmsType("normal");
//		req.setSmsFreeSignName("康康科技");   //签名
		req.setSmsFreeSignName(SystemContant.signName);   //签名
		req.setSmsParamString("{number:'"+phoneCode+"'}"); //验证码
		req.setRecNum(phone);   //电话
		/*
		 * flag
		 * 1:注册页面
		 * 2：修改  例如 密码 手机号 获取简单的验证码
		 */
		switch (flag) {
		case 1:
			req.setSmsTemplateCode("SMS_50155147");  //模板ID
			break;
		case 2:
			req.setSmsTemplateCode("SMS_59755083");  //模板ID
			break;

		default:
			break;
		}
		
		AlibabaAliqinFcSmsNumSendResponse  rsp = client.execute(req);
		System.out.println(rsp.getBody());
		return rsp.getBody();
		
		/* 成功后返回的json串
		 * {"alibaba_aliqin_fc_sms_num_send_response":
		 * 		{"result":
		 * 			{"err_code":"0","model":"106151303252^1108331772673","success":true},
		 * 		 "request_id":"ryhoslida8lt"}}
		 */
	}
	
}
