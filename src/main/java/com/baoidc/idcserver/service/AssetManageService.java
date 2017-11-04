package com.baoidc.idcserver.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baoid.idcserver.vo.IpRangeUseVO;
import com.baoid.idcserver.vo.ServerPort;
import com.baoidc.idcserver.core.ResponseMessage;
import com.baoidc.idcserver.po.Chest;
import com.baoidc.idcserver.po.ChestIpRange;
import com.baoidc.idcserver.po.Disk;
import com.baoidc.idcserver.po.Gateway;
import com.baoidc.idcserver.po.InstanceIp;
import com.baoidc.idcserver.po.MemoryBank;
import com.baoidc.idcserver.po.Order;
import com.baoidc.idcserver.po.Servers;
import com.baoidc.idcserver.po.Subnetmask;

public interface AssetManageService {
	
	//内存条
	//查询内存条库存情况
	public List<MemoryBank> getMemoryBank(String inHostRoom);
	
	//添加内存条
	public void addMemory(String memoryType,int num,String inHostRoom);
	
	//判断某一大小的内存条是否存在
	public int checkMemoryExist(String memoryType,String inHostRoom);
	
	//修改内存条的可用数
	public void updateMemoryTypeUsable(int memoryId,int num);
		
	//删除某一类型的内存条
	public void deleteMemoryType(int memoryId);

	
	//硬盘
	//查询硬盘库存情况
	public List<Disk> getDisk(String inHostRoom);
	//添加硬盘  
	public void addDisk(String diskType,String diskSize,int num,String inHostRoom);
	//修改硬盘的可用数
	public void updateDiskUsable(int diskId,int num);
	//检查某一类型的硬盘  大小是否存在
	public int checkDiskExist(String diskType,String diskSize,String inHostRoom);
	//删除该类硬盘
	public void deleteDiskType(int diskId);
	
	//机柜
	//查询所有机柜以及机柜状态
	public List<Chest> getChest(Chest chest);
	//添加机柜
	public void addChest(Chest chest);
	//删除机柜
	public void deleteChest(int id);
	//修改机柜
	public void updateChest(Chest chest);
	//给机柜添加ip
	public void addIpToChest(List<InstanceIp> ipList,int chestId);
	public void deleteChestIp(int chestId);
	public void deleteChestIpRange(int chestId);
	//判断机柜的IP段是否可以修改
	public List<IpRangeUseVO> checkIpRangeUse(int chestId);
	//匹配删除ip段
	public void deleteChestIpRangeExist(List<ChestIpRange> chestIpRangeList,
				int chestId);
	//匹配删除机柜上的ip
	public void deleteChestIpListExist(List<InstanceIp> ipList, int chestId);
	
	//机柜添加网关
	public void addGatewayToChest(List<Gateway> gatewayList, int chestId);
	//机柜添加子网掩码
	public void addSubnetmaskToChest(List<Subnetmask> subnetmaskList,
			int chestId);
	
	//服务器
	//查询所有服务器以及服务器的状态
	public List<Servers> getServers(Servers servers);
	//添加服务器
	public void addServers(Servers servers);
	//删除服务器
	public void deleteServers(int id);
	//修改服务器
	public void updateServers(Servers servers);
	//给服务器添加IP
	public void addIpToServers(List<InstanceIp> ipList,int serverId);

	//查询所有能匹配订单的服务器及当前信息
	public Map<String, Object> getMatchServers4Order(Order order);

	//查询机柜下未使用的ip
	public List<InstanceIp> getAllNoUseIp(int chestId);
	
	//移除服务器上的IP
	public void removeServerIp(int ipId);

	//获取单个order
	public Order getSingleOrderByOrderId(String orderId);

	public int checkChestCanDelete(int chestId);

	public void deleteServer(int serverId);

	public void deleteServerIp(int serverId);

	public void addRangeIpToChest(List<ChestIpRange> chestIpRangeList,
			int chestId);
	
	//匹配机柜
	public List<Chest> getMatchRack4Order(Order order);
	
	//判断交换机端口号是否被占用
	public int checkPortUsed(int chestId,String portNum);

	public void deleteServerPort(int serverId);
	
	//删除机柜网关
	public void deleteGatewayByChestId(int chestId);
	//删除机柜子网掩码
	public void deleteSubnetmaskByChestId(int chestId);
	//判断机柜编号是否存在
	public int checkChestNumberExist(String chestNumber);
	//判断IP是否被使用
	public int checkIpUse(int instanceId);

	//根据机柜ID查询机柜
	public Chest getChestByChestId(int chestId);
	
	
}
