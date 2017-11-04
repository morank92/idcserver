package com.baoidc.idcserver.service;

import java.util.List;

import com.baoid.idcserver.vo.IntStringClass;
import com.baoidc.idcserver.po.DeviceSourceParam;

public interface ProductConfService {
	//获取所有以配置的参数项
	public List<DeviceSourceParam> getAllConfParam();
	
	//删除配置项同时删除产品和配置项的关系
	public void deleteParam(int id);
	
	//添加配置项同时修改更改的配置项
	public void configParam(List<DeviceSourceParam> paramList);
	
	//获取某一种产品的配置项
	public List<Integer> getProductParam(int proId);

	//获取最外层的提供项
	public List<IntStringClass> getServerOffer();
	//根据最外层的提供想 查询当前所提供的产品
	public List<Integer> getProductOffer(int serverId);
	
	//配置产品
	public void toConfProductParam(List<Integer> checkedArr);

	//判断机房下是否有资产
	public boolean checkHostRoomUse(int id);
}
