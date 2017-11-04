package com.baoidc.idcserver.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baoid.idcserver.vo.IntStringClass;
import com.baoidc.idcserver.po.DeviceSourceParam;

public interface ProductConfDAO {
	
	//获取所有以配置的参数项
	public List<DeviceSourceParam> getAllConfParam();
	
	//删除配置项
	public void deleteParam(int id);
	//删除产品和配置项的关系
	public void deleteProductParam(int id);
	
	//添加配置项
	public void addParam(@Param("paramList")List<DeviceSourceParam> paramList);
	
	//修改配置项
	public void updateParam(@Param("paramList")List<DeviceSourceParam> paramList);

	//获取某一种产品的配置项
	public List<Integer> getProductParam(int proId);
	//获取最外层的提供项
	public List<IntStringClass> getServerOffer();
	//根据最外层的提供想 查询当前所提供的产品
	public List<Integer> getProductOffer(int serverId);
	
	//移除产品配置
	public void removeProductParam(int proId);
	//配置产品
	public void toConfProductParam(@Param("checkedArr")List<Integer> checkedArr,@Param("proId")Integer proId);

	//判断机房下面是否有资产
	public int checkHostRoomServersUse(int id);
	public int checkHostRoomChestUse(int id);
	
	
	
	
}
