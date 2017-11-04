package com.baoidc.idcserver.rest.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

import com.baoidc.idcserver.core.ResponseMessage;
import com.baoidc.idcserver.po.Touclick;
import com.touclick.captcha.exception.TouclickException;
import com.touclick.captcha.model.Status;
import com.touclick.captcha.service.TouClick;

@Component
@Path("/verify")
public class TouclickVerifyRestService {
	
	private static final String PUBKEY = "43b285f0-3b86-4c3e-9333-46b367ff0dd5";//公钥(从点触官网获取)
    private static final String PRIKEY = "6b973b18-5781-4e85-9c6d-8ec5a22e4974";//私钥(从点触官网获取)
	
	@POST 
	@Path("/getToVerify")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public ResponseMessage getToVerify(Touclick touclickPo,@Context HttpServletRequest request){
		TouClick touclick = new TouClick(); 
	    
		ResponseMessage rm = new ResponseMessage();
		Status status;
		try {
			String checkAddress = touclickPo.getCheckAddress();
			String token = touclickPo.getToken();
			String sid = touclickPo.getSid();
			status = touclick.check(checkAddress,sid,token,PUBKEY,PRIKEY);
			System.out.println("checkAddress :"+checkAddress + ",token:" + token);
	        System.out.println("code :"+status.getCode() + ",message:" + status.getMessage());
	        if(status != null && status.getCode()==0){
	            //执行自己的程序逻辑
	        	rm.setCode("200");
	        }else{
	        	rm.setCode("500");
	        	rm.setMsg("校验失败，请重新校验");
	        }
	        return rm;
		}catch(TouclickException t){
			t.printStackTrace();
			rm.setCode("500");
			rm.setMsg("校验失败，请重新校验");
			return rm;
		} 
		catch (Exception e) {
			e.printStackTrace();
			rm.setCode("500");
			rm.setMsg("网络异常");
			return rm;
		}
        
	}
	
}
