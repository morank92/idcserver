package com.baoidc.idcserver.service;

import java.util.List;
import java.util.Map;

import com.baoidc.idcserver.po.EmailParam;
import com.baoidc.idcserver.po.ReportConf;


public interface IReportConfService {
	
	//添加用户告警配置
	public void addUserReportConf(List<ReportConf> list);

	//根据ID查询用户告警配置
	public List<ReportConf> queryUserReportConf(int userId);

	//获取邮箱告警配置参数
	public EmailParam queryManageEmailParam();

	//修改邮箱告警配置参数
	public void updateManageEmailParam(EmailParam emailParam);

	//删除邮箱告警配置参数
	public void deleteManageEmailParam(int id);

	//添加邮箱告警配置参数
	public void insertManageEmailParam(EmailParam emailParam);
	
	//邮箱告警  短信告警 发送 
	public void report(int userId,String subject,String content,Map<String,String> map,int flag);

}
