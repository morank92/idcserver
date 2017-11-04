package com.baoidc.idcserver.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baoidc.idcserver.core.ChargeStatus;
import com.baoidc.idcserver.core.DateUtil;
import com.baoidc.idcserver.core.SystemContant;
import com.baoidc.idcserver.dao.IUserAccountDAO;
import com.baoidc.idcserver.dao.IUserDAO;
import com.baoidc.idcserver.po.ChargeRecord;
import com.baoidc.idcserver.po.ConsumeRecord;
import com.baoidc.idcserver.po.EncashmentRecord;
import com.baoidc.idcserver.po.User;
import com.baoidc.idcserver.po.UserAccount;
import com.baoidc.idcserver.service.IUserAccountService;
import com.baoidc.idcserver.vo.query.ChargeQueryVO;
import com.baoidc.idcserver.vo.query.ConsumeQueryVO;

@Service("userAccountService")
public class UserAccountServiceImpl implements IUserAccountService {
	
	@Autowired
	private IUserAccountDAO userAccountDAO;
	
	@Autowired
	private IUserDAO userDao;

	public UserAccount getUserAccountInfo(int userId) {
		UserAccount userAccount = userAccountDAO.getAccountInfo(userId);
		userAccount.setAccountBalance(userAccount.getChargeTotal() - userAccount.getConsumeTotal()); //计算余额
		return userAccount;
	}

	public ChargeRecord addNewChargeRecord(ChargeRecord chargeRecord) {
		String chargeNo = SystemContant.COMMON_CONTANT+DateUtil.dateToStr(new Date(), "yyyyMMddHHmmss");
		chargeRecord.setChargeNo(chargeNo);
		chargeRecord.setChargeCreateTime(DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
		chargeRecord.setChargeStatus(ChargeStatus.CREATED.getStatusValue());//充值状态，初始为新建
		userAccountDAO.addNewChargeRecord(chargeRecord);
		return chargeRecord;
	}

	public ConsumeRecord addNewConsumeRecord(ConsumeRecord consumeRecord) {
		String consumeNo = SystemContant.COMMON_CONTANT+DateUtil.dateToStr(new Date(), "yyyyMMddHHmmss");
		consumeRecord.setConsumeNo(consumeNo);
		consumeRecord.setConsumeTime(DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
		userAccountDAO.addNewConsumeRecord(consumeRecord);
		if(consumeRecord.getRecordId() != 0){  //新增消费修改账户信息
			System.out.println("getRecordId?..........>>> "+consumeRecord.getRecordId());
			UserAccount userAccount = new UserAccount();
			userAccount.setUserId(consumeRecord.getUserId());
			userAccount.setAccountId(consumeRecord.getAccountId());
			userAccount.setConsumeTotal(consumeRecord.getConsumeAmt());
			userAccount.setChargeTotal(0.00);
			userAccountDAO.updateAccountInfo(userAccount);
		}
		return consumeRecord;
	}
	//更新账户消费总额
	public void updateUserAccountInfo(UserAccount userAccount) {
		userAccountDAO.updateAccountInfo(userAccount);
		
	}

	public List<ChargeRecord> getChargeRecord(Integer userId,String startTime,String endTime) {
		ChargeQueryVO queryVO = new ChargeQueryVO();
		queryVO.setUserId(userId);
		queryVO.setStartTime(startTime);
		queryVO.setEndTime(endTime);
		return userAccountDAO.getChargeRecord(queryVO);
	}

	public List<ConsumeRecord> getConsumeRecord(ConsumeQueryVO consumeQueryVO) {
		return userAccountDAO.getConsumeRecord(consumeQueryVO);
	}

	public void handlePayResult(Map<String, String> params) {
		System.out.println("aaaa->");
		if(params != null){
			userAccountDAO.createPayRecord(params);
			String respCode = params.get("respCode");
			String orderNo = params.get("orderId");
			if(respCode !=  null && "00".equalsIgnoreCase(respCode)){//支付成功
				//更改充值记录
				ChargeRecord chargeRecord = userAccountDAO.getChargeRecordByOrderNo(orderNo);
				if(chargeRecord !=  null){
					chargeRecord.setChargeStatus(ChargeStatus.FINISHED.getStatusValue());
					chargeRecord.setChargeFinishTime(DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
					userAccountDAO.updateChargeRecord(chargeRecord);
					//更新用户账户信息
					int userId = chargeRecord.getUserId();
					int accountId = chargeRecord.getAccountId();
					UserAccount userAccount = new UserAccount();
					userAccount.setUserId(userId);
					userAccount.setAccountId(accountId);
					userAccount.setChargeTotal(chargeRecord.getChargeAmt());
					userAccount.setConsumeTotal(0.00);
					userAccountDAO.updateAccountInfo(userAccount);
				}
			}
		}
	}
	
	public void handleAliPayResult(Map<String, String> params) {
		String orderNo = params.get("out_trade_no");
		userAccountDAO.createAliPayRecord(params);
		ChargeRecord chargeRecord = userAccountDAO.getChargeRecordByOrderNo(orderNo);
		if(chargeRecord!=null){
			chargeRecord.setChargeStatus(ChargeStatus.FINISHED.getStatusValue());
			chargeRecord.setChargeFinishTime(DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
			userAccountDAO.updateChargeRecord(chargeRecord);
			//更新用户账户信息
			int userId = chargeRecord.getUserId();
			int accountId = chargeRecord.getAccountId();
			UserAccount userAccount = new UserAccount();
			userAccount.setAccountId(accountId);
			userAccount.setUserId(userId);
			userAccount.setChargeTotal(chargeRecord.getChargeAmt());
			userAccount.setConsumeTotal(0.00);
			userAccountDAO.updateAccountInfo(userAccount);
		}
	}

	public void createEncashmentRecord(int userId, double encashmentAmt) {
		//添加提现记录
		User user = userDao.getUserInfoById(userId);
		UserAccount accountInfo = userAccountDAO.getAccountInfo(userId);
		EncashmentRecord er = new EncashmentRecord();
		er.setEncashmentAccount(user.getZfbao());
		er.setEncashmentAmt(encashmentAmt);
		er.setEncashmentCreateTime(DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
		String encashmentNo = SystemContant.COMMON_CONTANT+DateUtil.dateToStr(new Date(), "yyyyMMddHHmmss");
		er.setEncashmentNo(encashmentNo);
		er.setUserId(userId);
		userAccountDAO.createEncashmentRecord(er);
		//添加消费记录
		ConsumeRecord cr = new ConsumeRecord();
		cr.setAccountId(accountInfo.getAccountId());
		cr.setConsumeAmt(encashmentAmt);
		String consumeNo = SystemContant.COMMON_CONTANT+DateUtil.dateToStr(new Date(), "yyyyMMddHHmmss");
		cr.setConsumeNo(consumeNo);
		cr.setConsumeTime(DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
		cr.setOrderNo(er.getEncashmentNo());
		cr.setUserId(userId);
		cr.setOrderType("6");
		cr.setInstanceType(4);
		cr.setProductName("提现");
		userAccountDAO.addNewConsumeRecord(cr);
		
		//用户账户消费总额加上提金额
		UserAccount ua = new UserAccount();
		ua.setChargeTotal(0.0);
		ua.setConsumeTotal(encashmentAmt);
		ua.setUserId(userId);
		ua.setAccountId(accountInfo.getAccountId());
		userAccountDAO.updateAccountInfo(ua);
		
	}

	public List<EncashmentRecord> getEncashmentRecordById(Integer id) {
		return userAccountDAO.getEncashmentRecordById(id);
	}
	

}
