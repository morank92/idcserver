package com.baoidc.idcserver.dao;

import java.util.List;
import javax.ws.rs.PathParam;

import org.apache.ibatis.annotations.Param;

import com.baoid.idcserver.vo.ConsumeAnalyze;
import com.baoid.idcserver.vo.ConsumeTypeAnalyze;
import com.baoid.idcserver.vo.FinanceAnalyze;
import com.baoidc.idcserver.po.EncashmentRecord;

public interface FinanceManageDAO {
	
	//提现管理
	//查询所有的提现记录
	public  List<EncashmentRecord> getEncashmentRecord(@Param("userId")int userId, @Param("startTime")String startTime,@Param("endTime") String endTime);
	//处理提现  修改提现状态
	public void updateEncashmentRecordStatus(@Param("id")int id,@Param("finashTime")String finashTime);
	//删除提现记录
	public void deleteEncashmentRecord(int id);
	
	//财务分析  之   充值  --  提现 
	//分月查询充值记录
	public List<FinanceAnalyze> getRechargeByMounth(@Param("timeStr") String timeStr);
	//分月查询提现记录
	public List<FinanceAnalyze> getEncashmentByMouth(@Param("timeStr") String timeStr);
	
	
	//消费分析
	//查询所有的消费记录
	public List<ConsumeAnalyze> getAllConsumeRecord(@Param("startTime")String startTime, @Param("endTime")String endTime,@Param("userId")int userId);
	//分类查询近半年的消费记录
	public List<ConsumeTypeAnalyze> getConsumeByTypeMonth(@Param("timeStr")String timeStr,@Param("userId")int userId);
	
	public EncashmentRecord getEncashmentById(@Param("id") int id);
	
	
	
	
	
	
}
