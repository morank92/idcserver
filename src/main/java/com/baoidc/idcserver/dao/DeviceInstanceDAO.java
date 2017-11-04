package com.baoidc.idcserver.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baoidc.idcserver.po.AutoRenewConf;
import com.baoidc.idcserver.po.DeviceInstance;
import com.baoidc.idcserver.po.DeviceSourceParam;
import com.baoidc.idcserver.po.InstanceIp;
import com.baoidc.idcserver.po.InstanceParam;
import com.baoidc.idcserver.po.Order;
import com.baoidc.idcserver.po.OrderParam;
import com.baoidc.idcserver.po.ServerRentOrder;
import com.baoidc.idcserver.po.ServerRoom;


public interface DeviceInstanceDAO {
	
	//获取用户的所有产品实例by userId
	public List<DeviceInstance> getInstanceListByUserId(int userId);
	
	//获取产品实例by deviceInstance
	public List<DeviceInstance> getInstanceList(DeviceInstance deviceInstance);

	//获取单个实例by instanceId
	public DeviceInstance getInstanceByInstanceId(int instanceId);
	
	//新建产品实例
	public void generateNewInstance(DeviceInstance deviceInstance);
	
	//新建产品实例属性
	public void addInstanceParams(@Param("instanceParamList")List<InstanceParam> instanceParamList);
	
	//修改产品实例属性
	public void updateInstanceParam(@Param("instanceParamList")List<InstanceParam> instanceParamList);
	
	//更新产品实例
	public void updateInstance(DeviceInstance deviceInstance);
	
	//获取带回收产品实例
	public List<DeviceInstance> getPreRecycleInstanceList();
	
	//根据实例ID查询产品实例参数
	public InstanceParam getInstParam(InstanceParam instParam);
	
	//删除实例参数
	public void deleteInstancParam(String instanceId);

	//查询ip对象通过 ipStr
	public InstanceIp getInstanceIpByIpstr(@Param("ipStr")String ipStr);

	//通过设备id查询实例
	public DeviceInstance getInstanceByDeviceId(@Param("deviceId")int deviceId);

	//获取由订单产生的实例，by orderId
	public List<DeviceInstance> getInstanceByorderId(String orderId);

	//获取机房下所有的实例
	public List<DeviceInstance> getAllInstanceByHostRoom(@Param("hostRoom")String hostRoom,@Param("userId")int userId);

	//从数据库获取实例，保证数据同步统一性，insList：页面现有的实例集合
	public DeviceInstance getSingleInstance(DeviceInstance deviceInstance);
	
	public List<DeviceInstance> getInstanceInUse();

	//通过机柜id获取所有的ip
	public List<InstanceIp> getInstanceIpByChestId(@Param("chestId")int chestId);

	//添加自动续费的配置
	public void addAutoRenewConf(AutoRenewConf conf);

	//更新
	public void updateInstanceByAnyting(DeviceInstance deviceInstance);
	
	//查询自动续费配置
	public AutoRenewConf getAutoRenewConfByInstId(int instanceId);

	public void updateAutoRenewConf(AutoRenewConf conf);

	//删除
	public void deleteAutoRenewConf(@Param("instanceId")int instanceId);


}
