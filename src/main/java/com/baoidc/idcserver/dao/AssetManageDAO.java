package com.baoidc.idcserver.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baoid.idcserver.vo.IpRangeUseVO;
import com.baoid.idcserver.vo.ServerPort;
import com.baoidc.idcserver.po.Chest;
import com.baoidc.idcserver.po.ChestIpRange;
import com.baoidc.idcserver.po.Disk;
import com.baoidc.idcserver.po.Gateway;
import com.baoidc.idcserver.po.InstanceIp;
import com.baoidc.idcserver.po.MemoryBank;
import com.baoidc.idcserver.po.Servers;
import com.baoidc.idcserver.po.Subnetmask;

public interface AssetManageDAO {
	//内存条
	//查询内存条库存情况
	public List<MemoryBank> getMemoryBank(String inHostRoom);
	
	//添加内存条  
	public void addMemory(@Param("memoryType") String memoryType,@Param("num") int num,@Param("inHostRoom")String inHostRoom);
	
	//判断某一大小的内存条是否存在
	public int checkMemoryExist(@Param("memoryType")String memoryType,@Param("inHostRoom")String inHostRoom);
	
	//修改内存条的可用数
	public void updateMemoryTypeUsable(@Param("memoryId")int memoryId,@Param("num")int num);
	
	//删除某一类型的内存条
	public void deleteMemoryType(int memoryId);
	
	//硬盘
	//查询硬盘库存情况
	public List<Disk> getDisk(String inHostRoom);
	
	//判断某一大小的内存条是否存在
	public int checkDiskExist(@Param("diskType") String diskType,@Param("diskSize") String diskSize,@Param("inHostRoom")String inHostRoom);
	
	//添加硬盘   
	public void addDisk(@Param("diskType") String diskType,@Param("diskSize") String diskSize,@Param("num") int num,@Param("inHostRoom")String inHostRoom);
	
	//修改硬盘可用量
	public void updateDiskUsable(@Param("diskId")int diskId,@Param("num") int num);
	
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
	//给机柜添加IP
	public void addIpToChest(@Param("ipList")List<InstanceIp> ipList,@Param("chestId") int chestId);
	//判断机柜是否可以删除
	public int checkChestCanDelete(int chestId);
	//删除ip库中机柜的IP
	public void deleteChestIp(int chestId);
	//给机柜绑定开始   结束  ip段
		public void addRangeIpToChest(@Param("chestIpRangeList")List<ChestIpRange> chestIpRangeList,
				@Param("chestId")int chestId);
	//删除机柜关联的范围IP
	public void deleteChestIpRange(int chestId);
	//判断机柜的IP段是否可以修改
	public List<IpRangeUseVO> checkIpRangeUse(int chestId);
	//匹配删除ip段
	public void deleteChestIpRangeExist(@Param("chestIpRange")ChestIpRange chestIpRange,@Param("chestId")int chestId);
	//匹配删除机柜上的ip
	public void deleteChestIpListExist(@Param("instanceIp")InstanceIp instanceIp,@Param("chestId")int chestId);
	//机柜添加网关
	public void addGatewayToChest(@Param("gatewayList")List<Gateway> gatewayList,@Param("chestId") int chestId);
	//机柜添加子网掩码
	public void addSubnetmaskToChest(@Param("subnetmaskList")List<Subnetmask> subnetmaskList,
			@Param("chestId")int chestId);
	//删除机柜网关
	public void deleteGatewayByChestId(int chestId);
	//删除机柜子网掩码
	public void deleteSubnetmaskByChestId(int chestId);
	
	//服务器
	//查询所有服务器以及服务器的状态
	public List<Servers> getServers(Servers servers);
	//根据服务器编号 查询服务器
	public Servers getServersByServerNumber(String serverNumber);
	//添加服务器
	public void addServers(Servers servers);
	//删除服务器
	public void deleteServers(int id);
	//修改服务器
	public void updateServers(Servers servers);
	
	//给服务器绑定ip
	public void addIpToServers(@Param("ipId")int ipId,@Param("serverId")int serverId,@Param("ipStatus")int ipStatus);
	
	//查询所有服务器4订单
	public List<Servers> getMatchServers(Servers servers);

	//查询机柜下未使用的ip
	public List<InstanceIp> getAllNoUseIp(int chestId);
	
	//服务器回收
	public void recycleServer(Servers server);

	//by  id
	public Servers getServerByServerId(int serverId);

	//移除服务器上的IP
	public void removeServerIp(int ipId);
	//删除服务器
	public void deleteServer(int serverId);
	//更新和服务器关联的ip
	public void deleteServerIp(int serverId);
	
	//通过正在使用的用户id查询出旗下的设备
	public List<Servers> getServersByuserId(int userId);

	//通过机柜id查询此机柜
	public Chest getChestByChestId(int chestId);

	//根据订单参数匹配机柜
	public List<Chest> getMatchChests(Chest chest);
	
	//服务器添加交换机端口号
	public void addServerPort(@Param("portList")List<ServerPort> portList,@Param("serverId")int serverId);
	
	//判断交换机端口号是否被占用
	public int checkPortUsed(@Param("chestId")int chestId,@Param("portNum")String portNum);
	
	//删除服务器绑定的交换机端口
	public void deleteServerPort(int serverId);
	
	//判断机柜编号是否存在
	public int checkChestNumberExist(String chestNumber);
	//判断IP是否被使用
	public int checkIpUse(int instanceId);
	
	//根据机柜ID删除服务器
	public void delServerByChestId(int chestId);
	//根据机柜ID删除服务器上的交换机端口
	public void delPortByChestId(int chestId);
	//根据机柜ID更新机柜上的IP为闲置状态
	public void updateIpUnUsedByChestId(int chestId);
}
