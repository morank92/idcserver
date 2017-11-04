package com.baoidc.idcserver.rest.service;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

import com.baoidc.idcserver.core.ResponseMessage;
import com.baoidc.idcserver.po.UserLog;
import com.baoidc.idcserver.service.IUserLogService;

@Component
@Path("/userOptLog")
public class UserOptLogRestService {
	
	@Resource
	private  IUserLogService logService;
	
	/*
	 * 查询日志
	 */
	@GET 
	@Path("/getUserOptLog")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public ResponseMessage getUserWorkOrderList(@QueryParam("userId") int userId,@QueryParam("adminId")int adminId,
			@QueryParam("startTime") String startTime,@QueryParam("endTime") String endTime,@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
			return rm;
		}else{
			rm = new ResponseMessage();
			List<UserLog> list = logService.queryUserOptLog(userId,adminId,startTime,endTime);
			rm.setCode("200");
			rm.setObj(list);
			return rm;
		}
	}
	
}
