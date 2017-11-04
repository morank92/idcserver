package com.baoidc.idcserver.rest.service;

import com.baoid.idcserver.vo.ServerRentVo;
import com.baoidc.idcserver.core.*;
import com.baoidc.idcserver.core.aspect.ControllerLog;
import com.baoidc.idcserver.po.Order;
import com.baoidc.idcserver.service.IDeviceService;
import com.baoidc.idcserver.vo.query.ColumnIndexVO;
import com.baoidc.idcserver.vo.query.OrderQueryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.*;

@Component
@Path("/deviceServer")
public class ProductRestService { // controller

	// private static final String HOST = "http://localhost:8080/idcserver";

	@Autowired
	private IDeviceService deviceService;
	
	@Autowired
	private RedisUtil redisUtil;

	// 过时了
	@GET
	@Path("/getDeviceServer4Rent")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	@Transactional
	public ServerRentVo getDeviceSourceParam() {
		ServerRentVo serverRentVo = deviceService.getDeviceSourceParam();
		return serverRentVo;
	}
	
	// 提交要显示的列名
	@POST
	@Path("saveColumnIndex4Table")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Transactional
	public ResponseMessage saveColumnIndex4Table(ColumnIndexVO columnIndexVO,@Context HttpServletRequest request) {
		
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
		  return rm;
		}else{
			  deviceService.saveColumnIndex4Table(columnIndexVO);
			  rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);//
			  return rm;
		}
	}
	// 查询要显示的列名
	@POST
	@Path("getColumnIndex4Table")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Transactional
	public ResponseMessage getColumnIndex4Table(ColumnIndexVO columnIndexVO,@Context HttpServletRequest request) {
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		ColumnIndexVO colIndex = new ColumnIndexVO();
		if(rm!=null){
		  return rm;
		}else{
				  ResponseMessage res = deviceService.getColumnIndex4Table(columnIndexVO);
				  if(res.getFlag().equals("Y")){
					  colIndex = (ColumnIndexVO) res.getObj();
					  rm = new ResponseMessage(ErrorCode.EVENT_TRUE);//
				  }else{
					  rm = new ResponseMessage(ErrorCode.EVENT_FALSE);//
				  }
				  rm.setData(colIndex);
				  return rm;
		}
	}

	/*// 提交当前配置的服务产品，生成对应的类型的订单
	@POST
	@Path("confirmNewOrder")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Transactional
	public ResponseMessage confirmNewOrder(Order order) {
		ResponseMessage res = new ResponseMessage();
		Order newOrder = deviceService.confirmNewOrder(order);
		if(newOrder == null){
			res.setFlag("N");
		}else{
			res.setObj(newOrder);
		}
		return res;
	}*/
	
	// 提交当前配置的服务产品，生成对应的类型的订单
	@POST
	@Path("submitNewOrder")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Transactional
	@ControllerLog
	public ResponseMessage submitNewOrder(Order order,@Context HttpServletRequest request) {
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		StringBuilder busiDesc = new StringBuilder();
		if(rm!=null){
		  return rm;
		}else{
			Order newOrder = deviceService.submitNewOrder(order);
			if (newOrder != null) {
				rm = new ResponseMessage(ErrorCode.SUBMIT_ORDER_TRUE);//
				rm.setData(newOrder);
				busiDesc.append("订单提交成功，订单类型：").append(OrderTypeTransfer.getOrderTypeText(order.getOrderType()))
						.append("，提交时间：").append(DateUtil.dateToStr(new Date(),"yyyy-MM-dd HH:mm:ss"))
						.append("，订单金额：").append(order.getPrice());
			} else {
				rm = new ResponseMessage(ErrorCode.SUBMIT_ORDER_FALSE);//
				busiDesc.append("订单处理失败");
			}
			rm.setBusinessDesc(busiDesc.toString());
			return rm;
		}
	}

	// 通过userId获取其对应的所有的订单
	@POST
	@Path("/getOrderListByUserId/{userId}")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	@ControllerLog(description="查询用户订单")
	@Transactional
	public ResponseMessage getOrderListByUserId(
			@PathParam("userId") int userId,@Context HttpServletRequest request) {
		ResponseMessage rm = (ResponseMessage)request.getAttribute("isLogin");
		if(rm != null){
			return rm;
		}else{
			List<Order> orderList = new ArrayList<Order>();
			HashMap<String, List<Order>> map = deviceService.getOrderListByUserId(userId);
			if (map != null) {
				orderList = map.get("aaData");
			}
			rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
			rm.setData(orderList);
			return rm;
		}
	}

	// 通过服务产品的id获取对应的购买页面信息
	@GET
	@Path("/getServiceProduct4page")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	@Transactional
	public ResponseMessage getServiceProduct4page(
			@QueryParam("productId") int productId,@Context HttpServletRequest request) {
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
		  return rm;
		}else{
			ServerRentVo serverRentVo = deviceService.getServiceProduct4page(productId);
			rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);//
			rm.setData(serverRentVo);
			return rm;
		}
	}

	// 确定支付订单
	@POST
	@Path("payProductOrder")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Transactional
	@ControllerLog
	public ResponseMessage payProductOrder(Order order,@Context HttpServletRequest request) {
		ResponseMessage res = (ResponseMessage)request.getAttribute("isLogin");
		StringBuilder busiDesc = new StringBuilder();
		if(res != null){
			return res;
		}else{
			res = deviceService.payProductOrder(order);
			busiDesc.append("订单支付成功，订单金额：").append(order.getPrice())
					.append("，支付时间：").append(DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
			res.setBusinessDesc(busiDesc.toString());
			return res;
		}
		
	}
	
	// 确定批量支付订单
	/*@POST
	@Path("payProductOrderList")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Transactional
	public ResponseMessage payProductOrderList(OrderBatchVO orderBatchVO) {
		List<Order> orderList = orderBatchVO.getOrderList();//取出集合
		ResponseMessage res = deviceService.payProductOrderList(orderList);
		return res;
	}*/

	// 删除订单
	@POST
	@Path("deleteOrder")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Transactional
	@ControllerLog
	public ResponseMessage deleteOrder(Order order,@Context HttpServletRequest request) {
		ResponseMessage res = (ResponseMessage)request.getAttribute("isLogin");
		StringBuilder busiDesc = new StringBuilder();
		if(res != null){
			return res;
		}else{
			res = deviceService.deleteOrder(order);
			busiDesc.append("删除订单成功，订单Id：").append(order.getOrderId());
			res.setBusinessDesc(busiDesc.toString());
			return res;
		}
		
	}

	// 通过userId获取其对应的所有的订单,管理员
	@GET
	@Path("/getOrders")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	@Transactional
	public ResponseMessage getOrders(@QueryParam("orderType") Integer orderType,
			@QueryParam("orderStatus") Integer orderStatus,
			@QueryParam("startTime") String startTime,
			@QueryParam("endTime") String endTime,@Context HttpServletRequest request) {
		ResponseMessage rm = (ResponseMessage)request.getAttribute("isLogin");
		if(rm != null){
			return rm;
		}else{
				System.out.println("orderType = " + orderType);
				OrderQueryVO orderQueryVO = new OrderQueryVO();
				
				/*if(orderType == null || orderType == 0){
					orderType = -1;
					
				}*/
				orderQueryVO.setOrderType(orderType);
				orderQueryVO.setOrderStatus(orderStatus);
				if(startTime == null || startTime == ""){
					orderQueryVO.setStartTime("");
				}else{
					orderQueryVO.setStartTime(startTime);
				}
				if(endTime == null || endTime == ""){
					orderQueryVO.setEndTime("");
				}else{
					orderQueryVO.setEndTime(endTime);
				}
				
				//判断是否为客户经理访问此路径，如果是，则需要查询的订单应为客户经理下的用户的订单
				boolean isCusManager = false;
				int cusIdLoginId = -1;//客户经理的id,默认-1
				String t = request.getHeader("TOKEN");
				String cusIdStr = request.getHeader("USER-ID");
				if(cusIdStr != null && cusIdStr != ""){
					cusIdLoginId = Integer.parseInt(cusIdStr);//当前登录的用户id
				}
				String role = redisUtil.get(t);//登录时放入redis中的角色id；
				if(role != null && role.equals("3")){//目前将客户经理角色id暂定于3
					isCusManager = true;
				}
				
				//获取数据
				Map<String, List<Order>> map = deviceService.getOrdersForManage(orderQueryVO);
			
				List<Order> orderList = new ArrayList<Order>();
				if(map.get("aaData") != null){
					orderList = map.get("aaData");
					if(isCusManager){//取出当前客户经理的订单
						ArrayList<Order> nlist = new ArrayList<Order>();
						for(int i=0;i<orderList.size();i++){
							int cusId = orderList.get(i).getCustomerManagerId();
							if(cusIdLoginId == cusId){//取出当前客户经理的订单
								nlist.add(orderList.get(i));
							}
						}
						orderList = nlist;//重新赋值
					}
				}
				rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
				rm.setData(orderList);
				return rm;
		}
	}
	
	// 条件查询订单，客户系统的
	@POST
	@Path("/orderQuery4user")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	@Transactional
	public ResponseMessage orderQuery4user(Order order,@Context HttpServletRequest request) {
		ResponseMessage rm = (ResponseMessage)request.getAttribute("isLogin");
		if(rm != null){
			return rm;
		}else{
				List<Order> orderList = new ArrayList<Order>();
				orderList = deviceService.orderQuery4user(order);
				rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
				rm.setData(orderList);
				return rm;
		}
	}
	
	// 同步订单状态
	@POST
	@Path("/getSyncOrder")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON })
	@Transactional
	public ResponseMessage getSyncOrder(Order order,@Context HttpServletRequest request) {
		ResponseMessage rm = (ResponseMessage)request.getAttribute("isLogin");
		if(rm != null){
			return rm;
		}else{
			
			Order newOrder = deviceService.getSingleOrder(order);
			rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
			rm.setData(newOrder);
			return rm;
		}
	}
	
	
	
	/*
	 * 查询未支付订单个数
	 */
	@GET 
	@Path("/getNoPayOrder")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@Transactional
	public ResponseMessage getNoPayOrder(@QueryParam("userId")int userId,@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
			return rm;
		}else{
				int count = deviceService.getNoPayOrder(userId);
				rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
				rm.setData(count);
				return rm;
		}
	}
	/*
	 * 查询即将过期的订单个数
	 */
	@GET 
	@Path("/getExpireOrder")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@Transactional
	public ResponseMessage getExpireOrder(@QueryParam("userId")int userId,@Context HttpServletRequest request){
		ResponseMessage rm = (ResponseMessage) request.getAttribute("isLogin");
		if(rm!=null){
			return rm;
		}else{
				int count = deviceService.getExpireOrder(userId);
				rm = new ResponseMessage(ErrorCode.BUSINESS_DEAL_SUCCESS);
				rm.setData(count);
				return rm;
		}
	}

}
