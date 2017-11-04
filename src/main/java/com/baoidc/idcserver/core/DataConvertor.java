package com.baoidc.idcserver.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import com.baoidc.idcserver.po.ReportConnection;
import com.baoidc.idcserver.po.ReportFlow;
import com.baoidc.idcserver.vo.query.EchartsQueryVO;


public class DataConvertor {
	
	/**
	 * 原始攻击类型转化为可视的类型
	 * @param keyType
	 * @return
	 */
	public static String numType2MyType(String keyType){
		HashMap<String, String> map = new HashMap<String, String>();
		//我的攻击类型
		/*String[] attTypeArray = new String[]{
		};*/
		map.put("10","SYN");
		map.put("11","ACK");
		map.put("12","SYN-ACK");
		map.put("13","FIN/RST");
		map.put("16","TCP");
		map.put("17","TCP FRAG");
		map.put("19","UDP");
		map.put("21","UDP FRAG");
		map.put("23","ICMP");
		map.put("26","HTTPS");
		map.put("27","HTTP");
		map.put("29","DNS QUERY");
		map.put("30","DNS REPLY");
		
		String myAttType = map.get(keyType); 
		if(myAttType == null){
			myAttType = keyType;
		}
		return myAttType;
	}
	/**
	 * 可视攻击类型转换为数据库存储的攻击类型
	 * @param keyType
	 * @return
	 */
	public static String myType2numType(String keyType){
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("SYN","10");
		map.put("ACK","11");
		map.put("SYN-ACK","12");
		map.put("FIN/RST","13");
		map.put("TCP","16");
		map.put("TCP FRAG","17");
		map.put("UDP","19");
		map.put("UDP FRAG","21");
		map.put("ICMP","23");
		map.put("HTTPS","26");
		map.put("HTTP","27");
		map.put("DNS QUERY","29");
		map.put("DNS REPLY","30");
		
		String myAttType = map.get(keyType); 
		if(myAttType == null){
			myAttType = keyType;
		}
		return myAttType;
	}
	
	//连接数的时间验证
	public static List<ReportConnection> getExactForConnList(String tableType, String startGenerate, List<ReportConnection> flist){
		//遍历验证报表时间的准确性
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar sc = new GregorianCalendar();//查询的开始时间
		Calendar sc2 = new GregorianCalendar();//实际开始时间
		Calendar sc3 = new GregorianCalendar();//中间标准开始时间
		List<ReportConnection> newlist = new ArrayList<ReportConnection>();
		try {
				sc2.setTime(sdf.parse(flist.get(0).getGenerateTime()));
			
		} catch (ParseException e1) {
			e1.printStackTrace();
		}//数据库实际开始的时间
		try {
				sc.setTime(sdf.parse(startGenerate));//查询的开始时间
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
				sc3.setTime(sdf.parse(flist.get(0).getGenerateTime()));
			
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		//补前面的时间所属的对象
		if(tableType.length() > 7){
			sc2.add(Calendar.MINUTE, -2);//减去2分钟
		}
		
		List<ReportConnection> list1 = new ArrayList<ReportConnection>();
		while(sc2.getTimeInMillis() > sc.getTimeInMillis()){//直到等于或者小于（查询时间加2分钟）
			if(tableType.length() > 7){
				sc2.add(Calendar.MINUTE, -2);//减去2分钟
			}else{
				sc2.add(Calendar.HOUR, -1);//减去一小时
			}
			ReportConnection rf = new ReportConnection();
			if(sc2.getTimeInMillis() < sc.getTimeInMillis()){//因为是先减去了2分钟所有为了防止小于查询开始时间
				rf.setGenerateTime(sdf.format(sc.getTime()));//如果小于就让他等于查询的开始时间
			}else{
				rf.setGenerateTime(sdf.format(sc2.getTime()));
			}
			list1.add(rf);
		}
		//顺序放入
		for(int l=list1.size()-1; l>0; l--){
			newlist.add(list1.get(l));
		}
		//补中间有可能断掉的时间所属对象
		Calendar sc4 = new GregorianCalendar();
		for(int i=0;i<flist.size();i++){
			ReportConnection flow = flist.get(i);
			try {
				sc4.setTime(sdf.parse(flow.getGenerateTime()));//数据库中每一个真实的时间点
				if(sc4.getTimeInMillis() - sc3.getTimeInMillis() <= 5*60*1000){//在5分钟内的误差时间不需要处理
					newlist.add(flow);
					sc3.setTime(sdf.parse(flow.getGenerateTime()));//重置sc3时间等于sc4
					if(tableType.length() > 7){
						sc3.add(Calendar.MINUTE, 2);
					}else{
						sc3.add(Calendar.HOUR, 1);//加一小时
					}
					
				}else{
					//补时间点，
					sc4.setTime(sdf.parse(flow.getGenerateTime()));//数据库中每一个真实的时间点
					//连续循环补时间点
					while(sc4.getTimeInMillis() - sc3.getTimeInMillis() > 5*60*1000){//直到等于或者小于（查询时间加2分钟）
						ReportConnection flow2 = new ReportConnection();
						flow2.setGenerateTime(sdf.format(sc3.getTime()));
						newlist.add(flow2);
						if(tableType.length() > 7){
							sc3.add(Calendar.MINUTE, 2);
						}else{
							sc3.add(Calendar.HOUR, 1);//加一小时
						}
					}
					//跳出循环后重新设置sc3同步sc4时间,且加上这次的对象
					sc3.setTime(sdf.parse(flow.getGenerateTime()));//重新获取最新的时间点
					newlist.add(flow);
					if(tableType.length() > 7){
						sc3.add(Calendar.MINUTE, 2);
					}else{
						sc3.add(Calendar.HOUR, 1);//加一小时
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return newlist;
	}
	
	
	
	//flow的时间验证
	public static List<ReportFlow> getExactForReportFlowList(String tableType, String startGenerate, List<ReportFlow> flist){
		//遍历验证报表时间的准确性
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Calendar sc = new GregorianCalendar();//查询的开始时间
				Calendar sc2 = new GregorianCalendar();//实际开始时间
				Calendar sc3 = new GregorianCalendar();//中间标准开始时间
				List<ReportFlow> newlist = new ArrayList<ReportFlow>();
				try {
						sc2.setTime(sdf.parse(flist.get(0).getGenerateTime()));//数据库实际开始的时间
					
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				try {
						sc.setTime(sdf.parse(startGenerate));//查询的开始时间
					
				} catch (ParseException e) {
					e.printStackTrace();
				}
				try {
						sc3.setTime(sdf.parse(flist.get(0).getGenerateTime()));//用于标准的数据开始时间
					
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				//补前面的时间所属的对象
				if(tableType.length() > 7){//20170506为普通表，天表数据
					sc2.add(Calendar.MINUTE, -2);//减去2分钟
				}
				
				List<ReportFlow> list1 = new ArrayList<ReportFlow>();
				while(sc2.getTimeInMillis() > sc.getTimeInMillis()){//直到实际开始时间等于或者小于查询时间（查询时间加2分钟）
					if(tableType.length() > 7){
						sc2.add(Calendar.MINUTE, -2);//减去2分钟
					}else{
						sc2.add(Calendar.HOUR, -1);//减去一小时
					}
					ReportFlow rf = new ReportFlow();
					if(sc2.getTimeInMillis() < sc.getTimeInMillis()){//防止小于查询开始时间
						rf.setGenerateTime(sdf.format(sc.getTime()));//如果小于就让他等于查询的开始时间
					}else{
						rf.setGenerateTime(sdf.format(sc2.getTime()));
					}
					list1.add(rf);
				}
				//纠正顺序放入
				for(int l=list1.size()-1; l>0; l--){
					newlist.add(list1.get(l));
				}
				//补中间有可能断掉的时间所属对象
				Calendar sc4 = new GregorianCalendar();
				for(int i=0;i<flist.size();i++){
					ReportFlow flow = flist.get(i);
					try {
							sc4.setTime(sdf.parse(flow.getGenerateTime()));//数据库中每一个真实的时间点
						//判断在一段时内是否没有数据
						if(sc4.getTimeInMillis() - sc3.getTimeInMillis() <= 5*60*1000){//在5分钟内的误差时间不需要处理
							newlist.add(flow);
							sc3.setTime(sdf.parse(flow.getGenerateTime()));//重置sc3时间等于sc4
							if(tableType.length() > 7){
								sc3.add(Calendar.MINUTE, 2);
							}else{
								sc3.add(Calendar.HOUR, 1);//加一小时
							}
							
						}else{
							//连续循环补时间点
							while(sc4.getTimeInMillis() - sc3.getTimeInMillis() > 5*60*1000){//直到等于或者小于（查询时间加2分钟）
								ReportFlow flow2 = new ReportFlow();
								flow2.setGenerateTime(sdf.format(sc3.getTime()));
								newlist.add(flow2);
								if(tableType.length() > 7){
									sc3.add(Calendar.MINUTE, 2);
								}else{
									sc3.add(Calendar.HOUR, 1);//加一小时
								}
							}
							//跳出循环后重新设置sc3同步sc4时间,且加上这次的对象
							sc3.setTime(sdf.parse(flow.getGenerateTime()));//重新获取最新的时间点
							newlist.add(flow);
							if(tableType.length() > 7){
								sc3.add(Calendar.MINUTE, 2);
							}else{
								sc3.add(Calendar.HOUR, 1);//加一小时
							}
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				return newlist;
	}
	
	//新的报表完善数据
	//流量对比
	public static List<ReportFlow> getExactData4ReportFlowList(
			EchartsQueryVO echartsQueryVo, List<ReportFlow> flist) {
		//遍历验证报表时间的准确性
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar sc = new GregorianCalendar();//查询的开始时间
		Calendar sc2 = new GregorianCalendar();//实际开始时间
		Calendar sc3 = new GregorianCalendar();//中间标准开始时间
		
		Calendar ec1 = new GregorianCalendar();//数据库结束时间
		Calendar ec2 = new GregorianCalendar();//查询的结束时间
		
		List<ReportFlow> newlist = new ArrayList<ReportFlow>();
		
		String tableType = echartsQueryVo.getTableType();//判断是加几分钟还是加一小时，因为有以天为单位的表还有以月为单位的表
		String stime = echartsQueryVo.getStime();//查询的开始时间
		String etime = echartsQueryVo.getEtime();//查询的结束时间
		
		
		try {
				sc.setTime(sdf.parse(stime));//查询的开始时间
				sc2.setTime(sdf.parse(flist.get(0).getGenerateTime()));//数据库实际开始的时间--因为此参数会重新赋值所以需要另外初始一个值
				sc3.setTime(sdf.parse(flist.get(0).getGenerateTime()));//用于标准的数据开始时间
				
				
				ec1.setTime(sdf.parse(flist.get(flist.size()-1).getGenerateTime()));//数据库实际结束时间
				ec2.setTime(sdf.parse(etime));//查询的结束时间
			
		}
		 catch (ParseException e1) {
			e1.printStackTrace();
		}
		//补前面的时间所属的对象
		if(tableType.length() > 7){//20170506为普通表，天表数据
			sc2.add(Calendar.MINUTE, -2);//减去2分钟
		}
		
		List<ReportFlow> list1 = new ArrayList<ReportFlow>();
		while(sc2.getTimeInMillis() > sc.getTimeInMillis()){//直到实际开始时间等于或者小于查询时间（查询时间加2分钟）
			if(tableType.length() > 7){
				sc2.add(Calendar.MINUTE, -2);//减去2分钟
			}else{
				sc2.add(Calendar.HOUR, -1);//减去一小时
			}
			ReportFlow rf = new ReportFlow();
			if(sc2.getTimeInMillis() < sc.getTimeInMillis()){//防止小于查询开始时间
				rf.setGenerateTime(sdf.format(sc.getTime()));//如果小于就让他等于查询的开始时间
			}else{
				rf.setGenerateTime(sdf.format(sc2.getTime()));
			}
			list1.add(rf);
		}
		//纠正顺序放入
		for(int l=list1.size()-1; l>0; l--){
			newlist.add(list1.get(l));
		}
		
		//补中间有可能断掉的时间所属对象
		Calendar sc4 = new GregorianCalendar();
		for(int i=0;i<flist.size();i++){
			ReportFlow flow = flist.get(i);
			try {
					sc4.setTime(sdf.parse(flow.getGenerateTime()));//数据库中每一个真实的时间点
				//判断在一段时内是否没有数据
				if(sc4.getTimeInMillis() - sc3.getTimeInMillis() <= 5*60*1000){//在5分钟内的误差时间不需要处理
					newlist.add(flow);
					sc3.setTime(sdf.parse(flow.getGenerateTime()));//重置sc3时间等于sc4
					if(tableType.length() > 7){
						sc3.add(Calendar.MINUTE, 2);//数据库中每条数据的时间间隔
					}else{
						sc3.add(Calendar.HOUR, 1);//加一小时
					}
					
				}else{
					//连续循环补时间点
					while(sc4.getTimeInMillis() - sc3.getTimeInMillis() > 5*60*1000){//直到等于或者小于（查询时间加2分钟）
						ReportFlow flow2 = new ReportFlow();
						flow2.setGenerateTime(sdf.format(sc3.getTime()));
						newlist.add(flow2);
						if(tableType.length() > 7){
							sc3.add(Calendar.MINUTE, 2);
						}else{
							sc3.add(Calendar.HOUR, 1);//加一小时
						}
					}
					//跳出循环后重新设置sc3同步sc4时间,且加上这次的对象
					sc3.setTime(sdf.parse(flow.getGenerateTime()));//重新获取最新的时间点
					newlist.add(flow);
					if(tableType.length() > 7){
						sc3.add(Calendar.MINUTE, 2);
					}else{
						sc3.add(Calendar.HOUR, 1);//加一小时
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			//补后面的时间段
			while(ec2.getTimeInMillis() > ec1.getTimeInMillis()){//如果查询时间大于数据库实际时间成立（查询时间加2分钟）
				if(tableType.length() > 7){
					ec1.add(Calendar.MINUTE, 2);//+2分钟
				}else{
					ec1.add(Calendar.HOUR, 1);//+一小时
				}
				ReportFlow rf = new ReportFlow();
				rf.setGenerateTime(sdf.format(ec1.getTime()));
				newlist.add(rf);
			}
			
		}
		return newlist;
		
	}
	
	//完善连接数报表的数据
	public static List<ReportConnection> getExactData4ConnList(
			EchartsQueryVO echartsQueryVo, List<ReportConnection> flist) {
		//遍历验证报表时间的准确性
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar sc = new GregorianCalendar();//查询的开始时间
		Calendar sc2 = new GregorianCalendar();//实际开始时间
		Calendar sc3 = new GregorianCalendar();//中间标准开始时间
		
		Calendar ec1 = new GregorianCalendar();//数据库结束时间
		Calendar ec2 = new GregorianCalendar();//查询的结束时间
		
		
		String tableType = echartsQueryVo.getTableType();//判断是加几分钟还是加一小时，因为有以天为单位的表还有以月为单位的表
		String stime = echartsQueryVo.getStime();//查询的开始时间
		String etime = echartsQueryVo.getEtime();//查询的结束时间
		
		List<ReportConnection> newlist = new ArrayList<ReportConnection>();
		try {
			sc.setTime(sdf.parse(stime));//查询的开始时间
			sc2.setTime(sdf.parse(flist.get(0).getGenerateTime()));//数据库实际开始的时间--因为此参数会重新赋值所以需要另外初始一个值
			sc3.setTime(sdf.parse(flist.get(0).getGenerateTime()));//用于标准的数据开始时间
			
			
			ec1.setTime(sdf.parse(flist.get(flist.size()-1).getGenerateTime()));//数据库实际结束时间
			ec2.setTime(sdf.parse(etime));//查询的结束时间
		
			}
			 catch (ParseException e1) {
				e1.printStackTrace();
			}
		//补前面的时间所属的对象
		if(tableType.length() > 7){
			sc2.add(Calendar.MINUTE, -2);//减去2分钟
		}
		
		List<ReportConnection> list1 = new ArrayList<ReportConnection>();
		while(sc2.getTimeInMillis() > sc.getTimeInMillis()){//直到等于或者小于（查询时间加2分钟）
			if(tableType.length() > 7){
				sc2.add(Calendar.MINUTE, -2);//减去2分钟
			}else{
				sc2.add(Calendar.HOUR, -1);//减去一小时
			}
			ReportConnection rf = new ReportConnection();
			if(sc2.getTimeInMillis() < sc.getTimeInMillis()){//因为是先减去了2分钟所有为了防止小于查询开始时间
				rf.setGenerateTime(sdf.format(sc.getTime()));//如果小于就让他等于查询的开始时间
			}else{
				rf.setGenerateTime(sdf.format(sc2.getTime()));
			}
			list1.add(rf);
		}
		//顺序放入
		for(int l=list1.size()-1; l>0; l--){
			newlist.add(list1.get(l));
		}
		//补中间有可能断掉的时间所属对象
		Calendar sc4 = new GregorianCalendar();
		for(int i=0;i<flist.size();i++){
			ReportConnection flow = flist.get(i);
			try {
				sc4.setTime(sdf.parse(flow.getGenerateTime()));//数据库中每一个真实的时间点
				if(sc4.getTimeInMillis() - sc3.getTimeInMillis() <= 5*60*1000){//在5分钟内的误差时间不需要处理
					newlist.add(flow);
					sc3.setTime(sdf.parse(flow.getGenerateTime()));//重置sc3时间等于sc4
					if(tableType.length() > 7){
						sc3.add(Calendar.MINUTE, 2);
					}else{
						sc3.add(Calendar.HOUR, 1);//加一小时
					}
					
				}else{
					//补时间点，
					sc4.setTime(sdf.parse(flow.getGenerateTime()));//数据库中每一个真实的时间点
					//连续循环补时间点
					while(sc4.getTimeInMillis() - sc3.getTimeInMillis() > 5*60*1000){//直到等于或者小于（查询时间加2分钟）
						ReportConnection flow2 = new ReportConnection();
						flow2.setGenerateTime(sdf.format(sc3.getTime()));
						newlist.add(flow2);
						if(tableType.length() > 7){
							sc3.add(Calendar.MINUTE, 2);
						}else{
							sc3.add(Calendar.HOUR, 1);//加一小时
						}
					}
					//跳出循环后重新设置sc3同步sc4时间,且加上这次的对象
					sc3.setTime(sdf.parse(flow.getGenerateTime()));//重新获取最新的时间点
					newlist.add(flow);
					if(tableType.length() > 7){
						sc3.add(Calendar.MINUTE, 2);
					}else{
						sc3.add(Calendar.HOUR, 1);//加一小时
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			//补后面的时间段
			while(ec2.getTimeInMillis() > ec1.getTimeInMillis()){//如果查询时间大于数据库实际时间成立（查询时间加2分钟）
				if(tableType.length() > 7){
					ec1.add(Calendar.MINUTE, 2);//+2分钟
				}else{
					ec1.add(Calendar.HOUR, 1);//+一小时
				}
				ReportConnection rf = new ReportConnection();
				rf.setGenerateTime(sdf.format(ec1.getTime()));
				newlist.add(rf);
			}
		}
		return newlist;
	}
	
	
}
