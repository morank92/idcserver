package com.baoidc.idcserver.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baoid.idcserver.vo.CreateDeviceInstanceVO;
import com.baoid.idcserver.vo.DeviceInstanceVO;
import com.baoid.idcserver.vo.ServerRentVo;
import com.baoidc.idcserver.core.ResponseMessage;
import com.baoidc.idcserver.po.AutoRenewConf;
import com.baoidc.idcserver.po.DeviceInstance;
import com.baoidc.idcserver.po.Order;
import com.baoidc.idcserver.po.Servers;
import com.baoidc.idcserver.vo.query.InstanceQuery;
import com.baoidc.idcserver.vo.query.RenewVO;

public interface IDeviceInstanceService {
	
	//获取用户的所有产品实例by userId
	public HashMap<String, List<DeviceInstance>> getInstanceListByUserId(int userId);

	//获取所有产品实例by deviceInstance
	public List<DeviceInstance> getInstanceList(
			DeviceInstance deviceInstance);

	//获取单个实例by instanceId
	public DeviceInstance getInstanceByInstanceId(int instanceId);
	
	public void generationInstance(CreateDeviceInstanceVO createInstanceVO);
	
	public void updateInstance(String orderId);

	//创建实例续费的订单
	public List<Order> submitRenewOrder(RenewVO renewvo);
	
	//获取所有产品实例by deviceInstance
	public Map<String, List<DeviceInstance>> getPreRecycleInstanceList();
	
	public void assetRecycle(int instanceId);
	
	public boolean renewInstance(String orderId);
	
	//条件查询实例，如：ip地址
	public List<DeviceInstance> queryInstance(InstanceQuery instanceQuery);

	//处理升级订单，获取设备信息
	public Servers getDeviceByInstanceId4Order(int instanceId);

	public boolean payProductOrderList(List<Order> orderList);

	//查询一个机房下的所有实例
	public Map<String, Map<String, Integer>> getAllInstanceByHostRoom(
			String hostRoom,int userId)throws Exception;

	//获取当个服务器信息
	public Servers getServerByServerId4UpOrder(int serverId);

	 //从数据库获取实例，保证数据同步统一性，insList：页面现有的实例集合
	public List<DeviceInstance> getSourceInstance2db(List<DeviceInstance> instanceList);
	
	public List<DeviceInstance> getInstanceInUse();
	
	//更新实例信息，不更新实例参数及相关订单信息
	public void updateInstanceSingle(DeviceInstance deviceInstance);

	//获取单个实例对象
	public DeviceInstance getSingleInstance(DeviceInstance deviceInstance);

	//自动续费，提交，生成配置
	public void submitAutoRenew(List<DeviceInstance> instanceList);

	//更新
	public void updateInstanceByAnyting(DeviceInstance deviceInstance);
	
	public AutoRenewConf getAutoRenewConf(int instanceId);

	public void updateAutoRenewConf(AutoRenewConf conf);

	//删除
	public void deleteAutoRenewConf(int instanceId);

	
}
