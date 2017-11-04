package com.baoidc.idcserver.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baoidc.idcserver.po.EmailParam;
import com.baoidc.idcserver.po.ReportConf;


public interface IUserReportConfDAO {
	
	//添加用户告警配置
	public void addUserReportConf(List<ReportConf> list);

	//根据ID查询用户告警配置
	public List<ReportConf> queryUserReportConf(int userId);
	
	//删除用户告警配置
	public void deleteUserReportConf(@Param("userId")int suerId,@Param("type")String type);

	//获取邮箱告警参数
	public EmailParam queryManageEmailParam();

	//修改邮箱告警配置参数
	public void updateManageEmailParam(EmailParam emailParam);

	//删除邮箱告警配置参数
	public void deleteManageEmailParam(int id);

	//添加邮箱告警配置参数
	public void insertManageEmailParam(EmailParam emailParam);
	
}
