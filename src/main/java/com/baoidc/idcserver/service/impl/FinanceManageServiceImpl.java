package com.baoidc.idcserver.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baoid.idcserver.vo.ConsumeAnalyze;
import com.baoid.idcserver.vo.ConsumeTypeAnalyze;
import com.baoid.idcserver.vo.FinanceAnalyze;
import com.baoidc.idcserver.core.DateUtil;
import com.baoidc.idcserver.dao.FinanceManageDAO;
import com.baoidc.idcserver.po.EncashmentRecord;
import com.baoidc.idcserver.service.FinanceManageService;

@Service
public class FinanceManageServiceImpl implements FinanceManageService {

	@Autowired
	private FinanceManageDAO financeManageDAO;

	//提现管理
	public List<EncashmentRecord> getEncashmentRecord(int userId, String startTime, String endTime) {
		return financeManageDAO.getEncashmentRecord(userId,startTime,endTime);
	}

	public void updateEncashmentRecordStatus(int id) {
		String finashTime = DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss");
		financeManageDAO.updateEncashmentRecordStatus(id, finashTime);
	}

	public void deleteEncashmentRecord(int id) {
		financeManageDAO.deleteEncashmentRecord(id);
	}

	//财务分析  查询每月的充值 提现记录
	public List<FinanceAnalyze> getFinanceAnalyzeByMonth() {
		List<FinanceAnalyze> rechargeByMounth = financeManageDAO.getRechargeByMounth("");
		List<FinanceAnalyze> encashmentByMouth = financeManageDAO.getEncashmentByMouth("");
		ArrayList<FinanceAnalyze> list = new ArrayList<FinanceAnalyze>();
		Map<String,FinanceAnalyze> temp = new HashMap<String, FinanceAnalyze>();
		for (FinanceAnalyze recharge : rechargeByMounth) {
			temp.put(recharge.getMonths(),recharge);
		}
		for (FinanceAnalyze encashment : encashmentByMouth) {
			boolean flag = temp.containsKey(encashment.getMonths());
			if(flag){
				FinanceAnalyze financeAnalyze = temp.get(encashment.getMonths());
				financeAnalyze.setEncashmentAmt(encashment.getEncashmentAmt());
			}else{
				temp.put(encashment.getMonths(), encashment);
			}
		}
		Set<String> keySet = temp.keySet();
		for (String string : keySet) {
			FinanceAnalyze financeAnalyze = temp.get(string);
			financeAnalyze.setMonths(string);
			list.add(financeAnalyze);
		}
		return list;
	}
	public List<String> getHalfTimeArr(){
		GregorianCalendar gc = new GregorianCalendar();
		gc.setGregorianChange(new Date());
		int nowYear = gc.get(Calendar.YEAR);
		int nowMonth = gc.get(Calendar.MONTH)+1;
		//半年时间
		List<String> timeArr = new ArrayList<String>();
		for (int i = 0; i < 6; i++) {
			int flag = nowMonth-i;
			
			if(flag>0){
				if(flag>=10){
					timeArr.add(nowYear+"-"+flag);
				}else{
					timeArr.add(nowYear+"-0"+flag);
				}
			}else{
				flag = 12-flag;
				if(flag>=10){
					timeArr.add((nowYear-1)+"-"+flag);
				}else{
					timeArr.add((nowYear-1)+"-0"+flag);
				}
			}
		}
		return timeArr;
	}
	//近半年的充值提现记录
	public List<Object> getChargeEnshmentHalf() {
		List<String> timeArr = getHalfTimeArr();
		//获取数据
		List<FinanceAnalyze> rechargeByMounth = financeManageDAO.getRechargeByMounth(timeArr.get(timeArr.size()-1));
		List<FinanceAnalyze> encashmentByMouth = financeManageDAO.getEncashmentByMouth(timeArr.get(timeArr.size()-1));
		//处理数据
		//将所有数据线整合到 map集合中
		Map<String,FinanceAnalyze> temp = new HashMap<String, FinanceAnalyze>();
		for (String timeStr : timeArr) {
			FinanceAnalyze fa = new FinanceAnalyze();
			fa.setChargeAmt(0);
			fa.setEncashmentAmt(0);
			fa.setMonths(timeStr);
			temp.put(timeStr,fa);
		}
		for (FinanceAnalyze fa : encashmentByMouth) {
			FinanceAnalyze financeAnalyze = temp.get(fa.getMonths());
			financeAnalyze.setEncashmentAmt(fa.getEncashmentAmt());
		}
		for (FinanceAnalyze fa : rechargeByMounth) {
			FinanceAnalyze financeAnalyze = temp.get(fa.getMonths());
			financeAnalyze.setChargeAmt(fa.getChargeAmt());
		}
		List<Double> listCharge = new ArrayList<Double>();
		List<Double> listEncashment = new ArrayList<Double>();
		for (String str : timeArr) {
			listCharge.add(temp.get(str).getChargeAmt());
			listEncashment.add(temp.get(str).getEncashmentAmt());
		}
		
		//倒叙
		Collections.reverse(timeArr);
		Collections.reverse(listCharge);
		Collections.reverse(listEncashment);
		List<Object> list = new ArrayList<Object>();
		list.add(timeArr);
		list.add(listCharge);
		list.add(listEncashment);
		return list;
	}

	//消费分析
	public List<ConsumeAnalyze> getAllConsumeRecord(String startTime, String endTime, int userId) {
		return financeManageDAO.getAllConsumeRecord(startTime,endTime,userId);
	}
	//近半年的消费类型统计记录
	public List<Object> getConsumeHalf(int userId) {
		List<String> timeArr = getHalfTimeArr();
		//得到数据
		List<ConsumeTypeAnalyze> totalList =  financeManageDAO.getConsumeByTypeMonth(timeArr.get(timeArr.size()-1),userId);
		//设备租用
		List<Double> listDevicesRent = new ArrayList<Double>();
		//设备托管
		List<Double> listDevicesTrusteeship = new ArrayList<Double>();
		//机柜租用
		List<Double> listChestRent = new ArrayList<Double>();
		//提现
		List<Double> listEncashment = new ArrayList<Double>();
		int i = 0;
		for (String timeStr : timeArr) {
			listDevicesRent.add(0.0);
			listDevicesTrusteeship.add(0.0);
			listChestRent.add(0.0);
			listEncashment.add(0.0);
			for (ConsumeTypeAnalyze cy : totalList) {
				if(cy.getMonths().equals(timeStr)){
					//服务器租用
					if(cy.getInstanceType()==1){
						listDevicesRent.set(i,listDevicesRent.get(i)+cy.getConsumeTotal());
					}
					//服务器托管
					if(cy.getInstanceType()==2){
						listDevicesTrusteeship.set(i,listDevicesTrusteeship.get(i)+cy.getConsumeTotal());
					}
					//机柜租用
					if(cy.getInstanceType()==3){
						listChestRent.set(i,listChestRent.get(i)+cy.getConsumeTotal());
					}
					//提现
					if(cy.getInstanceType()==4){
						listEncashment.set(i,listEncashment.get(i)+cy.getConsumeTotal());
					}
					
				}
			}
			i++;
		}
		List<Object> list = new ArrayList<Object>();
		Collections.reverse(timeArr);
		Collections.reverse(listDevicesRent);
		Collections.reverse(listDevicesTrusteeship);
		Collections.reverse(listChestRent);
		Collections.reverse(listEncashment);
		list.add(timeArr);
		list.add(listDevicesRent);
		list.add(listDevicesTrusteeship);
		list.add(listChestRent);
		list.add(listEncashment);
		return list;
	}

	public EncashmentRecord getEncashmentById(int id) {
		return financeManageDAO.getEncashmentById(id);
	}
	
	
	
	
	
	

}
