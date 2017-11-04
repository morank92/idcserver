package com.baoidc.idcserver.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baoidc.idcserver.po.ChargeRecord;
import com.baoidc.idcserver.po.ConsumeRecord;
import com.baoidc.idcserver.po.EncashmentRecord;
import com.baoidc.idcserver.po.UserAccount;
import com.baoidc.idcserver.vo.query.ChargeQueryVO;
import com.baoidc.idcserver.vo.query.ConsumeQueryVO;

public interface IUserAccountDAO {
	
	public void addNewUserAccount(UserAccount userAccount);
	
	public UserAccount getAccountInfo(int userId);
	
	public void addNewChargeRecord(ChargeRecord chargeRecord);
	
	public void addNewConsumeRecord(ConsumeRecord consumeRecord);
	
	public void updateAccountInfo(UserAccount userAccount);
	
	
	//查询充值记录
	public List<ChargeRecord> getChargeRecord(ChargeQueryVO chargeQueryVO);
	
	//查询消费记录
	public List<ConsumeRecord> getConsumeRecord(ConsumeQueryVO consumeQueryVO);
	
	//更新充值记录
	public void updateChargeRecord(ChargeRecord chargeRecord);
	
	public void createPayRecord(Map<String, String> params);
	
	public ChargeRecord getChargeRecordByOrderNo(String orderNo);

	public void updateUserAccountConsumeTotal(UserAccount userAccount);
	
	//创建支付宝交易记录
	public void createAliPayRecord(Map<String,String> params);
	
	//创建提现记录
	public void createEncashmentRecord(EncashmentRecord er);
	
	//根据用户id查询提现记录
	public List<EncashmentRecord> getEncashmentRecordById(Integer id);

	//批量添加
	public void addNewConsumeRecordList(@Param("recordList") List<ConsumeRecord> recordList);
	

}
