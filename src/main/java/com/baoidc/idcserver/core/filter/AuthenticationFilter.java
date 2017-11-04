package com.baoidc.idcserver.core.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.baoidc.idcserver.core.ErrorCode;
import com.baoidc.idcserver.core.IdcServerUtils;
import com.baoidc.idcserver.core.RedisUtil;
import com.baoidc.idcserver.core.ResponseMessage;
public class AuthenticationFilter implements Filter {
	
	
	public void destroy() {
		
	}

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)res;
		
		ServletContext sc = request.getSession().getServletContext();
		XmlWebApplicationContext cxt = (XmlWebApplicationContext)WebApplicationContextUtils.getWebApplicationContext(sc);
		RedisUtil redisUtil = (RedisUtil) cxt.getBean("redisUtil");
		
		String requestUri = request.getHeader("Request-Uri");
		String loginName = request.getHeader("Login-Name");
		String token = request.getHeader("TOKEN");
		//无需验证的请求头
		List<String> notAuthList = new ArrayList<String>();
		notAuthList.add("/user/login");
		notAuthList.add("/user/checkDoubleCode");
		notAuthList.add("/user/regist");
		notAuthList.add("/user/getMobileCheckCode");
		notAuthList.add("/system/manageLogin");
		notAuthList.add("/verify/getToVerify");
		
		//这里还需要一些角色的对应访问资源
		//....
		if(loginName == null && notAuthList.contains(requestUri)){
			//判断是否是请求验证码  注册  登陆 等 无需token的路径
			chain.doFilter(request, response);
		}else{ //其他操作进行token验证
			if(loginName==null){
				//判断用户没有登陆  或者  没有token 或者token过期 跳转到登陆页面
				ResponseMessage rm = new ResponseMessage(ErrorCode.NOT_LOGIN);
				request.setAttribute("isLogin", rm);
				chain.doFilter(request, response);
			}else if(redisUtil.get(loginName)==null){
				ResponseMessage rm = new ResponseMessage(ErrorCode.NOTLOGIN);
				request.setAttribute("isLogin", rm);
				chain.doFilter(request, response);
			}else{
				String tokenCode = redisUtil.get(loginName);
				//判断用户带过来的token是否和系统存储的一致
				if(!tokenCode.equals(token)){
					ResponseMessage rm = new ResponseMessage(ErrorCode.GETOUT);
					request.setAttribute("isLogin", rm);
					chain.doFilter(request, response);
				}
				String decryTokenCode = IdcServerUtils.decryptPassword(tokenCode);
				System.out.println(decryTokenCode);
				String[] userInfo = decryTokenCode.split("_");
				int userId = Integer.parseInt(userInfo[2]);
				String logType = userInfo[3];
				if(logType != null && StringUtils.isNotBlank(logType) && logType.equalsIgnoreCase("user")){
					String userIdParam = request.getParameter("userId");
					if(userIdParam != null && StringUtils.isNotBlank(userIdParam)){
						int userIdParamInt = Integer.parseInt(userIdParam);
						if(userIdParamInt != userId){
							ResponseMessage rm = new ResponseMessage(ErrorCode.NO_AUTH);//判断有无权限
							request.setAttribute("isLogin", rm);
							chain.doFilter(request, response);
						}else if(redisUtil.get(tokenCode) != null && redisUtil.get(tokenCode).equals("3")){// 判断是否为客户经理且对当前访问路径资源有无权限
							//这里还需要角色对应的访问资源权限的验证。。。
							System.out.println("当前管理员为客户经理、roleID暂定为3");
							//更新用户的token时间
							//权限控制
							redisUtil.set(tokenCode, null, 1800);//对应的角色，
							redisUtil.set(loginName, null, 1800);
							chain.doFilter(request, response);
							
						}else{
							//更新用户的token时间
							//权限控制
							redisUtil.set(loginName, null, 1800);
							chain.doFilter(request, response);
						}
					}else{
						//更新用户的token时间
						//权限控制
						redisUtil.set(loginName, null, 1800);
						chain.doFilter(request, response);
					}
				}else{
					//更新用户的token时间
					//权限控制
					redisUtil.set(loginName, null, 1800);
					chain.doFilter(request, response);
				}
			}
		}
		
	}

	public void init(FilterConfig config){
	}


}
