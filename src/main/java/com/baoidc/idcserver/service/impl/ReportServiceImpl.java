package com.baoidc.idcserver.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baoidc.idcserver.core.DataConvertor;
import com.baoidc.idcserver.core.RedisUtil;
import com.baoidc.idcserver.dao.AssetManageDAO;
import com.baoidc.idcserver.dao.IReportDAO;
import com.baoidc.idcserver.po.ReportAttack;
import com.baoidc.idcserver.po.ReportConnection;
import com.baoidc.idcserver.po.ReportFlow;
import com.baoidc.idcserver.po.Servers;
import com.baoidc.idcserver.service.IReportService;
import com.baoidc.idcserver.vo.query.EchartsQueryVO;
@Service

public class ReportServiceImpl implements IReportService { //controller
	
	@Autowired
	private IReportDAO reportDao;
	
	@Autowired
	private  AssetManageDAO assetManageDao;
	
	@Autowired
	private RedisUtil redisUtil;
	
	

	public List<Servers> getInstanceIpByuserId(int userId) {
		
		//先查询出该用户的所有设备，再查询出设备上的ip
		List<Servers> servers = assetManageDao.getServersByuserId(userId);
		
		return servers;
	}
	
	//修正前端传过来的协议，类型，攻击字段
		public String getProUnit(String proUpper,String unitUpper){
			String pro = proUpper.toLowerCase().replace(" ","");
			String unit = unitUpper.toLowerCase();
			ArrayList<String> proList = new ArrayList<String>();
			String[] proStr = new String[]{"SYN","SYN-ACK","ACK","FIN/RST"};
			for(int i=0;i<proStr.length;i++){
				proList.add(proStr[i]);
			}
			if(proList.contains(proUpper)){//这些协议只有pps单位
				unit = "pps";
			}
			
			if(pro.equals("dnsquery")){
				pro = "dnsq";
			}
			if(pro.equals("dnsreply")){
				pro = "dnsr";
			}
			if(proUpper.equals("FIN/RST")){
				pro = "finrst";
			}
			if(proUpper.equals("SYN-ACK")){
				pro = "synack";
			}
			if(pro.equals("tcpfragment")){//TCP FRAGMENT
				pro = "tcpfrag";
			}
			if(pro.equals("udpfragment")){//UDP FRAGMENT
				pro = "udpfrag";
			}
			if(pro.equals("全部")){//全部协议类型total
				pro = "total";
			}
			if(unit.equals("kbit/s")){
				unit = "kbps";
			}
			String proUnit = pro+"_"+unit;
			return proUnit;
		}

		//流量对比
		public List<ReportFlow> getFlowReport(EchartsQueryVO echartsQueryVo) {
			//对单位和协议类型字段做改正,转小写去空格
			String tableType = echartsQueryVo.getTableType();
			String proUpper = echartsQueryVo.getProtocol();
			String unitUpper = echartsQueryVo.getUnit();
			
			String tableName = "t_report_flow"+tableType;
			String pro = proUpper.toLowerCase().replace(" ","");
			String unit = unitUpper.toLowerCase();
			ArrayList<String> proList = new ArrayList<String>();
			//一些特殊的单位
			String[] proStr = new String[]{"SYN","SYN-ACK","ACK","FIN/RST"};
			for(int i=0;i<proStr.length;i++){
				proList.add(proStr[i]);
			}
			if(proList.contains(proUpper)){//这些协议只有pps单位
				unit = "pps";
			}
			
			if(pro.equals("dnsquery")){
				pro = "dnsq";
			}
			if(pro.equals("dnsreply")){
				pro = "dnsr";
			}
			if(proUpper.equals("FIN/RST")){
				pro = "finrst";
			}
			if(proUpper.equals("SYN-ACK")){
				pro = "synack";
			}
			if(pro.equals("tcpfragment")){//TCP FRAGMENT
				pro = "tcpfrag";
			}
			if(pro.equals("udpfragment")){//UDP FRAGMENT
				pro = "udpfrag";
			}
			if(pro.equals("全部")){//全部协议类型total
				pro = "total";
			}
			if(unit.equals("kbit/s")){
				unit = "kbps";
			}
			//构建入流量攻击流量出流量表名 字段
			String inputFlow = pro+"in"+unit;
			String attFlow = pro+"attack"+unit;
			String outFlow = pro+"out"+unit;
			echartsQueryVo.setInputFlow(inputFlow);
			echartsQueryVo.setAttFlow(attFlow);
			echartsQueryVo.setOutFlow(outFlow);
			
			echartsQueryVo.setTableName(tableName);
			
			
			List<ReportFlow> flist = reportDao.getFlowReport(echartsQueryVo);
			if(flist != null && flist.size() !=0 ){
				//return DataConvertor.getExactForReportFlowList(tableType,echartsQueryVo.getStartGenerate(),flist);
				return DataConvertor.getExactData4ReportFlowList(echartsQueryVo,flist);
			}else{
				return flist;
			}
			
		}

		//连接数
		public List<ReportConnection> getConnectsReport(EchartsQueryVO echartsQueryVo) {
			String tableType = echartsQueryVo.getTableType();
			String type = echartsQueryVo.getConnType();
			String tableName = "t_report_connection"+tableType;
			String unitType = null;
			if(type.equals("新建")){
				unitType = "increase_conn";
			}
			if(type.equals("并发")){
				unitType = "concur_conn";
			}
			echartsQueryVo.setTableName(tableName);
			echartsQueryVo.setUnitType(unitType);
			
			List<ReportConnection> flist = reportDao.getConnectsReport(echartsQueryVo);
			if(flist != null && flist.size() !=0 ){
				//return DataConvertor.getExactForConnList(echartsQueryVo.getTableType(),echartsQueryVo.getStartGenerate(),flist);
				return DataConvertor.getExactData4ConnList(echartsQueryVo,flist);
			}else{
				return flist;
			}
		}


		//攻击topn
		public List<ReportAttack> getTopnReport(EchartsQueryVO echartsQueryVo) {
			String tableType = echartsQueryVo.getTableType();
			String tableName = "t_report_attack"+tableType;
			String type = echartsQueryVo.getTopnUnit();//排名类型：单位
			String dataTypeUp = echartsQueryVo.getDataType();//数据类型
			String resultType;
			
			if(type.equals("kbit/s")){
				type = "kbps";
			}
			String dataType = "attackpv";
			/*if(dataTypeUp.equals("全部")){
				dataType = "attackpv";
			}*/
			if(dataTypeUp.equals("均值")){
				dataType = "attackmv";
			}
			resultType = dataType + type;
			if(resultType.equals("attackmvkbps")){
				resultType = "attackmvkpbs";
			}
			
			echartsQueryVo.setDataType(dataType);//重新赋值，作为数据库中查询sql的判断
			echartsQueryVo.setTableName(tableName);
			echartsQueryVo.setTopnResultType(resultType);
			
			return reportDao.getTopnReport(echartsQueryVo);
		}

		//攻击分布
		public List<ReportAttack> getAttDisReport(EchartsQueryVO echartsQueryVo) {
			
			String tableType = echartsQueryVo.getTableType();
			String tableName = "t_report_attack"+tableType;
			
			echartsQueryVo.setTableName(tableName);
			return reportDao.getAttDisReport(echartsQueryVo);
		}

		//攻击详情
		/*public List<ReportAttack> getAttDetails(EchartsQueryVO echartsQueryVo) {
			String tableType = echartsQueryVo.getTableType();
			String attType = null;
			String tableName = "t_report_attack_domain"+tableType;
			if(!attTypeUp.equals("全部")){
				//attType = attTypeUp.toLowerCase().replace(" ","");//攻击类型的修正,转小写，去空格
				attType = DataConvertor.myType2numType(attTypeUp);//转为数据库存储的攻击类型，
			}
			
			return reportDao.getAttDetails(echartsQueryVo);
		}*/
		
		//攻击趋势
		/*public List<ReportFlow> getAttReport(EchartsQueryVO echartsQueryVo) {
			
			
			String attType = attTypeUp.toLowerCase().replace(" ","");//攻击类型的修正,转小写，去空格
			String attFlow;
			String tableName = "t_report_flow_domain"+tableType;
			if(attType.equals("dnsquery")){
				attType = "dnsq";
			}
			if(attType.equals("dnsreply")){
				attType = "dnsr";
			}
			if(attType.equals("tcpfragment")){//TCP FRAGMENT
				attType = "tcpfrag";
			}
			if(attType.equals("udpfragment")){//UDP FRAGMENT
				attType = "udpfrag";
			}
			if(attTypeUp.equals("FIN/RST")){
				attType = "finrst";
			}
			if(attTypeUp.equals("SYN-ACK")){
				attType = "synack";
			}
			if(attType.equals("全部")){
				attType = "total";
			}
			attFlow = attType+"attackpps";
			
			List<ReportFlow> flist = reportDao.getAttReport(tableName,fenceip,attFlow,startGenerate,endGenerate);
			if(flist != null && flist.size() !=0 ){
				return DataConvertor.getExactForReportFlowList(tableType,startGenerate,flist);
			}else{
				return flist;
			}
			
		}*/
		
		//总览
		/*public HashMap<String, Object> getAllReport(String tableType, String fenceip,
				String startGenerate, String endGenerate) {
			HashMap<String, Object> reMap = new HashMap<String, Object>();
			//各个所需的表名
			String tableName;
			String inputFlow = "totalinpps";
			String attFlow = "totalattackpps";
			String outFlow = "totaloutpps";
			tableName = "t_report_flow_domain"+tableType;
			//流量对比
			List<ReportFlow> flowRe = null;
			List<ReportFlow> flist = reportDao.getFlowReport(tableName,fenceip, inputFlow, attFlow, outFlow, startGenerate, endGenerate);
			if(flist.size()!=0 && flist != null){
				flowRe = DataConvertor.getExactForReportFlowList(tableType,startGenerate,flist);
			}else{
				flowRe = flist;
			}
			
			//攻击趋势字段为null即为默认查询
			List<ReportFlow> attRe = null;
			List<ReportFlow> atlist = reportDao.getAttReport(tableName,fenceip, attFlow, startGenerate, endGenerate);
			if(atlist.size()!=0 && atlist !=null){
				attRe = DataConvertor.getExactForReportFlowList(tableType,startGenerate,atlist);
			}else{
				attRe = atlist;
			}
			
			
			//攻击top;
			String type = "attackpvpps";
			String dataType = "attackpv";//默认查询峰值
			tableName = "t_report_attack_domain"+tableType;
			List<ReportAttack> topnRe = reportDao.getTopnReport(tableName,fenceip, type, dataType, startGenerate, endGenerate);
			
			//连接
			tableName = "t_report_connection_domain"+tableType;
			String unitType = null;
			List<ReportConnection> connRe = null;
			List<ReportConnection> connList = reportDao.getConnectsReport(tableName,fenceip, unitType, startGenerate, endGenerate);
			if(connList.size()!=0 && connList != null){
				connRe = DataConvertor.getExactForConnList(tableType, startGenerate, connList);
			}else{
				connRe = connList;
			}
			
			
			reMap.put("flowRe", flowRe);
			reMap.put("attRe", attRe);
			reMap.put("topnRe", topnRe);
			reMap.put("connRe", connRe);
			return reMap;
		}
		//总览查询2张表的数据组装
		public HashMap<String, Object> getAllReport2(String tableType1,
				String fenceip, String sd1, String ed1, String tableType2,
				String fenceip2, String sd2, String ed2) {
			HashMap<String, Object> reMap = new HashMap<String, Object>();
			HashMap<String, Object> map1 = getAllReport(tableType1, fenceip, sd1, ed1);
			HashMap<String, Object> map2 = getAllReport(tableType2, fenceip2, sd2, ed2);
			//取出其中的list集合重新组合
			List l1 = (List) map1.get("flowRe");
			List l2 = (List) map1.get("attRe");
			List l3 = (List) map1.get("topnRe");
			List l4 = (List) map1.get("connRe");
			List ll1 = (List) map2.get("flowRe");
			List ll2 = (List) map2.get("attRe");
			List ll3 = (List) map2.get("topnRe");
			List ll4 = (List) map2.get("connRe");
			
			for(int i=0;i<ll1.size();i++){
				l1.add(ll1.get(i));
			}
			for(int i=0;i<ll2.size();i++){
				l2.add(ll2.get(i));
			}
			for(int i=0;i<ll3.size();i++){
				l3.add(ll3.get(i));
			}
			for(int i=0;i<ll4.size();i++){
				l4.add(ll4.get(i));
			}
			reMap.put("flowRe", l1);
			reMap.put("attRe", l2);
			reMap.put("topnRe", l3);
			reMap.put("connRe", l4);
			
			return reMap;
		}*/
	
	
	
	
	
	
}
