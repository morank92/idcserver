package com.baoidc.idcserver.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baoidc.idcserver.po.ChargeRecord;
import com.baoidc.idcserver.po.ConsumeRecord;
import com.baoidc.idcserver.po.EncashmentRecord;
import com.baoidc.idcserver.po.UserAccount;
import com.baoidc.idcserver.vo.query.ConsumeQueryVO;

public interface IUserAccountService {
	
	public UserAccount getUserAccountInfo(int userId);
	
	public ChargeRecord addNewChargeRecord(ChargeRecord chargeRecord);
	
	public ConsumeRecord addNewConsumeRecord(ConsumeRecord consumeRecord);
	
	//查询充值记录
	public List<ChargeRecord> getChargeRecord(Integer userId,String startTime,String endTime);
	
	//查询消费记录
	public List<ConsumeRecord> getConsumeRecord(ConsumeQueryVO consumeQueryVO);
	
	public void handlePayResult(Map<String,String> params);
	
	//支付宝支付成功处理业务
	public void handleAliPayResult(Map<String,String> params);

	//更新账户消费总额
	public void updateUserAccountInfo(UserAccount userAccount);
	
	//创建提现记录
	public void createEncashmentRecord(int userId,double encashmentAmt);
	
	//根据用户id查询提现记录
	public List<EncashmentRecord> getEncashmentRecordById(Integer id);


}
