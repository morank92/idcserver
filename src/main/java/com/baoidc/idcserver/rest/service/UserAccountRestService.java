package com.baoidc.idcserver.rest.service;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.baoidc.idcserver.acp.sdk.ACPDemoBase;
import com.baoidc.idcserver.acp.sdk.AcpService;
import com.baoidc.idcserver.acp.sdk.SDKConfig;
import com.baoidc.idcserver.alipay.config.AlipayConfig;
import com.baoidc.idcserver.alipay.util.AlipaySubmit;
import com.baoidc.idcserver.core.DateUtil;
import com.baoidc.idcserver.core.ErrorCode;
import com.baoidc.idcserver.core.ResponseMessage;
import com.baoidc.idcserver.core.aspect.ControllerLog;
import com.baoidc.idcserver.po.ChargeRecord;
import com.baoidc.idcserver.po.ConsumeRecord;
import com.baoidc.idcserver.po.EncashmentRecord;
import com.baoidc.idcserver.po.UserAccount;
import com.baoidc.idcserver.service.IUserAccountService;
import com.baoidc.idcserver.vo.query.ConsumeQueryVO;

@Component
@Path("/userAccount")
public class UserAccountRestService {
	
	@Resource
	private IUserAccountService userAccountService;
	
	@Autowired
	private DataSourceTransactionManager tx;
	@Autowired
	private DefaultTransactionDefinition def;
	
	@GET @Path("/getUserAccountInfo")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public ResponseMessage getUserAccountInfo(@QueryParam("userId") int userId,@Context HttpServletRequest request){
		ResponseMessage resMessage = (ResponseMessage)request.getAttribute("isLogin");
		if(resMessage != null){
			return resMessage;
		}else{
			try{
				UserAccount userAccount = userAccountService.getUserAccountInfo(userId);
				if(userAccount !=  null){
					resMessage = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
					resMessage.setData(userAccount);
				}else{
					resMessage = new ResponseMessage(ErrorCode.ERROR_CODE);
				}
			}catch(Exception e){
				e.printStackTrace();
				resMessage = new ResponseMessage(ErrorCode.EXCEPTION);
			}
		}
		return resMessage;
	}
	//获取单个用户的账户信息组装成datatable的数据
	@GET @Path("/getUserAccountInfo4Table")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public ResponseMessage getUserAccountInfo4Table(@QueryParam("userId") int userId,@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
			return rm;
		}else{
			try {
				List<UserAccount> list = new ArrayList<UserAccount>();
				UserAccount userAccount = userAccountService.getUserAccountInfo(userId);
				list.add(userAccount);
				rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
				rm.setData(list);
				return rm;
			} catch (Exception e) {
				e.printStackTrace();
				rm = new ResponseMessage(ErrorCode.EXCEPTION);
				return rm;
			}
		}
	}
	
	@POST @Path("/doCharge")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@ControllerLog()
	public ResponseMessage charge(ChargeRecord chargeRecord,@Context HttpServletResponse response,@Context HttpServletRequest request){
		ResponseMessage resMessage = (ResponseMessage)request.getAttribute("isLogin");
		StringBuilder busiDesc = new StringBuilder();
		if(resMessage  !=  null){
			return resMessage;
		}
		try{
			if(chargeRecord != null){
				userAccountService.addNewChargeRecord(chargeRecord);
			}
			if(chargeRecord.getRecordId() != 0){
				if(chargeRecord.getChargeType() == 0){ //跳转到银联支付页面
					//金额转换
					double txAmt = chargeRecord.getChargeAmt() * 100; //单位分
					DecimalFormat format = new DecimalFormat("#.#");
					String amtStr = format.format(txAmt);
					//amtStr = amtStr.substring(0, amtStr.indexOf(".") -1 );
					
					//跳转到支付页面,目前仅支持银联支付
					Map<String, String> requestData = new HashMap<String, String>();
					
					/***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
					requestData.put("version", ACPDemoBase.version);   			  //版本号，全渠道默认值
					requestData.put("encoding", ACPDemoBase.encoding_UTF8); 			  //字符集编码，可以使用UTF-8,GBK两种方式
					requestData.put("signMethod", "01");            			  //签名方法，只支持 01：RSA方式证书加密
					requestData.put("txnType", "01");               			  //交易类型 ，01：消费
					requestData.put("txnSubType", "01");            			  //交易子类型， 01：自助消费
					requestData.put("bizType", "000201");           			  //业务类型，B2C网关支付，手机wap支付
					requestData.put("channelType", "07");           			  //渠道类型，这个字段区分B2C网关支付和手机wap支付；07：PC,平板  08：手机
					
					/***商户接入参数***/
					requestData.put("merId", SDKConfig.getConfig().getMerId());    	          			  //商户号码，请改成自己申请的正式商户号或者open上注册得来的777测试商户号
					requestData.put("accessType", "0");             			  //接入类型，0：直连商户 
					requestData.put("orderId",chargeRecord.getChargeNo());             //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则		
					requestData.put("txnTime", ACPDemoBase.getCurrentTime());        //订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
					requestData.put("currencyCode", "156");         			  //交易币种（境内商户一般是156 人民币）		
					requestData.put("txnAmt", amtStr);             			      //交易金额，单位分，不要带小数点
					
					requestData.put("frontUrl", SDKConfig.getConfig().getFrontUrl());
					
					requestData.put("backUrl", ACPDemoBase.backUrl);
					
					/**请求参数设置完毕，以下对请求参数进行签名并生成html表单，将表单写入浏览器跳转打开银联页面**/
					Map<String, String> submitFromData = AcpService.sign(requestData,ACPDemoBase.encoding_UTF8);  //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
					
					String requestFrontUrl = SDKConfig.getConfig().getFrontRequestUrl();  //获取请求银联的前台地址：对应属性文件acp_sdk.properties文件中的acpsdk.frontTransUrl
					String html = AcpService.createAutoFormHtml(requestFrontUrl, submitFromData,ACPDemoBase.encoding_UTF8);   //生成自动跳转的Html表单
					
					//LogUtil.writeLog("打印请求HTML，此为请求报文，为联调排查问题的依据："+html);
					/*resp.getWriter().write(html);*/
					
					//respMessage.setFlag("Y");
					//respMessage.setObj(html);
					resMessage = new ResponseMessage(ErrorCode.CHARGE_SUCCESS);
					resMessage.setData(html);
					busiDesc.append("充值成功,本次充值金额").append(txAmt).append("元。");
				}else if(chargeRecord.getChargeType() == 1){ //支付宝支付
					
					
					
					 response.setHeader("Cache-Control", "no-cache");
		               response.setCharacterEncoding("utf-8");
		               response.setContentType("text/html;charset=utf-8");
					////////////////////////////////////请求参数//////////////////////////////////////
					
					//商户订单号，商户网站订单系统中唯一订单号，必填
					//String out_trade_no = new String(request.getParameter("WIDout_trade_no").getBytes("ISO-8859-1"),"UTF-8");
					
					//订单名称，必填
					//String subject = new String(request.getParameter("WIDsubject").getBytes("ISO-8859-1"),"UTF-8");
					
					//付款金额，必填
					//String total_fee = new String(request.getParameter("WIDtotal_fee").getBytes("ISO-8859-1"),"UTF-8");
					
					//商品描述，可空
					//String body = new String(request.getParameter("WIDbody").getBytes("ISO-8859-1"),"UTF-8");
					
					
					
					//////////////////////////////////////////////////////////////////////////////////
					
					//把请求参数打包成数组
					Map<String, String> sParaTemp = new HashMap<String, String>();
					sParaTemp.put("service", AlipayConfig.service);
					sParaTemp.put("partner", AlipayConfig.partner);
					sParaTemp.put("seller_id", AlipayConfig.seller_id);
					sParaTemp.put("_input_charset", AlipayConfig.input_charset);
					sParaTemp.put("payment_type", AlipayConfig.payment_type);
					sParaTemp.put("notify_url", AlipayConfig.notify_url);
					sParaTemp.put("return_url", AlipayConfig.return_url);
					sParaTemp.put("anti_phishing_key", AlipayConfig.anti_phishing_key);
					sParaTemp.put("exter_invoke_ip", AlipayConfig.exter_invoke_ip);
					
					try {
						//订单号
						//sParaTemp.put("out_trade_no", out_trade_no);
						sParaTemp.put("out_trade_no", new String(chargeRecord.getChargeNo().getBytes("ISO-8859-1"),"UTF-8"));
						//商品名称    网上自助充值
						//sParaTemp.put("subject", subject);
						String name = "Baoidc";
						String namenew = new String(name.getBytes("ISO-8859-1"),"utf-8");
						sParaTemp.put("subject",namenew);
						//金额
						//sParaTemp.put("total_fee", total_fee);
						sParaTemp.put("total_fee", new String((chargeRecord.getChargeAmt()+"").getBytes("ISO-8859-1"),"UTF-8"));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					//描述   可不填
					//sParaTemp.put("body", body);
					//其他业务参数根据在线开发文档，添加参数.文档地址:https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.O9yorI&treeId=62&articleId=103740&docType=1
					//如sParaTemp.put("参数名","参数值");
					
					//建立请求
					String sHtmlText = AlipaySubmit.buildRequest(sParaTemp,"post","确认");
					//out.println(sHtmlText);
					
					resMessage = new ResponseMessage(ErrorCode.CHARGE_SUCCESS);
					resMessage.setData(sHtmlText);
					busiDesc.append("充值成功,本次充值金额").append(chargeRecord.getChargeAmt()).append("元。");
				}
			} else {
				resMessage = new ResponseMessage(ErrorCode.CHARGE_FAIL);
				busiDesc.append("充值失败");
			}
		}catch(Exception e){
			e.printStackTrace();
			resMessage = new ResponseMessage(ErrorCode.EXCEPTION);
			busiDesc.append("充值失败");
		}
		resMessage.setBusinessDesc(busiDesc.toString());
		return resMessage;
	}
	
	@POST @Path("/payResult")
	public void payResult(@Context HttpServletRequest request){
		Map<String, String> params = getAllRequestParam(request);
		userAccountService.handlePayResult(params);
	} 
	
	/**
	 * 获取请求参数中所有的信息
	 * 
	 * @param request
	 * @return
	 */
	public static Map<String, String> getAllRequestParam(
			final HttpServletRequest request) {
		Map<String, String> res = new HashMap<String, String>();
		Enumeration<?> temp = request.getParameterNames();
		System.out.println(temp);
		if (null != temp) {
			while (temp.hasMoreElements()) {
				String en = (String) temp.nextElement();
				System.out.println(en);
				String value = request.getParameter(en);
				res.put(en, value);
				System.out.println(value);
				// 在报文上送时，如果字段的值为空，则不上送<下面的处理为在获取所有参数数据时，判断若值为空，则删除这个字段>
				if (res.get(en) == null || "".equals(res.get(en))) {
					res.remove(en);
				}
			}
		}
		return res;
	}
	
	
	/*
	 * 查询用户充值记录
	 */
	@GET
	@Path("/getChargeRecord")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public ResponseMessage getChargeRecord(@QueryParam("userId")Integer userId,
			@QueryParam("startTime") String startTime,
			@QueryParam("endTime") String endTime,
			@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage)request.getAttribute("isLogin");
		if(rm != null){
			return rm;
		}else{
			try{
				List<ChargeRecord> list = userAccountService.getChargeRecord(userId,startTime,endTime);
				rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
				rm.setData(list);
			}catch(Exception e){
				e.printStackTrace();
				rm = new ResponseMessage(ErrorCode.EXCEPTION);
			}
		}
		return rm;
	}
	
	/*
	 * 查询用户消费记录
	 */
	@GET
	@Path("/getConsumeRecord")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public ResponseMessage getConsumeRecord(@QueryParam("userId")Integer userId,
			@QueryParam("startTime") String startTime,
			@QueryParam("endTime") String endTime,
			@QueryParam("orderType") int orderType,
			@QueryParam("queryType") Integer queryType,
			@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage)request.getAttribute("isLogin");
		if(rm != null){
			return rm;
		}else{
			try{
				ConsumeQueryVO consumeQueryVO = new ConsumeQueryVO();
				consumeQueryVO.setUserId(userId);
				if(queryType != null){
					if(queryType == 0){ //0:查询单月消费记录
						String currentMonth = DateUtil.dateToStr(new Date(), "yyyy-MM");
						consumeQueryVO.setCurrentMonth(currentMonth);
					}else{
						consumeQueryVO.setCurrentMonth("");
					}
				}
				if(startTime == null || startTime == ""){
					//System.out.println(startTime);
					consumeQueryVO.setStartTime("");
				}else{
					consumeQueryVO.setStartTime(startTime);
				}
				if(endTime == null || endTime == ""){
					consumeQueryVO.setEndTime("");
				}else{
					consumeQueryVO.setEndTime(endTime);
				}
				if(orderType != -1){
					consumeQueryVO.setOrderType(orderType);
				}
				List<ConsumeRecord> list = userAccountService.getConsumeRecord(consumeQueryVO);
				rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
				rm.setData(list);
			}catch(Exception e){
				e.printStackTrace();
				rm = new ResponseMessage(ErrorCode.EXCEPTION);
			}
		}
		return rm;
	}
	
	/*
	 * 用户提现
	 */
	@POST
	@Path("/doEncashment")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@ControllerLog()
	public ResponseMessage doEncashment(@QueryParam("userId")Integer userId,
			@QueryParam("encashmentAmt")double encashmentAmt,
			@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage)request.getAttribute("isLogin");
		StringBuilder busiDesc = new StringBuilder();
		if(rm != null){
			return rm;
		}else{
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务  
			TransactionStatus txStatus = tx.getTransaction(def);// 获得事务状态 
			try{
				userAccountService.createEncashmentRecord(userId, encashmentAmt);
				rm = new ResponseMessage(ErrorCode.ENCASHMENT_SUCCESS);
				busiDesc.append("提现成功，本次提现金额").append(encashmentAmt).append("元");
				tx.commit(txStatus);
			}catch(Exception e){
				e.printStackTrace();
				rm = new ResponseMessage(ErrorCode.EXCEPTION);
				busiDesc.append("系统异常,提现失败");
				tx.rollback(txStatus);
			}
			rm.setBusinessDesc(busiDesc.toString());
			return rm;
		}
	}
	
	/*
	 * 根据用户ID查询用户提现记录
	 */
	@GET
	@Path("/getEncashmentRecordById")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public ResponseMessage getEncashmentRecordById(@QueryParam("userId")Integer userId,@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage)request.getAttribute("isLogin");
		if(rm != null){
			return rm;
		}else{
			try{
				List<EncashmentRecord> list = userAccountService.getEncashmentRecordById(userId);
				rm =  new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
				rm.setData(list);
			}catch(Exception e){
				e.printStackTrace();
				rm = new ResponseMessage(ErrorCode.EXCEPTION);
			}
		}
		return rm;
	}
	
	
}
