package com.baoidc.idcserver.service;

import java.util.List;

import com.baoid.idcserver.vo.ConsumeAnalyze;
import com.baoid.idcserver.vo.FinanceAnalyze;
import com.baoidc.idcserver.po.EncashmentRecord;


public interface FinanceManageService {
	
	//提现管理
	//查询所有的提现记录
	public List<EncashmentRecord>  getEncashmentRecord(int userId, String startTime, String endTime);
	//处理提现  修改提现状态
	public void updateEncashmentRecordStatus(int id);
	//删除提现记录
	public void deleteEncashmentRecord(int id);
	
	//财务分析  之   充值  --  提现 
	//财务分析  查询每月的充值 提现记录
	public List<FinanceAnalyze> getFinanceAnalyzeByMonth();
	//查询近半年的充值提现记录
	public List<Object> getChargeEnshmentHalf();
	
	//消费分析
	public List<ConsumeAnalyze> getAllConsumeRecord(String startTime, String endTime, int userId);
	//统计近半年的消费类型统计
	public List<Object> getConsumeHalf(int userId); 
	//根据提现记录Id获取提现信息
	public EncashmentRecord getEncashmentById(int id);
	
}
