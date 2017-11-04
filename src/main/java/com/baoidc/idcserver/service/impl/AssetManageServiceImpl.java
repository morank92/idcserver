package com.baoidc.idcserver.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baoid.idcserver.vo.IpRangeUseVO;
import com.baoid.idcserver.vo.ServerPort;
import com.baoidc.idcserver.core.OrderType;
import com.baoidc.idcserver.core.ResponseMessage;
import com.baoidc.idcserver.core.SourceTag;
import com.baoidc.idcserver.dao.AssetManageDAO;
import com.baoidc.idcserver.dao.DeviceServerDAO;
import com.baoidc.idcserver.po.Chest;
import com.baoidc.idcserver.po.ChestIpRange;
import com.baoidc.idcserver.po.Disk;
import com.baoidc.idcserver.po.Gateway;
import com.baoidc.idcserver.po.InstanceIp;
import com.baoidc.idcserver.po.MemoryBank;
import com.baoidc.idcserver.po.Order;
import com.baoidc.idcserver.po.OrderParam;
import com.baoidc.idcserver.po.Servers;
import com.baoidc.idcserver.po.Subnetmask;
import com.baoidc.idcserver.service.AssetManageService;
@Service
public class AssetManageServiceImpl implements AssetManageService {

	@Autowired
	private AssetManageDAO assetManageDAO;
	@Autowired
	private DeviceServerDAO deviceServerDao;
	
	//内存条
	public List<MemoryBank> getMemoryBank(String inHostRoom) {
		return assetManageDAO.getMemoryBank(inHostRoom);
	}

	public void addMemory(String memoryType, int num,String inHostRoom) {
		assetManageDAO.addMemory(memoryType, num, inHostRoom);
	}

	public int checkMemoryExist(String memoryType,String inHostRoom) {
		return assetManageDAO.checkMemoryExist(memoryType,inHostRoom);
	}

	//修改内存条的可用数
	public void updateMemoryTypeUsable(int memoryId,int num){
		assetManageDAO.updateMemoryTypeUsable(memoryId, num);
	}
			
	//删除某一类型的内存条
	public void deleteMemoryType(int memoryId){
		assetManageDAO.deleteMemoryType(memoryId);
	}

	//硬盘
	public List<Disk> getDisk(String inHostRoom) {
		return assetManageDAO.getDisk(inHostRoom);
	}

	public void addDisk(String diskType, String diskSize, int num,String inHostRoom) {
		assetManageDAO.addDisk(diskType, diskSize, num, inHostRoom);
	}

	public void updateDiskUsable(int diskId, int num) {
		assetManageDAO.updateDiskUsable(diskId, num);
	}
	public int checkDiskExist(String diskType,String diskSize,String inHostRoom){
		return assetManageDAO.checkDiskExist(diskType, diskSize,inHostRoom);
	}
	//删除该类硬盘
	public void deleteDiskType(int diskId){
		assetManageDAO.deleteDiskType(diskId);
	}

	//机柜
	public List<Chest> getChest(Chest chest) {
		return assetManageDAO.getChest(chest);
	}
	//删除ip库中机柜的IP
	public void deleteChestIp(int chestId) {
		assetManageDAO.deleteChestIp(chestId);
	}
	
	//判断机柜是否可以删除
	public int checkChestCanDelete(int chestId) {
		return assetManageDAO.checkChestCanDelete(chestId);
	}

	public void addChest(Chest chest) {
		assetManageDAO.addChest(chest);
	}
	
	public void addIpToChest(List<InstanceIp> ipList,int chestId){
		if(ipList.size()!=0){
			assetManageDAO.addIpToChest(ipList,chestId);
		}
	}

	public void deleteChest(int id) {
		assetManageDAO.deleteChest(id);
	}

	public void updateChest(Chest chest) {
		assetManageDAO.updateChest(chest);
	}
	//给机柜绑定开始   结束  ip段
	public void addRangeIpToChest(List<ChestIpRange> chestIpRangeList,
			int chestId) {
		if(chestIpRangeList.size()!=0){
			assetManageDAO.addRangeIpToChest(chestIpRangeList,chestId);
		}
	}
	//删除机柜关联的范围IP
		public void deleteChestIpRange(int chestId) {
			assetManageDAO.deleteChestIpRange(chestId);
		}
	//判断机柜的IP段是否可以修改
	public List<IpRangeUseVO> checkIpRangeUse(int chestId) {
		return assetManageDAO.checkIpRangeUse(chestId);
	}
	//匹配删除ip段
		public void deleteChestIpRangeExist(List<ChestIpRange> chestIpRangeList,
					int chestId){
			for (ChestIpRange chestIpRange : chestIpRangeList) {
				assetManageDAO.deleteChestIpRangeExist(chestIpRange,chestId);
			}
		}
	//匹配删除机柜上的ip
	public void deleteChestIpListExist(List<InstanceIp> ipList, int chestId){
		for (InstanceIp instanceIp : ipList) {
			assetManageDAO.deleteChestIpListExist(instanceIp,chestId);
		}
	}
	//机柜添加网关
	public void addGatewayToChest(List<Gateway> gatewayList, int chestId){
		assetManageDAO.addGatewayToChest(gatewayList,chestId);
	}
	//机柜添加子网掩码
	public void addSubnetmaskToChest(List<Subnetmask> subnetmaskList,
			int chestId){
		assetManageDAO.addSubnetmaskToChest(subnetmaskList,chestId);
	}
	//删除机柜网关
	public void deleteGatewayByChestId(int chestId){
		assetManageDAO.deleteGatewayByChestId(chestId);
	}
	//删除机柜子网掩码
	public void deleteSubnetmaskByChestId(int chestId){
		assetManageDAO.deleteSubnetmaskByChestId(chestId);
	}

	//服务器
	public List<Servers> getServers(Servers servers) {
		return assetManageDAO.getServers(servers);
	}

	public void addServers(Servers servers) {
		servers.setShowMemory(transformShow(servers.getShowMemory()));
		servers.setShowDisk(transformShow(servers.getShowDisk()));
		assetManageDAO.addServers(servers);
		List<ServerPort> portList = servers.getPortList();
		if(portList.size()!=0){
			assetManageDAO.addServerPort(portList, servers.getServerId());
		}
	}
	//判断交换机端口号是否被占用
	public int checkPortUsed(int chestId,String portNum){
		return assetManageDAO.checkPortUsed(chestId, portNum);
	}
	//将页面传过来的   内存4G+8G+4G  转换成 4G*2+8G*1的形式   硬盘同样如此
	public String transformShow(String show){
		Map<String,Integer> map = new HashMap<String, Integer>();
		String[] split = show.split("\\+");
		for (String string : split) {
			if(map.containsKey(string)){
				map.put(string, map.get(string)+1);
			}else{
				map.put(string, 1);
			}
		}
		Set<String> keySet = map.keySet();
		String newShow = "";
		int i = 0;
		for (String string : keySet) {
			if(i!=0){
				newShow += "+";
			}
			newShow = newShow+string+"*"+map.get(string);
			i++;
		}
		return newShow;
	}
	public void addIpToServers(List<InstanceIp> ipList,int serverId){
		for (InstanceIp instanceIp : ipList) {
			assetManageDAO.addIpToServers(instanceIp.getIpId(), serverId,1);
		}
	}
	public void updateServers(Servers servers) {
		servers.setShowMemory(transformShow(servers.getShowMemory()));
		servers.setShowDisk(transformShow(servers.getShowDisk()));
		assetManageDAO.updateServers(servers);
		List<ServerPort> portList = servers.getPortList();
		if(portList !=null  && portList.size()!=0 ){
			assetManageDAO.addServerPort(portList, servers.getServerId());
		}
	}
	public void deleteServerPort(int id){
		assetManageDAO.deleteServerPort(id);
	}

	public void deleteServers(int id) {
		assetManageDAO.deleteServers(id);
		assetManageDAO.deleteServerPort(id);
	}

	//从现有资产中匹配订单的配置设备
	public Map<String, Object> getMatchServers4Order(Order order) {
		Map<String, Object> map = new HashMap<String, Object>();
		//通过订单取匹配查询所需要的server
		int orderType = Integer.parseInt(order.getOrderType());//订单的类型
		List<OrderParam> paramList = order.getOrderParamList();//订单所需要配置的参数
		Servers sv = new Servers();
		
		//获取备用匹配信息
		int ouserId = order.getUserId();;//资产所属者
		String room = "";//机房
		String model = "";//型号
		String disk = "";//硬盘
		String ram = "";//内存 
		String width = "";//带宽
		List<String> ttlList = new ArrayList<String>();//线路集合
		int ipCount = 1;//ip个数
		int needCount = 1;//数量
		for(int i=0;i<paramList.size();i++){//订单的参数
			OrderParam param = paramList.get(i);
			String tag = param.getTagValue();
			String val = param.getParamValue();
			if(SourceTag.ROOM.getTagVal().equals(tag)){//为机房
				room = val;
			}
			if(SourceTag.WIDTH.getTagVal().equals(tag)){//为带宽
				width = val;
			}
			if(SourceTag.MODEL.getTagVal().equals(tag)){//为型号
				model = val;
			}
			if(SourceTag.DISK.getTagVal().equals(tag)){//为硬盘:有可能为多块，以+号分割
				String[] diskStr = val.split("\\+");
				int diskvalCount = 0;//总共大小
				int diskvalInt = 0;//单个大小
				for(int j=0;j<diskStr.length;j++){//判断每一块硬盘单位，转换为GB
					String diskval = diskStr[j];
					String unit = diskval.substring(diskval.length()-1, diskval.length());//单位
					if(unit.equals("T")){//为sata硬盘，需要转换为GB类型
						diskvalInt = Integer.parseInt(diskval.substring(0, diskval.length()-1)) * 1024;
					}else{//为ssd 不需要*1024,只需要去掉单位
						diskvalInt = Integer.parseInt(diskval.substring(0, diskval.length()-2));
					}
					diskvalCount += diskvalInt;//总共大小
				}
				disk = diskvalCount + "GB";
			}
			if(SourceTag.RAM.getTagVal().equals(tag)){//为内存
				ram = val;
				
			}
			if(SourceTag.TTL.getTagVal().equals(tag)){//为线路
				String[] ttl = val.split(" ");//以空格分隔
				for(int y=0;y<ttl.length;y++){
					ttlList.add(ttl[y]);//线路集合
				}
			}
			if(SourceTag.IPCOUNT.getTagVal().equals(tag)){//为ip个数
				ipCount = Integer.parseInt(val);
				
			}
			if(SourceTag.COUNT.getTagVal().equals(tag)){//为数量
				needCount = Integer.parseInt(val);
			}
		}
		if(OrderType.SERVER_RENT.getOrderType() == orderType){//服务器租用
			sv.setInHostRoom(room);
			sv.setServerDetail(model);
			sv.setDiskSize(disk);
			sv.setBandWidth(width);
			sv.setMemorySize(ram);
			
			
		}
		if(OrderType.SERVER_TRUST.getOrderType() == orderType){//服务器托管
			sv.setOwnUserId(ouserId);
		}
		
		sv.setUseStatus(0);//未被使用的
		List<Servers> servers = assetManageDAO.getMatchServers(sv);
		List<Servers> newServers = new ArrayList<Servers>();//装匹配正确的设备
		
		for(int i=0;i<servers.size();i++){//判断每一个设备中的ip集合中的属性、
			
			List<InstanceIp> newIplist = new ArrayList<InstanceIp>();//最后匹配到的ip集合
			
			List<String> serverttls = new ArrayList<String>();//当前设备上的线路集合
			boolean f = false;//线路是否符合所需的标识
			
			Servers server = servers.get(i);
			List<InstanceIp> ipList = server.getIpList();//设备里面的ip
			if(ipList != null){//设备中有ip
				//获取该设备上的线路集合
				for(int j=0;j<ipList.size();j++){
					InstanceIp ip = ipList.get(j);
					if(!serverttls.contains(ip.getIpBusi())){
						serverttls.add(ip.getIpBusi());//线路--已经去除了重复的，
					}
				}
				//判断线路
				if(ttlList.size() == serverttls.size()){
					for(int j=0;j<ttlList.size();j++){
						if(serverttls.contains(ttlList.get(j))){
							f = true;
						}
					}
				}
				
				//获取判断该设备中ip集合的正确性
				for(int j=0;j<ipList.size();j++){
					InstanceIp ip = ipList.get(j);	
					if(ttlList.contains(ip.getIpBusi())){//为空闲，可用的ip且线路正确
						newIplist.add(ip);//匹配到的ip集合
					}
				}
				//判断这个设备所拥有的ip空闲的个数并且线路的正确性
				if(newIplist.size() == ipCount && f){//即成立
					newServers.add(server);//匹配正确的设备
				}
			}
		}
		//正确匹配到的设备数量与订单的数量一致或大于
		if(newServers.size() >= needCount){
			map.put("list", newServers);
		}else{
			servers.clear();//不满足条件
			map.put("list", servers);
		}
		return map;
	}
	
	//根据订单需求匹配机柜
	public List<Chest> getMatchRack4Order(Order order) {
		List<OrderParam> paramList = order.getOrderParamList();//订单所需要配置的参数
		Chest sv = new Chest();
		List<String> ttlList = new ArrayList<String>();//线路集合
		String room = "";//机房
		String size = "";//规格
		int ipCount = 1;//ip个数
		int needCount = 1;//数量
		for(int i=0;i<paramList.size();i++){//订单的参数,即为查询的条件参数
			OrderParam param = paramList.get(i);
			String tag = param.getTagValue();
			String val = param.getParamValue();
			if(SourceTag.ROOM.getTagVal().equals(tag)){//为机房
				room = val;
			}
			if(SourceTag.SIZE.getTagVal().equals(tag)){//为机房
				size = val;
			}
			if(SourceTag.TTL.getTagVal().equals(tag)){//为线路
				String[] ttl = val.split(" ");//以空格分隔
				for(int y=0;y<ttl.length;y++){
					ttlList.add(ttl[y]);//线路集合
				}
			}
			if(SourceTag.IPCOUNT.getTagVal().equals(tag)){//为ip个数
				ipCount = Integer.parseInt(val);
				
			}
			if(SourceTag.COUNT.getTagVal().equals(tag)){//为数量
				needCount = Integer.parseInt(val);
			}
		}
		sv.setHostRoom(room);
		sv.setChestType(size);//机柜的规格：高度
		sv.setUseStatus(0);//未被使用的
		//sv.setServerNumber("");//设备编号---由于sql那边需要判断
		List<Chest> chests = assetManageDAO.getMatchChests(sv);
		List<Chest> newChests = new ArrayList<Chest>();//装匹配正确的设备
		for(int i=0;i<chests.size();i++){//判断每一个设备中的ip属性
			List<InstanceIp> newIplist = new ArrayList<InstanceIp>();//匹配到的ip集合
			
			List<String> serverttls = new ArrayList<String>();//当前设备上的线路集合
			boolean f = false;//线路是否符合所需的标识
			
			Chest chest = chests.get(i);
			
			
			List<InstanceIp> ipList = chest.getIpList();//设备里面的ip
			if(ipList != null){//设备中有ip
	
				//获取该设备上的线路集合
				for(int j=0;j<ipList.size();j++){
					InstanceIp ip = ipList.get(j);
					if(!serverttls.contains(ip.getIpBusi())){
						serverttls.add(ip.getIpBusi());//线路--已经去除了重复的，
					}
				}
				//判断线路
				if(ttlList.size() == serverttls.size()){
					for(int j=0;j<ttlList.size();j++){
						if(serverttls.contains(ttlList.get(j))){
							f = true;
						}
					}
				}
	
	
				for(int j=0;j<ipList.size();j++){
					InstanceIp ip = ipList.get(j);
					if(ttlList.contains(ip.getIpBusi())){//为空闲，可用的ip且线路正确
						newIplist.add(ip);//匹配到的ip集合
					}
				}
				//判断这个设备所拥有的ip空闲的个数并且线路的正确性
				if(newIplist.size() == ipCount && f){//即成立
					newChests.add(chest);//匹配正确的设备
				}
			}
		}
		//正确匹配到的设备数量与订单的数量一致或大于
		if(newChests.size() >= needCount){
			chests = newChests;
		}else{
			chests.clear();//不满足条件
		}
		return chests;
	}
	

	public List<InstanceIp> getAllNoUseIp(int chestId) {
		return assetManageDAO.getAllNoUseIp(chestId);
	}
	//移出服务器上的ip
	public void removeServerIp(int ipId) {
		assetManageDAO.removeServerIp(ipId);
	}
	//删除服务器
	public void deleteServer(int serverId) {
		assetManageDAO.deleteServer(serverId);
	}
	//更新和服务器关联的ip
	public void deleteServerIp(int serverId) {
		assetManageDAO.deleteServerIp(serverId);
	}
	
	

	public Order getSingleOrderByOrderId(String orderId) {
		//获取该订单、
		Order morder = deviceServerDao.getSingleOrderByOrderId(orderId);
		return morder;
	}
	
	//判断机柜编号是否存在
	public int checkChestNumberExist(String chestNumber){
		return assetManageDAO.checkChestNumberExist(chestNumber);
	}
	//判断IP是否被使用
	public int checkIpUse(int instanceId){
		return assetManageDAO.checkIpUse(instanceId);
	}
	//根据机柜ID查询机柜
	public Chest getChestByChestId(int chestId){
		return assetManageDAO.getChestByChestId(chestId);
	}

}
