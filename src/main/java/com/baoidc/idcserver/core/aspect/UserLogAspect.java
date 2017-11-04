package com.baoidc.idcserver.core.aspect;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.baoidc.idcserver.core.CustomUtil;
import com.baoidc.idcserver.core.ResponseMessage;
import com.baoidc.idcserver.po.UserLog;
import com.baoidc.idcserver.service.IUserLogService;
import com.baoidc.idcserver.service.IUserService;



@Aspect
@Component
public class UserLogAspect {
	
	@Resource
	private IUserLogService userLogService;
	@Resource
	private IUserService userService;
	
	private static final Logger logger = Logger.getLogger(UserLogAspect.class);
	
	@Pointcut("@annotation(com.baoidc.idcserver.core.aspect.ControllerLog)")
	public void controllerAspect(){
	}
	
	@AfterReturning(pointcut = "controllerAspect()",returning="result")
	public void doBefore(JoinPoint joinPoint,Object result){
		List<String> noLogCode = new ArrayList<String>();
		noLogCode.add("404");
		noLogCode.add("222");
		noLogCode.add("109");
		try{
			Object[] args = joinPoint.getArgs();
			String operDesc = "";
			if(result instanceof ResponseMessage){
				ResponseMessage rm =  (ResponseMessage)result;
				if(rm.getCode() != null && noLogCode.contains(rm.getCode())){ //用户未登录不记录日志
					return;
				}
				operDesc = rm.getBusinessDesc();
			}
			
			HttpServletRequest request = (HttpServletRequest) args[args.length-1];
			String userIdStr = request.getHeader("USER-ID");
			String logType = request.getHeader("LOG-TYPE");
			int userId;
			if(userIdStr==null){
				if("用户登录".equals(operDesc) || "管理员登录系统".equals(operDesc)){
					userId = (Integer) request.getAttribute("USER-ID");
				}else{
					return;
				}
			}else{
				userId = Integer.parseInt(request.getHeader("USER-ID"));
			}
			
			String ip = CustomUtil.getIpAddr(request);
			UserLog userLog = new UserLog();
			String logUser = request.getHeader("LOG-USER");   //user    or    admin
			if(logUser==null){
				logUser = "user";
			}
			userLog.setLogType(LogTypeTransform.transform(logType));
			userLog.setMethod(joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()");
			userLog.setRequestIp(ip);
			userLog.setOperation(operDesc);
			int adminId;
			if(logUser.equals("user")){
				adminId = -1;
				logUser = "客户";
			}else{
				adminId = userId;
				userId = -1;
				logUser = "管理员";
			}
			userLog.setLogUser(logUser);
			userLog.setUserId(userId);
			userLog.setAdminId(adminId);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			userLog.setDoTime(sdf.format(new Date()));
			userLogService.addUserLog(userLog);
			logger.info("=====>记录操作日志成功,日志信息:" + userLog.toString());
		}catch(Exception e){
			e.printStackTrace();
			logger.error("====>记录日志信息异常,异常信息:" + e.getMessage());
		}
	}
	
	public static String getControllerDescription(JoinPoint joinPoint) throws Exception{
		String targetName = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		Object args[] = joinPoint.getArgs();
		Class targetClass = Class.forName(targetName);
		Method[] methods = targetClass.getMethods();
		String description = "";
		for(Method method : methods){
			if(method.getName().equals(methodName)){
				Class[] clazzs = method.getParameterTypes();
				if(clazzs.length == args.length){
					description = method.getAnnotation(ControllerLog.class).description();
					break;
				}
			}
		}
		
		return description;
	}
	
	/*public static Map<String, Object> getControllerParam(JoinPoint joinPoint) throws Exception{
		String targetName = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		Object args[] = joinPoint.getArgs();
		Class targetClass = Class.forName(targetName);
		Method[] methods = targetClass.getMethods();
		Map<String, Object> paramMap = new HashMap<String,Object>();
		for(Method method : methods){
			if(method.getName().equals(methodName)){
				Class[] clazzs = method.getParameterTypes();
				if(clazzs.length == args.length){
					String description = method.getAnnotation(ControllerLog.class).description();
					paramMap.put("description", description);
					String paramIndex = method.getAnnotation(ControllerLog.class).paramIndex();
					paramMap.put("paramIndex", paramIndex);
					Class[] paramType = method.getAnnotation(ControllerLog.class).paramType();
					paramMap.put("paramType", paramType);
					String needValue = method.getAnnotation(ControllerLog.class).needValue();
					paramMap.put("needValue", needValue);
					break;
				}
			}
		}
		
		return paramMap;
	}*/

}
