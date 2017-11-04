package com.baoidc.idcserver.rest.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.baoidc.idcserver.core.DateTimeStr;
import com.baoidc.idcserver.core.DateUtil;
import com.baoidc.idcserver.core.ErrorCode;
import com.baoidc.idcserver.core.ResponseMessage;
import com.baoidc.idcserver.po.ReportAttack;
import com.baoidc.idcserver.po.ReportConnection;
import com.baoidc.idcserver.po.ReportFlow;
import com.baoidc.idcserver.po.Servers;
import com.baoidc.idcserver.service.IDeviceService;
import com.baoidc.idcserver.service.IReportService;
import com.baoidc.idcserver.vo.query.EchartsQueryVO;

@Component
@Path("/report")
public class DataReportRestService { // controller

	// private static final String HOST = "http://localhost:8080/idcserver";

	@Autowired
	private IDeviceService deviceService;
	
	@Autowired
	private IReportService reportService;

	@GET
	@Path("/getInstanceIp/{userId}")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	@Transactional
	public ResponseMessage getInstanceIp(@PathParam("userId") int userId) {
		ResponseMessage res = new ResponseMessage();
		List<Servers> servers = reportService.getInstanceIpByuserId(userId);
		res.setObj(servers);
		return res;
	}
	//获取流量对比的数据
	@POST
	@Path("/getFlowData4Charts")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Transactional
	public ResponseMessage getFlowData4Charts(EchartsQueryVO echartsQueryVo
			,@Context HttpServletRequest request) {
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
		  return rm;
		}else{
			//reportService.getFlowData4Charts();
			rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
			
			List<ReportFlow> reportList = new ArrayList<ReportFlow>();
			String startGenerate;
			String endGenerate;
			String tableType;
			//判断时间差，并得出正确的String类型的时间差
			HashMap<String, String> map = DateUtil.isDeafultTime(echartsQueryVo.getStime(), echartsQueryVo.getEtime());
			if(map.get(DateTimeStr.TIME_ERROR) == "timeInputError"){//输入的结束时间小于开始时间
				rm.setFlag("0");
			}
			if(map.get(DateTimeStr.MESSAGE_TIME)=="1"){//从一张表查询
				startGenerate = map.get(DateTimeStr.START_TIME);
				endGenerate = map.get(DateTimeStr.END_TIME);
				tableType = map.get(DateTimeStr.TABLE_TYPE);
				
				//完整查询对象
				echartsQueryVo.setStartGenerate(startGenerate);
				echartsQueryVo.setEndGenerate(endGenerate);
				echartsQueryVo.setTableType(tableType);
				reportList = reportService.getFlowReport(echartsQueryVo);
			}
			if(map.get(DateTimeStr.MESSAGE_TIME)=="2"){//在两张天表查询
				String sd1=map.get(DateTimeStr.START_TIME);
				String ed1=map.get(DateTimeStr.START_TIME_E);
				String tableType1 = map.get(DateTimeStr.TABLE_TYPE2);//第一张表
				
				//完整查询对象
				echartsQueryVo.setStartGenerate(sd1);
				echartsQueryVo.setEndGenerate(ed1);
				echartsQueryVo.setTableType(tableType1);
				
				reportList.addAll(reportService.getFlowReport(echartsQueryVo));
				//第二张表数据
				String sd2=map.get(DateTimeStr.END_TIME_S);
				String ed2=map.get(DateTimeStr.END_TIME);
				String tableType2 = map.get(DateTimeStr.TABLE_TYPE2_2);//第二张表
				
				//完整查询对象
				echartsQueryVo.setStartGenerate(sd2);
				echartsQueryVo.setEndGenerate(ed2);
				echartsQueryVo.setTableType(tableType2);
				
				reportList.addAll(reportService.getFlowReport(echartsQueryVo));
			}
			if(reportList.size() != 0 && reportList != null){
				rm.setObj(reportList);
				rm.setFlag(map.get(DateTimeStr.FLAGX));//其中message有：1或2 或m（1为一天的查询时间，2为大于一天时间，m为月表查询）
			}else{//未获取到数据
				rm.setFlag("3");
				
			}
			return rm;
		}
		
	}
	
	//连接数
	@POST
	@Path("getConnectsReport")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Transactional
	public ResponseMessage getConnectsReport(EchartsQueryVO echartsQueryVo,@Context HttpServletRequest request)throws Exception{
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
		  return rm;
		}else{
			//reportService.getFlowData4Charts();
			rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
			List<ReportConnection> conectsList = new ArrayList<ReportConnection>();
			String startGenerate;
			String endGenerate;
			String tableType;
			//判断时间差，并得出正确的String类型的时间差
			HashMap<String, String> map = DateUtil.isDeafultTime(echartsQueryVo.getStime(), echartsQueryVo.getEtime());
			if(map.get(DateTimeStr.TIME_ERROR) == "timeInputError"){//输入的结束时间小于开始时间
				rm.setFlag("0");
			}
			if(map.get(DateTimeStr.MESSAGE_TIME)=="1"){//从一张表查询
				startGenerate = map.get(DateTimeStr.START_TIME);
				endGenerate = map.get(DateTimeStr.END_TIME);
				tableType = map.get(DateTimeStr.TABLE_TYPE);
				
				//完整查询对象
				echartsQueryVo.setStartGenerate(startGenerate);
				echartsQueryVo.setEndGenerate(endGenerate);
				echartsQueryVo.setTableType(tableType);
				conectsList = reportService.getConnectsReport(echartsQueryVo);
			}
			if(map.get(DateTimeStr.MESSAGE_TIME)=="2"){//在两张天表查询
				String sd1=map.get(DateTimeStr.START_TIME);
				String ed1=map.get(DateTimeStr.START_TIME_E);
				String tableType1 = map.get(DateTimeStr.TABLE_TYPE2);//第一张表
				
				//完整查询对象
				echartsQueryVo.setStartGenerate(sd1);
				echartsQueryVo.setEndGenerate(ed1);
				echartsQueryVo.setTableType(tableType1);
				conectsList.addAll(reportService.getConnectsReport(echartsQueryVo));
				
				//第二张表数据
				String sd2=map.get(DateTimeStr.END_TIME_S);
				String ed2=map.get(DateTimeStr.END_TIME);
				String tableType2 = map.get(DateTimeStr.TABLE_TYPE2_2);//第二张表
				
				//完整查询对象
				echartsQueryVo.setStartGenerate(sd2);
				echartsQueryVo.setEndGenerate(ed2);
				echartsQueryVo.setTableType(tableType2);
				conectsList.addAll(reportService.getConnectsReport(echartsQueryVo));
			}
			if(conectsList.size() != 0 && conectsList != null){
				rm.setObj(conectsList);
				rm.setFlag(map.get(DateTimeStr.FLAGX));//其中message有：1或2 （1为默认，2为自定时间）
			}else{//未获取到数据
				rm.setFlag("3");
			}
			return rm;
		}
	}
	
	//攻击topn
	@POST
	@Path("getTopnReport")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Transactional
	public ResponseMessage getTopnReport(EchartsQueryVO echartsQueryVo,@Context HttpServletRequest request)throws Exception{
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
		  return rm;
		}else{
			//reportService.getFlowData4Charts();
			rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
			List<ReportAttack> topnList = new ArrayList<ReportAttack>();
			String startGenerate;
			String endGenerate;
			String tableType;
			//判断时间差，并得出正确的String类型的时间差
			HashMap<String, String> map = DateUtil.isDeafultTime(echartsQueryVo.getStime(), echartsQueryVo.getEtime());
			if(map.get(DateTimeStr.TIME_ERROR) == "timeInputError"){//输入的结束时间小于开始时间
				rm.setFlag("0");
			}
			if(map.get(DateTimeStr.MESSAGE_TIME)=="1"){//从一张表查询
				startGenerate = map.get(DateTimeStr.START_TIME);
				endGenerate = map.get(DateTimeStr.END_TIME);
				tableType = map.get(DateTimeStr.TABLE_TYPE);
				
				//完整查询对象
				echartsQueryVo.setStartGenerate(startGenerate);
				echartsQueryVo.setEndGenerate(endGenerate);
				echartsQueryVo.setTableType(tableType);
				topnList = reportService.getTopnReport(echartsQueryVo);
			}
			if(map.get(DateTimeStr.MESSAGE_TIME)=="2"){//在两张天表查询
				String sd1=map.get(DateTimeStr.START_TIME);
				String ed1=map.get(DateTimeStr.START_TIME_E);
				String tableType1 = map.get(DateTimeStr.TABLE_TYPE2);//第一张表
				
				//完整查询对象
				echartsQueryVo.setStartGenerate(sd1);
				echartsQueryVo.setEndGenerate(ed1);
				echartsQueryVo.setTableType(tableType1);
				topnList.addAll(reportService.getTopnReport(echartsQueryVo));
				
				//第二张表数据
				String sd2=map.get(DateTimeStr.END_TIME_S);
				String ed2=map.get(DateTimeStr.END_TIME);
				String tableType2 = map.get(DateTimeStr.TABLE_TYPE2_2);//第二张表
				
				//完整查询对象
				echartsQueryVo.setStartGenerate(sd2);
				echartsQueryVo.setEndGenerate(ed2);
				echartsQueryVo.setTableType(tableType2);
				topnList.addAll(reportService.getTopnReport(echartsQueryVo));
			}
			if(topnList.size() != 0 && topnList != null){
				rm.setObj(topnList);
				rm.setFlag(map.get(DateTimeStr.FLAGX));//其中message有：1或2 （1为默认，2为自定时间）
			}else{//未获取到数据
				rm.setFlag("3");
			}
			return rm;
		}
				
	}
	//攻击分布
	@POST
	@Path("getAttDisReport")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Transactional
	public ResponseMessage getAttDisReport(EchartsQueryVO echartsQueryVo,@Context HttpServletRequest request)throws Exception{
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
		  return rm;
		}else{
			//reportService.getFlowData4Charts();
			rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
			List<ReportAttack> disList = new ArrayList<ReportAttack>();
			String startGenerate;
			String endGenerate;
			String tableType;
			//判断时间差，并得出正确的String类型的时间差
			HashMap<String, String> map = DateUtil.isDeafultTime(echartsQueryVo.getStime(), echartsQueryVo.getEtime());
			if(map.get(DateTimeStr.TIME_ERROR) == "timeInputError"){//输入的结束时间小于开始时间
				rm.setFlag("0");
			}
			if(map.get(DateTimeStr.MESSAGE_TIME)=="1"){//从一张表查询
				startGenerate = map.get(DateTimeStr.START_TIME);
				endGenerate = map.get(DateTimeStr.END_TIME);
				tableType = map.get(DateTimeStr.TABLE_TYPE);
				
				//完整查询对象
				echartsQueryVo.setStartGenerate(startGenerate);
				echartsQueryVo.setEndGenerate(endGenerate);
				echartsQueryVo.setTableType(tableType);
				
				disList = reportService.getAttDisReport(echartsQueryVo);
			}
			if(map.get(DateTimeStr.MESSAGE_TIME)=="2"){//在两张天表查询
				String sd1=map.get(DateTimeStr.START_TIME);
				String ed1=map.get(DateTimeStr.START_TIME_E);
				String tableType1 = map.get(DateTimeStr.TABLE_TYPE2);//第一张表
				
				//完整查询对象
				echartsQueryVo.setStartGenerate(sd1);
				echartsQueryVo.setEndGenerate(ed1);
				echartsQueryVo.setTableType(tableType1);
				disList.addAll(reportService.getAttDisReport(echartsQueryVo));
				
				//第二张表数据
				String sd2=map.get(DateTimeStr.END_TIME_S);
				String ed2=map.get(DateTimeStr.END_TIME);
				String tableType2 = map.get(DateTimeStr.TABLE_TYPE2_2);//第二张表
				
				//完整查询对象
				echartsQueryVo.setStartGenerate(sd2);
				echartsQueryVo.setEndGenerate(ed2);
				echartsQueryVo.setTableType(tableType2);
				disList.addAll(reportService.getAttDisReport(echartsQueryVo));
			}
			if(disList.size() != 0 && disList != null){
				rm.setObj(disList);
				rm.setFlag(map.get(DateTimeStr.FLAGX));//其中message有：1或2 （1为默认，2为自定时间）
			}else{//未获取到数据
				rm.setFlag("3");
			}
			
			return rm;
		}
			
	}
	
	
	
	
	
	
	

}
