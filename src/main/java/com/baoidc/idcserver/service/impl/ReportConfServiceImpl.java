package com.baoidc.idcserver.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baoidc.idcserver.core.AES256Cipher;
import com.baoidc.idcserver.core.SendEmailUtil;
import com.baoidc.idcserver.core.SendNoteUtil;
import com.baoidc.idcserver.core.SystemContant;
import com.baoidc.idcserver.dao.IUserReportConfDAO;
import com.baoidc.idcserver.po.EmailParam;
import com.baoidc.idcserver.po.ReportConf;
import com.baoidc.idcserver.service.IReportConfService;

@Service
public class ReportConfServiceImpl implements IReportConfService {
	
	@Autowired
	private IUserReportConfDAO reportConfDAO;

	//添加用户告警配置
	public void addUserReportConf(List<ReportConf> list){
		ReportConf rc = list.remove(0);
		reportConfDAO.deleteUserReportConf(rc.getUserId(), rc.getType());
		if(list.size()!=0){
			reportConfDAO.addUserReportConf(list);
		}
	}

	//根据ID查询用户告警配置
	public List<ReportConf> queryUserReportConf(int userId){
		return reportConfDAO.queryUserReportConf(userId);
	}

	//获取邮箱告警配置参数
	@Override
	public EmailParam queryManageEmailParam() {
		EmailParam emailParam = reportConfDAO.queryManageEmailParam();
		if(emailParam!=null){
			try {
				emailParam.setEmailPassword(new AES256Cipher().decrypt(emailParam.getEmailPassword(), SystemContant.COMMON_CONTANT, ""));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return emailParam;
	}
	
	//修改邮箱告警配置参数
	public void updateManageEmailParam(EmailParam emailParam){
		try {
			emailParam.setEmailPassword(new AES256Cipher().encrypt(emailParam.getEmailPassword(), SystemContant.COMMON_CONTANT, ""));
		} catch (Exception e) {
			e.printStackTrace();
		}
		reportConfDAO.updateManageEmailParam(emailParam);
	}
	//删除邮箱告警配置参数
	public void deleteManageEmailParam(int id){
		reportConfDAO.deleteManageEmailParam(id);
	}
	//添加邮箱告警配置参数
	public void insertManageEmailParam(EmailParam emailParam){
		try {
			emailParam.setEmailPassword(new AES256Cipher().encrypt(emailParam.getEmailPassword(), SystemContant.COMMON_CONTANT, ""));
		} catch (Exception e) {
			e.printStackTrace();
		}
		reportConfDAO.insertManageEmailParam(emailParam);
	}
	
	//邮箱告警  短信告警 发送
	/*
	 * 参数
	 * 1：用户ID
	 * 2：邮件主题
	 * 3：邮件内容
	 * 4：手机短信参数键值对
	 * 5：switch case  选择模板
	 */
	public void report(int userId,String subject,String content,Map<String,String> map,int flag){
		//获取用户配置信息
		List<ReportConf> userReportConfList = reportConfDAO.queryUserReportConf(userId);
		List<ReportConf> emailList = new ArrayList<ReportConf>();
		List<ReportConf> noteList = new ArrayList<ReportConf>();
		for (ReportConf reportConf : userReportConfList) {
			if(reportConf.getType().equals("note")){
				noteList.add(reportConf);
			}else{
				emailList.add(reportConf);
			}
		}
		//获取管理员邮箱配置参数
		EmailParam emailParam = reportConfDAO.queryManageEmailParam();
		//发送邮件
		for (ReportConf reportConf : emailList) {
			try {
				SendEmailUtil.sendMimeMessage(emailParam, reportConf.getValue(), subject, content);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//发送短信
		for (ReportConf reportConf : noteList) {
//			SendNoteUtil.sendNote(reportConf.getValue(), map, flag);
		}
	}
}
