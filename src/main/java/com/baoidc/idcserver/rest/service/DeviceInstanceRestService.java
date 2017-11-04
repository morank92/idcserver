package com.baoidc.idcserver.rest.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.baoid.idcserver.vo.CreateDeviceInstanceVO;
import com.baoid.idcserver.vo.DeviceInstanceVO;
import com.baoid.idcserver.vo.ServerRentVo;
import com.baoidc.idcserver.core.DateUtil;
import com.baoidc.idcserver.core.ErrorCode;
import com.baoidc.idcserver.core.ResponseMessage;
import com.baoidc.idcserver.core.SourceTag;
import com.baoidc.idcserver.core.aspect.ControllerLog;
import com.baoidc.idcserver.po.AutoRenewConf;
import com.baoidc.idcserver.po.DeviceInstance;
import com.baoidc.idcserver.po.DeviceSourceParam;
import com.baoidc.idcserver.po.InstanceIp;
import com.baoidc.idcserver.po.Order;
import com.baoidc.idcserver.po.Servers;
import com.baoidc.idcserver.po.User;
import com.baoidc.idcserver.po.UserAccount;
import com.baoidc.idcserver.po.UserWorkOrder;
import com.baoidc.idcserver.service.AssetManageService;
import com.baoidc.idcserver.service.IDeviceInstanceService;
import com.baoidc.idcserver.service.IDeviceService;
import com.baoidc.idcserver.service.IUserAccountService;
import com.baoidc.idcserver.vo.query.InstanceQuery;
import com.baoidc.idcserver.vo.query.RenewVO;

@Component
@Path("/deviceInstance")
public class DeviceInstanceRestService { // controller

	// private static final String HOST = "http://localhost:8080/idcserver";

	@Autowired
	private IDeviceInstanceService instanceService;
	
	@Autowired
	private AssetManageService assetManageService;
	
	@Autowired
	private IDeviceService deviceService;
	
	// 提交选择的实例续费，生成续费类型的订单
	@POST
	@Path("submitRenewOrder")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor=Exception.class,timeout=3000)
	@ControllerLog(description="提交续费的订单")
	public ResponseMessage submitRenewOrder(RenewVO renewvo,@Context HttpServletRequest request) {
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		StringBuilder busiDesc = new StringBuilder();
	    if(rm!=null){
	      return rm;
	    }else{
	    	  List<Order> orderList = instanceService.submitRenewOrder(renewvo);
	    	  rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
	    	  rm.setData(orderList);
	    	  busiDesc.append("提交续费订单成功，订单总数：").append(orderList.size());
	    	  busiDesc.append("，订单提交时间：").append(DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
	    	  double orderTotalAmt = 0.00;
	    	  for (Order order : orderList) {
				 orderTotalAmt += order.getPrice();
			  }
	    	  busiDesc.append("，订单总金额：").append(orderTotalAmt);
	    	  rm.setBusinessDesc(busiDesc.toString());
	    	  return rm;
	    }
		
	}
	
	// 自动续费的配置，提交选择的实例
	@POST
	@Path("submitAutoRenew")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@ControllerLog
	public ResponseMessage submitAutoRenew(List<DeviceInstance> instanceList,@Context HttpServletRequest request) {
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		StringBuilder busiDesc = new StringBuilder();
	    if(rm!=null){
	      return rm;
	    }else{
	    	
    		  //更新当前实例的自动续费的状态
	    	for(int i=0;i<instanceList.size();i++){
	    		instanceList.get(i).setAutoRenewStatus(1);//1为自动续费开启的状态
	    		instanceService.updateInstanceByAnyting(instanceList.get(i));
	    	}
	    		
	    	  instanceService.submitAutoRenew(instanceList);
	    	  busiDesc.append("设备：");
	    	  for(int i=0;i<instanceList.size();i++){
	    		  if(i == instanceList.size()-1){
	    			  busiDesc.append(instanceList.get(i).getInstanceNum()+"；");
	    		  }else{
	    			  busiDesc.append(instanceList.get(i).getInstanceNum()+"、");
	    		  }
	    		  
	    	  }
	    	  
	    	  busiDesc.append("自动续费配置成功");
	    	  rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
	    	  rm.setBusinessDesc(busiDesc.toString());
	    	  return rm;
	    }
		
	}
	
	// 取消自动续费
	@POST
	@Path("cancelAutoRenew")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@ControllerLog
	public ResponseMessage cancelAutoRenew(DeviceInstance instance,@Context HttpServletRequest request) {
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		StringBuilder busiDesc = new StringBuilder();
	    if(rm!=null){
	      return rm;
	    }else{
	    	//更新实例的autorenewStatus状态为0
	    	instance.setAutoRenewStatus(0);
    	  instanceService.updateInstanceByAnyting(instance);//该实例dao只更新autorenewStatus状态
    	  //删除该实例AutoRenewConf
    	  instanceService.deleteAutoRenewConf(instance.getInstanceId());
    		
    	  busiDesc.append("取消了"+instance.getInstanceNum()+"设备的自动续费");
    	  rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
    	  rm.setBusinessDesc(busiDesc.toString());
    	  return rm;
	    }
		
	}
	
	//自动续费的执行
	@POST
	@Path("/executeAutoRenew")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@ControllerLog
	public ResponseMessage executeAutoRenew(AutoRenewConf conf,@Context HttpServletRequest request) {
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		StringBuilder busiDesc = new StringBuilder();
	    if(rm!=null){
	      return rm;
	    }else{
		  //1，需要查询出配置项中的实例，和其时长所对应的产品类deviceSourceParam,
	    	DeviceInstance deviceInstance = new DeviceInstance();
	    	if(conf.getUser() != null){
	    		deviceInstance.setUserId(conf.getUser().getId());//用户id
	    	}
			deviceInstance.setInstanceId(conf.getInstanceId());//实例id
			List<DeviceInstance> insList = instanceService.getInstanceList(deviceInstance);//此接口很强,返回的为自动续费实例
			
			int productId = 8;//产品参数，自动续费续费为8
			ServerRentVo vo = deviceService.getServiceProduct4page(productId);//获取产品参数，自动续费续费为8：1个月对应的对象
			List<DeviceSourceParam> paramList = vo.getSourceParamList();
			//获取为一个月的对象DeviceSourceParam
			DeviceSourceParam param = new DeviceSourceParam();
			for(int i=0;i<paramList.size();i++){
				if(conf.getDuration().equals(paramList.get(i).getSourceParam())){//AutoRenewConf默认配置的时长为一个月
					param = paramList.get(i);
				}
			}
			
			//调用生成续费订单的接口，所需要的参数是：{"userId":userId,"orderType":orderType,"durationEncrypt":durationEncrypt,"instanceEncrypt":instanceEncrypt};
			RenewVO renewvo = new RenewVO();
			if(insList != null && insList.size() != 0){
				String insEncrypt = insList.get(0).getEncrypt();//通过id返回的只有一个对象
				if(insEncrypt != null){
					String[] arr = {insEncrypt};//构建一个数组
					renewvo.setInstanceEncrypt(arr);
				}
			}
			renewvo.setUserId(conf.getUser().getId());
			renewvo.setOrderType(productId);//订单类型,自动续费的类型，作为订单处理的时候用
			renewvo.setDurationEncrypt(param.getEncrypt());//时长对应的属性
			List<Order> orderList = instanceService.submitRenewOrder(renewvo);//生成订单，并返回订单
			//再批量支付
			boolean isPay = instanceService.payProductOrderList(orderList);
			//更新AutoRenewConf 的状态为1，即：订单已生成待处理
			conf.setStatus(1);
			instanceService.updateAutoRenewConf(conf);
			
			if(isPay){
	    		  rm = new ResponseMessage(ErrorCode.PAY_SUCCESS);//支付成功
	    		  busiDesc.append("续费订单支付成功，");
                busiDesc.append("支付时间：").append(DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
                double orderTotalAmt = 0.00;
                for (Order order : orderList) {
					orderTotalAmt += order.getPrice();
				  }
                busiDesc.append("，支付总金额：").append(orderTotalAmt);
	    	  }else{
	    		  rm = new ResponseMessage(ErrorCode.BALANCE_NOT_ENOUGH);//余额不足
	    		  busiDesc.append("余额不足，续费订单支付失败");
	    	  }
	    	  rm.setBusinessDesc(busiDesc.toString());
	    	  rm.setData(orderList);
	    	  return rm;
	    }
		
	}
	
	
	// 批量支付
	@POST
	@Path("payProductOrderList")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor=Exception.class,timeout=3000)
	@ControllerLog(description="支付续费的订单")
	public ResponseMessage payProductOrderList(List<Order> orderList,@Context HttpServletRequest request) {
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		StringBuilder busiDesc = new StringBuilder();
	    if(rm!=null){
	      return rm;
	    }else{
	    	  boolean flag = instanceService.payProductOrderList(orderList);
	    	  if(flag){
	    		  rm = new ResponseMessage(ErrorCode.PAY_SUCCESS);//支付成功
	    		  busiDesc.append("续费订单支付成功，");
                  busiDesc.append("支付时间：").append(DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
                  double orderTotalAmt = 0.00;
                  for (Order order : orderList) {
					orderTotalAmt += order.getPrice();
				  }
                  busiDesc.append("，支付总金额：").append(orderTotalAmt);
	    	  }else{
	    		  rm = new ResponseMessage(ErrorCode.BALANCE_NOT_ENOUGH);//余额不足
	    		  busiDesc.append("余额不足，续费订单支付失败");
	    	  }
	    	  rm.setBusinessDesc(busiDesc.toString());
	    	  return rm;
	    }
	}

	// 通过userId获取其对应的所有的产品实例
	/*@GET
	@Path("/getInstanceListByUserId/{userId}")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	@Transactional
	public HashMap<String, List<DeviceInstance>> getInstanceListByUserId(
			@PathParam("userId") int userId,@Context HttpServletRequest request) {
		HashMap<String, List<DeviceInstance>> map = instanceService
				.getInstanceListByUserId(userId);
		return map;
	}*/
	
	//获取待回收产品实例
	@GET
	@Path("/getPreRecycleInstList")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	@Transactional
	public ResponseMessage getPreRecycleInstList(
			@PathParam("userId") int userId,@Context HttpServletRequest request) {
		ResponseMessage rm  = (ResponseMessage)request.getAttribute("isLogin");
		if(rm != null){
			return rm;
		}else{
			Map<String, List<DeviceInstance>> map = instanceService.getPreRecycleInstanceList();
			List<DeviceInstance> deviceInstList = map.get("aaData");
			rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
			rm.setData(deviceInstList);
			return rm;
		}
	}	

	// 获取其对应的所有的产品实例，通过userId、serviceId
	@POST
	@Path("/getInstanceList")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	//@ControllerLog(description="查询设备实例信息")
	@Transactional
	public ResponseMessage getInstanceList(InstanceQuery instanceQuery,@Context HttpServletRequest request) {
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
			return rm;
		}else{
				DeviceInstance deviceInstance = new DeviceInstance();
				deviceInstance.setUserId(instanceQuery.getUserId());
				deviceInstance.setServiceId(instanceQuery.getServiceId());
				List<DeviceInstance> list = instanceService
						.getInstanceList(deviceInstance);
				rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
				rm.setData(list);
				return rm;
				
		}
		
	}
	
	//获取单个实例对象
	@POST 
	@Path("/getSingleInstance")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@Transactional
	public ResponseMessage getWorkOrderListByUser(DeviceInstance deviceInstance,
			@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
			return rm;
		}else{
			DeviceInstance ins = instanceService.getSingleInstance(deviceInstance);
			rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
			rm.setData(ins);
			return rm;
		}
	}
	
	
	//条件查询实例，如：ip地址
	@POST
	@Path("queryInstance")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Transactional
	public ResponseMessage queryInstance(InstanceQuery instanceQuery,@Context HttpServletRequest request) {
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
	    if(rm!=null){
	      return rm;
	    }else{
	    		//先基于之前的查询出来数据，再来判断这些数据是否包含页面所需的ip，如果是则返回
	    	  	DeviceInstance deviceInstance = new DeviceInstance();
				deviceInstance.setUserId(instanceQuery.getUserId());
				deviceInstance.setServiceId(instanceQuery.getServiceId());
				List<DeviceInstance> list = instanceService.getInstanceList(deviceInstance);//查询出之前的数据
				
				List<DeviceInstance> callList = new ArrayList<DeviceInstance>();//需要返回的集合
				String ipStr = instanceQuery.getIpStr();//页面传过来的查询ip参数
				
				if(ipStr != null && ipStr != ""){
					for(int i=0;i<list.size();i++){
						List<InstanceIp> ipList = list.get(i).getIpList();
						if(ipList != null){
							for(int j=0;j<ipList.size();j++){
								String sIp = ipList.get(j).getIpStr();//差询出来的ip
								if(sIp != null){
									if(sIp.contains(ipStr)){//模糊包含页面参数ip，则返回
										callList.add(list.get(i));
										//跳出此次循环
										break;
									}
								}else{
									break;
								}
								
							}
						}
					}
				}else{
					callList.addAll(list);
				}
				
	    	  //List<DeviceInstance> instanceList = instanceService.queryInstance(instanceQuery);
	    	  rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
	    	  rm.setData(callList);
	    	  return rm;
	    }
	}

	// 通过instanceId获取其对应的产品实例
	@GET
	@Path("/getInstanceByInstanceId")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	@Transactional
	public ResponseMessage getInstanceByInstanceId(
			@QueryParam("instanceId") int instanceId,@Context HttpServletRequest request) {
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
		  return rm;
		}else{
			  DeviceInstance instance = instanceService.getInstanceByInstanceId(instanceId);
			  if(instance != null){
				  rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
				  rm.setData(instance);
			  }
			  return rm;
		}
	}

	// 新建实例
	@POST
	@Path("createInstance")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Transactional
	@ControllerLog
	public ResponseMessage createInstance(CreateDeviceInstanceVO createInstanceVO,@Context HttpServletRequest request) {
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		StringBuilder busiDesc = new StringBuilder();
		if(rm!=null){
		  return rm;
		}else{
			instanceService.generationInstance(createInstanceVO);
			rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
			busiDesc.append("创建实例成功,关联订单Id：").append(createInstanceVO.getOrderId())
					.append(",创建实例个数：").append(createInstanceVO.getNumbers().size());
			rm.setBusinessDesc(busiDesc.toString());
			return rm;
		}
	}

	// 升级处理前，获取设备信息
	@POST
	@Path("/getDeviceByInstanceId4Order")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Transactional
	public ResponseMessage getDeviceByInstanceId4Order(Order order,@Context HttpServletRequest request) {
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		Servers servers = new Servers();
		if(rm!=null){
		  return rm;
		}else{
			  if("6".equals(order.getOrderType())){//为6：针对于机柜里面的服务器升级的订单
				  
				  servers = instanceService.getServerByServerId4UpOrder(order.getServerId());
				  
			  }else{
				  servers = instanceService.getDeviceByInstanceId4Order(order.getInstanceId());
			  }
			  
			  if(servers != null){
				  rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);//
			  }else{
				  rm = new ResponseMessage(ErrorCode.GET_FALSE);//未获取到
			  }
			  rm.setData(servers);
			  return rm;
		}
	}
	
	// 升级的订单处理，
	@POST 
	@Path("/dealOrder4up")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	@ControllerLog(description="处理升级的订单")
	public ResponseMessage singleUpdateServers(Servers servers,@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		StringBuilder busiDesc = new StringBuilder();
		if(rm!=null){
		  return rm;
		}else{
			User user = new User();
			user.setId(0);
			servers.setUser(user);
			// 不需要修改当前资产的使用状态
			servers.setUseStatus(-1);// 此sql默认：使用状态为-1则不修改当前资产的使用状态。
			assetManageService.updateServers(servers);// 更新资产
			instanceService.updateInstance(servers.getOrderId());
			rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);//
			busiDesc.append("升级订单处理成功，关联资产id：").append(servers.getServerId()).append("，关联订单Id:").append(servers.getOrderId());
			rm.setBusinessDesc(busiDesc.toString());
			return rm;
		}
	}
	
	// 机柜升级的订单处理，
	@POST 
	@Path("/dealRack4OrderUp")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})	
	@Transactional
	@ControllerLog()
	public ResponseMessage dealRack4OrderUp(Order order,@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		StringBuilder busiDesc = new StringBuilder();
		if(rm!=null){
		  return rm;
		}else{
			instanceService.updateInstance(order.getOrderId());
			rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);//
			busiDesc.append("机柜DDOS升级处理完成，订单id：").append(order.getOrderId());
			rm.setBusinessDesc(busiDesc.toString());
			return rm;
		}
	}
	
	
	// 修改实例参数
	@POST
	@Path("updateInstance")
	@Produces({ MediaType.APPLICATION_JSON })
	@Transactional
	public ResponseMessage updateInstance(@QueryParam("orderId") String orderId,@Context HttpServletRequest request) {
		instanceService.updateInstance(orderId);
		ResponseMessage res = new ResponseMessage(
				ErrorCode.BUSINESS_DEAL_SUCCESS);
		return res;
	}
	
	//资产回收
	@POST
	@Path("assetRecyle")
	@Produces({ MediaType.APPLICATION_JSON })
	@Transactional
	@ControllerLog
	public ResponseMessage assetRecyle(@QueryParam("instanceId") int instanceId,@Context HttpServletRequest request) {
		ResponseMessage rm = (ResponseMessage)request.getAttribute("isLogin");
		StringBuilder busiDesc = new StringBuilder();
		if(rm != null){
			return rm;
		}else{
			instanceService.assetRecycle(instanceId);
			rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
			busiDesc.append("资产回收处理成功，关联实例Id：").append(instanceId);
		}
		rm.setBusinessDesc(busiDesc.toString());
		return rm;
	}
	
	//续费的订单处理
	@POST
	@Path("renewInstance")
	@Produces({ MediaType.APPLICATION_JSON })
	@Transactional
	@ControllerLog()
	public ResponseMessage renewInstance(@QueryParam("orderId") String orderId,@Context HttpServletRequest request) {
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		StringBuilder busiDesc = new StringBuilder();
		if(rm!=null){
		  return rm;
		}else{
			  boolean flag = instanceService.renewInstance(orderId);
			  if(flag){
				  rm = new ResponseMessage(ErrorCode.DEAL_REORDER_TRUE);//处理成功
				  busiDesc.append("续费订单处理成功，订单号：").append(orderId);
			  }else{
				  rm = new ResponseMessage(ErrorCode.DEAL_REORDER_FALSE);//该订单已处理
				  busiDesc.append("余额不足，续费订单处理失败！！");
			  }
			  rm.setBusinessDesc(busiDesc.toString());
			  return rm;
		}
	}
	
	//查询一个机房下所有实例
	@POST
	@Path("getAllInstanceByHostRoom")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Transactional
	public ResponseMessage getAllInstanceByHostRoom(@FormParam("hostRoom") String hostRoom,
						@FormParam("userId")int userId,@Context HttpServletRequest request) throws Exception {
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
		  return rm;
		}else{
			  Map<String,Map<String,Integer>> map = instanceService.getAllInstanceByHostRoom(hostRoom,userId);
			  rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
			  rm.setData(map);
			  return rm;
		}
	}
	
	
	 //从数据库获取实例，保证数据同步统一性，insList：页面现有的实例集合
	@POST
	@Path("getSourceInstance2db")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	@Transactional
	public ResponseMessage getSourceInstance2db(List<DeviceInstance> deviceInstanceList,@Context HttpServletRequest request) {
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
		  return rm;
		}else{
			  List<DeviceInstance> sIns = instanceService.getSourceInstance2db(deviceInstanceList);
			  rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
			  rm.setData(sIns);
			  return rm;
		}
	}
	
	// 通过instanceId获取其对应的产品实例
	@GET
	@Path("/getInstanceInUse")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	@Transactional
	public ResponseMessage getInstanceInUse(@Context HttpServletRequest request) {
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if (rm != null) {
			return rm;
		} else {
			List<DeviceInstance> instList = instanceService.getInstanceInUse();
			rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
			rm.setData(instList);
			return rm;
		}
	}
		
	//从数据库获取实例，保证数据同步统一性，insList：页面现有的实例集合
	@POST
	@Path("/updateInstanceSingle")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	@Transactional
	public ResponseMessage updateInstanceSingle(DeviceInstance deviceInstance,@Context HttpServletRequest request) {
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if (rm != null) {
			return rm;
		} else {
			instanceService.updateInstanceSingle(deviceInstance);
			rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
			return rm;
		}
	}
	
	
	// 通过instanceId获取其对应的产品实例
	@GET
	@Path("/queryAutoRenewConf")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	@Transactional
	public ResponseMessage queryAutoRenewConf(@QueryParam("instanceId") int instanceId,@Context HttpServletRequest request) {
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if (rm != null) {
			return rm;
		} else {
			AutoRenewConf autoRenewConf = instanceService.getAutoRenewConf(instanceId);
			rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
			rm.setData(autoRenewConf);
			return rm;
		}
	}
	
}
