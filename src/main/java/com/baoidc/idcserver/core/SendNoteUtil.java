package com.baoidc.idcserver.core;

import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

public class SendNoteUtil {
	public static String sendNote(String phone,Map<String,String> map,int flag)throws Exception{
		TaobaoClient client = new DefaultTaobaoClient(SystemContant.serverUrl,SystemContant.appKey,SystemContant.appSecret);
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setSmsType("normal");
		req.setSmsFreeSignName(SystemContant.signName);   //签名
		req.setRecNum(phone);   //电话
		ObjectMapper om = new ObjectMapper();
		String asString = "";
		switch (flag) {
		case 1:
			//服务即将过期提醒
			req.setSmsTemplateCode("SMS_73800030");  //模板ID
			/*
			 * 您的${server}为：${ip}的${type}还有${day}天到期，请注意到期时间，及时续费，以免带来困扰。
			 */
			asString = om.writeValueAsString(map);
			req.setSmsParam(asString);
			break;
		case 2:
			//服务过期提醒
			/*
			 * 您的${server}为：${ip}的${type}已过期，我们将停止服务，欢迎再次使用。
			 */
			req.setSmsTemplateCode("SMS_73900020");  //模板ID
			asString = om.writeValueAsString(map);
			req.setSmsParam(asString);
			break;

		default:
			break;
		}
		AlibabaAliqinFcSmsNumSendResponse  rsp = client.execute(req);
		System.out.println(rsp.getBody());
		return rsp.getBody();
	}
}
