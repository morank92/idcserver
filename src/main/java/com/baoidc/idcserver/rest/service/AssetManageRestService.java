package com.baoidc.idcserver.rest.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.baoid.idcserver.vo.IpRangeUseVO;
import com.baoid.idcserver.vo.ServerPort;
import com.baoidc.idcserver.core.DateUtil;
import com.baoidc.idcserver.core.ErrorCode;
import com.baoidc.idcserver.core.OrderType;
import com.baoidc.idcserver.core.ResponseMessage;
import com.baoidc.idcserver.core.ServerException;
import com.baoidc.idcserver.core.aspect.ControllerLog;
import com.baoidc.idcserver.po.Chest;
import com.baoidc.idcserver.po.ChestIpRange;
import com.baoidc.idcserver.po.Disk;
import com.baoidc.idcserver.po.InstanceIp;
import com.baoidc.idcserver.po.MemoryBank;
import com.baoidc.idcserver.po.Order;
import com.baoidc.idcserver.po.Servers;
import com.baoidc.idcserver.po.User;
import com.baoidc.idcserver.service.AssetManageService;

/*
 * 资产管理接口
 */

@Component
@Path("/assetManage")
public class AssetManageRestService {

	@Autowired
	private AssetManageService assetManageService;
	@Autowired
	private DataSourceTransactionManager tx;
	@Autowired
	private DefaultTransactionDefinition def;
	
	
	// 内存条
	/*
	 * 根据内存条型号 查询所有的内存条
	 */
	@GET
	@Path("/getAllMemoryByType")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Transactional
	public Map<String, List<MemoryBank>> getAllMemoryByType(
			@QueryParam("inHostRoom") String inHostRoom) {
		Map<String, List<MemoryBank>> map = new HashMap<String, List<MemoryBank>>();
		List<MemoryBank> list = assetManageService.getMemoryBank(inHostRoom);
		map.put("aaData", list);
		return map;
	}

	/*
	 * 添加内存条
	 */
	@GET
	@Path("/addMemory")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Transactional
	public ResponseMessage addMemory(
			@QueryParam("memoryType") String memoryType,
			@QueryParam("num") int num, @QueryParam("inHostRoom") String inHostRoom) {
		ResponseMessage rm = new ResponseMessage();
		try {
			int i = assetManageService.checkMemoryExist(memoryType, inHostRoom);
			if (i != 0) {
				rm.setCode("500");
				rm.setMsg("该类型已经存在，请前往修改可用数即可");
			} else {
				assetManageService.addMemory(memoryType, num, inHostRoom);
				rm.setCode("200");
				rm.setMsg("添加成功");
			}
			return rm;
		} catch (Exception e) {
			e.printStackTrace();
			rm.setCode("500");
			rm.setMsg("网络异常");
			return rm;
		}
	}

	/*
	 * 修改内存可用数
	 */
	@GET
	@Path("/updateMemoryTypeUsable")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Transactional
	public ResponseMessage updateMemoryTypeUsable(
			@QueryParam("memoryId") int memoryId, @QueryParam("num") int num) {
		ResponseMessage rm = new ResponseMessage();
		try {
			assetManageService.updateMemoryTypeUsable(memoryId, num);
			rm.setCode("200");
			rm.setMsg("修改成功");
			return rm;
		} catch (Exception e) {
			e.printStackTrace();
			rm.setCode("500");
			rm.setMsg("网络异常，修改失败");
			return rm;
		}
	}

	/*
	 * 删除某类型的内存条
	 */
	@GET
	@Path("/deleteMemoryType")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Transactional
	public ResponseMessage deleteMemoryType(@QueryParam("memoryId") int memoryId) {
		ResponseMessage rm = new ResponseMessage();
		try {
			assetManageService.deleteMemoryType(memoryId);
			rm.setCode("200");
			rm.setMsg("删除成功");
			return rm;
		} catch (Exception e) {
			e.printStackTrace();
			rm.setCode("500");
			rm.setMsg("网络异常，删除失败");
			return rm;
		}
	}

	// 硬盘
	/*
	 * 根据硬盘型号 大小来获取所有的硬盘信息
	 */
	@GET
	@Path("/getAllDiskByTypeSize")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Transactional
	public Map<String, List<Disk>> getAllDiskByTypeSize(
			@QueryParam("inHostRoom") String inHostRoom) {
		Map<String, List<Disk>> map = new HashMap<String, List<Disk>>();
		List<Disk> list = assetManageService.getDisk(inHostRoom);
		map.put("aaData", list);
		return map;
	}

	/*
	 * 添加硬盘
	 */
	@GET
	@Path("/addDisk")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Transactional
	public ResponseMessage addDisk(@QueryParam("diskType") String diskType,
			@QueryParam("diskSize") String diskSize,
			@QueryParam("num") int num, @QueryParam("inHostRoom") String inHostRoom) {
		ResponseMessage rm = new ResponseMessage();
		try {
			int flag = assetManageService.checkDiskExist(diskType, diskSize,
					inHostRoom);
			if (flag == 1) {
				rm.setCode("500");
				rm.setMsg("该类型已经存在，请前往修改可用数即可");
			} else {
				assetManageService.addDisk(diskType, diskSize, num, inHostRoom);
				rm.setCode("200");
				rm.setMsg("添加成功");
			}
			return rm;
		} catch (Exception e) {
			e.printStackTrace();
			rm.setCode("500");
			rm.setMsg("网络异常，添加失败");
			return rm;
		}
	}

	/*
	 * 修改硬盘可用数
	 */
	@GET
	@Path("/updateDiskUsable")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Transactional
	public ResponseMessage updateDiskUsable(@QueryParam("diskId") int diskId,
			@QueryParam("num") int num) {
		ResponseMessage rm = new ResponseMessage();
		try {
			assetManageService.updateDiskUsable(diskId, num);
			rm.setCode("200");
			rm.setMsg("修改成功");
			return rm;
		} catch (Exception e) {
			e.printStackTrace();
			rm.setCode("500");
			rm.setMsg("网络异常，修改失败");
			return rm;
		}
	}

	/*
	 * 删除某类硬盘
	 */
	@GET
	@Path("/deletDiskType")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Transactional
	public ResponseMessage deletDiskType(@QueryParam("diskId") int diskId) {
		ResponseMessage rm = new ResponseMessage();
		try {
			assetManageService.deleteDiskType(diskId);
			rm.setCode("200");
			rm.setMsg("删除成功");
			return rm;
		} catch (Exception e) {
			e.printStackTrace();
			rm.setCode("500");
			rm.setMsg("网络异常");
			return rm;
		}
	}

	//机柜
	/*
	 * 根据获取所有机柜
	 */
	@POST
	@Path("/getAllChest")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Transactional
	public ResponseMessage getAllChest(Chest chest,@Context HttpServletRequest request) {
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
			return rm;
		}else{
			rm = new ResponseMessage();
			List<Chest> list = assetManageService.getChest(chest);
			rm.setCode("200");
			rm.setObj(list);
			return rm;
		}
	}

	/*
	 * 添加机柜
	 */
	@POST
	@Path("/addChest")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Transactional
	@ControllerLog
	public ResponseMessage addChest(Chest chest,@Context HttpServletRequest request) {
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		StringBuilder busiDesc = new StringBuilder();
		if(rm!=null){
			return rm;
		}else{
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务  
			TransactionStatus txStatus = tx.getTransaction(def);// 获得事务状态 
			try {
				rm = new ResponseMessage();
				//判断机柜编号是否存在
				int count = assetManageService.checkChestNumberExist(chest.getChestNumber());
				if(count!=0){
					rm.setCode("500");
					rm.setMsg("机柜编号已经存在");
					tx.rollback(txStatus);
					return rm;
				}
				chest.setCreateTime(DateUtil.dateToStr(new Date(),
						"yyyy-MM-dd HH:mm:ss"));
				assetManageService.addChest(chest);
				if (chest.getIpList().size() > 0) {
					assetManageService.addIpToChest(chest.getIpList(),
							chest.getChestId());
					assetManageService.addRangeIpToChest(
							chest.getChestIpRangeList(), chest.getChestId());
				}
				if(chest.getGatewayList().size()>0){
					//机柜添加网关
					assetManageService.addGatewayToChest(chest.getGatewayList(),chest.getChestId());
				}
				if(chest.getSubnetmaskList().size()>0){
					//机柜添加子网掩码
					assetManageService.addSubnetmaskToChest(chest.getSubnetmaskList(),chest.getChestId());
				}
				rm.setCode("200");
				rm.setMsg("添加成功");
				//添加日志说明
				busiDesc.append("添加机柜成功，机柜ID：").append(chest.getChestId()).append("，机柜编号：").append(chest.getChestNumber());
				rm.setBusinessDesc(busiDesc.toString());
				tx.commit(txStatus);
				return rm;
			} catch (Exception e) {
				e.printStackTrace();
				rm = new ResponseMessage(ErrorCode.EXCEPTION);
				tx.rollback(txStatus);
				return rm;
			}
		}
	}

	/*
	 * 删除机柜
	 */
	@GET
	@Path("/deleteChest")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Transactional
	@ControllerLog
	public ResponseMessage deleteChest(@QueryParam("chestId") int chestId,@Context HttpServletRequest request) {
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		StringBuilder busiDesc = new StringBuilder();
		if(rm!=null){
			return rm;
		}else{
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务  
			TransactionStatus txStatus = tx.getTransaction(def);// 获得事务状态 
			try {
				rm = new ResponseMessage();
				int flag = assetManageService.checkChestCanDelete(chestId);
				if (flag != 0) {
					rm.setCode("500");
					rm.setMsg("机柜处于非空闲状态，不可删除");
					tx.rollback(txStatus);
				} else {
					assetManageService.deleteChest(chestId);
					// 删除ip库中机柜的IP
					assetManageService.deleteChestIp(chestId);
					// 删除机柜关联的范围IP
					assetManageService.deleteChestIpRange(chestId);
					//删除机柜网关
					assetManageService.deleteGatewayByChestId(chestId);
					//删除机柜子网掩码
					assetManageService.deleteSubnetmaskByChestId(chestId);
					rm.setCode("200");
					rm.setMsg("删除成功");
					busiDesc.append("删除机柜成功，机柜ID：").append(chestId);
					tx.commit(txStatus);
				}
				rm.setBusinessDesc(busiDesc.toString());
				return rm;
			} catch (Exception e) {
				e.printStackTrace();
				tx.rollback(txStatus);
				rm = new ResponseMessage(ErrorCode.EXCEPTION);
				return rm;
			}
		}
	}

	/*
	 * 判断机柜的IP段是否可以修改
	 */
	@GET
	@Path("/checkIpRangeUse")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Transactional
	public ResponseMessage checkIpRangeUse(@QueryParam("chestId") int chestId,@Context HttpServletRequest request) {
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
			return rm;
		}else{
			rm = new ResponseMessage();
			List<IpRangeUseVO> list = assetManageService
					.checkIpRangeUse(chestId);
			rm.setCode("200");
			rm.setObj(list);
			return rm;
		}
	}

	/*
	 * 修改机柜
	 */
	@POST
	@Path("/updateChest")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Transactional
	@ControllerLog
	public ResponseMessage updateChest(Chest chest,@Context HttpServletRequest request) {
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		StringBuilder busiDesc = new StringBuilder();
		if(rm!=null){
			return rm;
		}else{
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务  
			TransactionStatus txStatus = tx.getTransaction(def);// 获得事务状态 
			try {
				rm = new ResponseMessage();
				assetManageService.updateChest(chest);
				//删除机柜范围IP
				assetManageService.deleteChestIpRangeExist(
						chest.getChestIpRangeList(), chest.getChestId());
				//添加新的机柜范围IP
				if(chest.getChestIpRangeList().size()>0){
					assetManageService.addRangeIpToChest(chest.getChestIpRangeList(),
							chest.getChestId());
				}
				//删除机柜ip
				assetManageService.deleteChestIpListExist(chest.getIpList(),
						chest.getChestId());
				//添加新的机柜IP
				if(chest.getIpList().size()>0){
					assetManageService.addIpToChest(chest.getIpList(),
							chest.getChestId());
				}
				//删除机柜网关
				assetManageService.deleteGatewayByChestId(chest.getChestId());
				//添加新的网关
				if(chest.getGatewayList().size()>0){
					assetManageService.addGatewayToChest(chest.getGatewayList(), chest.getChestId());
				}
				//删除机柜子网掩码
				assetManageService.deleteSubnetmaskByChestId(chest.getChestId());
				//添加新的子网掩码
				if(chest.getSubnetmaskList().size()>0){
					assetManageService.addSubnetmaskToChest(chest.getSubnetmaskList(), chest.getChestId());
				}
				rm.setCode("200");
				rm.setMsg("修改成功");
				//添加操作日志
				busiDesc.append("修改机柜成功，机柜Id:").append(chest.getChestId()).append("，机柜编号：");
				if(chest.getChestNumber() ==  null || StringUtils.isBlank(chest.getChestNumber())){
					busiDesc.append("空");
				}else{
					busiDesc.append(chest.getChestNumber());
				}
				rm.setBusinessDesc(busiDesc.toString());
				tx.commit(txStatus);
				return rm;
			} catch (Exception e) {
				tx.rollback(txStatus);
				e.printStackTrace();
				rm = new ResponseMessage(ErrorCode.EXCEPTION);
				return rm;
			}
		}
	}

	/*
	 * 查询所有服务器及当前信息
	 */
	@POST
	@Path("/getAllServers")
	@Consumes({ MediaType.APPLICATION_JSON})
	@Produces({ MediaType.APPLICATION_JSON })
	@Transactional
	public ResponseMessage getAllServers(Servers servers,@Context HttpServletRequest request) {
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
			return rm;
		}else{
			rm = new ResponseMessage();
			List<Servers> list = assetManageService.getServers(servers);
			String instanceIp = servers.getIpList().get(0).getIpStr();
			List<Servers> newList = new ArrayList<Servers>();
			if(!instanceIp.equals("")){
				for (Servers servers2 : list) {
					List<InstanceIp> ipList = servers2.getIpList();
					for (InstanceIp instanceIp2 : ipList) {
						if(instanceIp2.getIpStr().indexOf(instanceIp)!=-1){
							newList.add(servers2);
						}
					}
				}
				rm.setCode("200");
				rm.setObj(newList);
				return rm;
			}else{
				rm.setCode("200");
				rm.setObj(list);
				return rm;
			}
				
		}
	}
	/*
	 * 查询机柜下的所有资产
	 */
	@POST
	@Path("/getServers4Chest")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Transactional
	public ResponseMessage getServers4Chest(Chest chest,@Context HttpServletRequest request) {
		Servers servers = new Servers();
		servers.setChest(chest);
		servers.setUseStatus(-1);//全部

		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
		  return rm;
		}else{
		  List<Servers> list = assetManageService.getServers(servers);
		  rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);//
		  rm.setData(list);
		  return rm;
		}
		
	}

	/*
	 * 查询所有能匹配订单的服务器及当前信息
	 */
	@GET
	@Path("/getMatchServers4Order")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Transactional
	public ResponseMessage getMatchServers4Order(
			@QueryParam("orderId") String orderId,@Context HttpServletRequest request) {
		ResponseMessage rm = (ResponseMessage)request.getAttribute("isLogin");
		if(rm != null){
			return rm;
		}else{
			Order order = assetManageService.getSingleOrderByOrderId(orderId);
			int orderType = Integer.parseInt(order.getOrderType());//订单的类型
			Map<String, Object> map = new HashMap<String, Object>();
			//如果为机柜租用，则处理方法不同
			if(OrderType.RACK_RENT.getOrderType() == orderType){
				List<Chest> list = assetManageService.getMatchRack4Order(order);
				map.put("aaData", list);
				map.put("order", order);// 作为回显配置的订单
			}else{
				Map<String, Object> callmap = assetManageService.getMatchServers4Order(order);
				Object obj = callmap.get("list");
				map.put("order", order);// 作为回显配置的订单
				map.put("aaData", obj);
			}
			//订单类型不同会有不同的页面，
			map.put("orderType", orderType);
			rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
			rm.setData(map);
			return rm;
		}
	}

	/*
	 * 添加服务器
	 */
	@POST
	@Path("/addServers")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Transactional
	@ControllerLog()
	public ResponseMessage addServers(List<Servers> serversList,@Context HttpServletRequest request) {
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		StringBuilder busiDesc = new StringBuilder();
		if(rm!=null){
			return rm;
		}else{
			//先判断交换机端口号有没有被占用
			rm = new ResponseMessage();
	        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务  
	        TransactionStatus txStatus = tx.getTransaction(def);// 获得事务状态  
			try {
				Chest chest = serversList.get(0).getChest();
				chest = assetManageService.getChestByChestId(chest.getChestId());
				int useStatus = 0;
				if(chest.getUseStatus()!=2){
					chest.setUseStatus(1);
					assetManageService.updateChest(chest);
				}else if(chest.getUseStatus()==2){
					useStatus = 1;
				}
				
				
				int i = 1;
				for (Servers servers : serversList) {
					List<ServerPort> portList = servers.getPortList();
					for (ServerPort serverPort : portList) {
						int flag = assetManageService.checkPortUsed(serverPort.getChestId(), serverPort.getPortNum());
						if(flag!=0){
							rm.setFlag("500");
							rm.setMsg("交换机端口号："+serverPort.getPortNum()+"已被占用");
							return rm;
						}
					}
					servers.setCreateTime(DateUtil.dateToStr(new Date(),
							"yyyy-MM-dd HH:mm:ss"));
					servers.setUseStatus(useStatus);
					//如果是批量添加的话  将服务器编号
					if(serversList.size()>1){
						if(!servers.getServerNumber().equals("")){
							servers.setServerNumber(servers.getServerNumber() + "-" + i);
						}
					}
					i++;
					assetManageService.addServers(servers);
					List<InstanceIp> ipList = servers.getIpList();
					for (InstanceIp instanceIp : ipList) {
						int status = assetManageService.checkIpUse(instanceIp.getIpId());
						if (status==1) {
							throw new ServerException("500","IP已被抢占,请刷新页面重新添加");
						}
					}
					assetManageService.addIpToServers(ipList, servers.getServerId());
				}
				rm.setCode("200");
				rm.setMsg("添加成功");
				tx.commit(txStatus);
				busiDesc.append("添加服务器成功，本次添加").append(i - 1).append("台服务器");
				rm.setBusinessDesc(busiDesc.toString());
				return rm;
				
			}catch(ServerException se){
				se.printStackTrace();
				rm.setCode(se.getCode());
				rm.setMsg(se.getMessage());
				tx.rollback(txStatus);
				return rm;
			}
			catch (Exception e) {
				tx.rollback(txStatus);
				e.printStackTrace();
				rm.setCode("500");
				rm.setMsg("网络异常");
				busiDesc.append("网络异常，添加服务器失败");
				rm.setBusinessDesc(busiDesc.toString());
				return rm;
			}
			
		}

	}

	/*
	 * 修改服务器
	 */
	@POST
	@Path("/updateServers")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Transactional
	@ControllerLog()
	public ResponseMessage updateServers(Servers servers,@Context HttpServletRequest request) {
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		StringBuilder busiDesc = new StringBuilder();
		if(rm!=null){
			return rm;
		}else{
			rm = new ResponseMessage();
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务  
	        TransactionStatus txStatus = tx.getTransaction(def);// 获得事务状态  
			try {
				assetManageService.deleteServerPort(servers.getServerId());
				List<ServerPort> portList = servers.getPortList();
				for (ServerPort serverPort : portList) {
					int flag = assetManageService.checkPortUsed(serverPort.getChestId(), serverPort.getPortNum());
					if(flag!=0){
						rm.setFlag("500");
						rm.setMsg("交换机端口号："+serverPort.getPortNum()+"已被占用");
						return rm;
					}
				}
				User user = new User();
				user.setId(0);
				servers.setUser(user);
				assetManageService.updateServers(servers);
				List<InstanceIp> ipList = servers.getIpList();
				for (InstanceIp instanceIp : ipList) {
					int status = assetManageService.checkIpUse(instanceIp.getIpId());
					if (status==1) {
						throw new ServerException("500","IP已被抢占,请刷新页面重新添加");
					}
				}
				assetManageService.addIpToServers(ipList,servers.getServerId());
				rm.setCode("200");
				rm.setMsg("修改成功");
				tx.commit(txStatus);
				//添加操作日志
				busiDesc.append("修改服务器成功，服务器Id:").append(servers.getServerId()).append("服务器编号：");
				if(servers.getServerNumber() == null || StringUtils.isBlank(servers.getServerNumber())){
					busiDesc.append("空");
				}else{
					busiDesc.append(servers.getServerNumber());
				}
				rm.setBusinessDesc(busiDesc.toString());
				return rm;
			} catch(ServerException se){
				rm.setCode(se.getCode());
				rm.setMsg(se.getMessage());
				tx.rollback(txStatus);
				return rm;
			}catch (Exception e) {
				e.printStackTrace();
				rm.setCode("500");
				rm.setMsg("网络异常，修改失败");
				tx.rollback(txStatus);
				busiDesc.append("服务器异常，服务器修改失败");
				rm.setBusinessDesc(busiDesc.toString());
				return rm;
			}
		}
	}
	
	@POST 
	@Path("/singleUpdateServers")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	public ResponseMessage singleUpdateServers(Servers servers,@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
			return rm;
		}else{
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
			TransactionStatus txStatus = tx.getTransaction(def);
			rm = new ResponseMessage();
			try {
				User user = new User();
				user.setId(0);
				servers.setUser(user);
				assetManageService.updateServers(servers);
				//assetManageService.addIpToServers(servers.getIpList(), servers.getServerId());
				rm.setCode("200");
				rm.setMsg("修改成功");
				tx.commit(txStatus);
				return rm;
			} catch (Exception e) {
				e.printStackTrace();
				rm.setCode("500");
				rm.setMsg("网络异常，修改失败");
				tx.rollback(txStatus);
				return rm;
			}
		}
	}
	

	/*
	 * 移除服务器上的IP
	 */
	@GET
	@Path("/removeServerIp")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Transactional
	public ResponseMessage removeServerIp(@QueryParam("ipId") int ipId,@Context HttpServletRequest request) {
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
			return rm;
		}else{
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务  
			TransactionStatus txStatus = tx.getTransaction(def);// 获得事务状态 
			try {
				rm = new ResponseMessage();
				assetManageService.removeServerIp(ipId);
				rm.setCode("200");
				tx.commit(txStatus);
				return rm;
			} catch (Exception e) {
				e.printStackTrace();
				tx.rollback(txStatus);
				rm = new ResponseMessage(ErrorCode.EXCEPTION);
				return rm;
			}
		}
	}

	/*
	 * 删除服务器
	 */
	@GET
	@Path("/deleteServer")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Transactional
	@ControllerLog(description="删除服务器")
	public ResponseMessage deleteServer(@QueryParam("serverId") int serverId,@Context HttpServletRequest request) {
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		StringBuilder busiDesc = new StringBuilder();
		if(rm!=null){
			return rm;
		}else{
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务  
			TransactionStatus txStatus = tx.getTransaction(def);// 获得事务状态 
			try {
				rm = new ResponseMessage();
				assetManageService.deleteServer(serverId);
				assetManageService.deleteServerIp(serverId);
				rm.setCode("200");
				rm.setMsg("删除成功");
				busiDesc.append("删除服务器成功，服务器Id:").append(serverId);
				rm.setBusinessDesc(busiDesc.toString());
				tx.commit(txStatus);
				return rm;
			} catch (Exception e) {
				e.printStackTrace();
				tx.rollback(txStatus);
				rm = new ResponseMessage(ErrorCode.EXCEPTION);
				return rm;
			}
		}
	}

	/*
	 * 获取机柜下所有未使用的IP
	 */
	@GET
	@Path("/getAllNoUseIp")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Transactional
	public ResponseMessage getAllNoUseIp(@QueryParam("chestId") int chestId,@Context HttpServletRequest request) {
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
			return rm;
		}else{
			rm = new ResponseMessage();
			List<InstanceIp> list = assetManageService.getAllNoUseIp(chestId);
			rm.setCode("200");
			rm.setData(list);
			return rm;
		}
		
	}
	/*
	 * 删除机柜下的IP段
	 */
	@POST
	@Path("/deleteIpRange")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Transactional
	public ResponseMessage deleteIpRange(InstanceIp instanceIp,@Context HttpServletRequest request) {
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
			return rm;
		}else{
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);// 事物隔离级别，开启新事务  
			TransactionStatus txStatus = tx.getTransaction(def);// 获得事务状态 
			try {
				rm = new ResponseMessage();
				List<InstanceIp> ipList = new ArrayList<InstanceIp>();
				List<ChestIpRange> ipRangeList = new ArrayList<ChestIpRange>();
				ChestIpRange chestIpRange = new ChestIpRange();
				chestIpRange.setIpBusi(instanceIp.getIpBusi());
				ipList.add(instanceIp);
				ipRangeList.add(chestIpRange);
				assetManageService.deleteChestIpRangeExist(ipRangeList, instanceIp.getChestId());
				assetManageService.deleteChestIpListExist(ipList, instanceIp.getChestId());
				rm.setCode("200");
				tx.commit(txStatus);
				return rm;
			} catch (Exception e) {
				e.printStackTrace();
				tx.rollback(txStatus);
				rm  = new ResponseMessage(ErrorCode.EXCEPTION);
				return rm;
			}
		}
		
	}
	

}
