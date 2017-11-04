package com.baoidc.idcserver.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baoidc.idcserver.po.DeviceSourceParam;
import com.baoidc.idcserver.po.Order;
import com.baoidc.idcserver.po.OrderParam;
import com.baoidc.idcserver.po.Remark;
import com.baoidc.idcserver.po.ServerRentOrder;
import com.baoidc.idcserver.po.ServerRoom;
import com.baoidc.idcserver.vo.query.OrderQueryVO;


public interface DeviceServerDAO {
	
	//获取所有的资源参数值
	public List<DeviceSourceParam> getDeviceSourceParam();

	//通过资源类型获取其下所有参数值
	public List<DeviceSourceParam> getDeviceSourceParamBySourceType(int sourceType);

	//获取所有的机房和其下的服务器
	public List<ServerRoom> getServerRoomList();

	//过时了，
	public void addServerRentOrder(ServerRentOrder serverRentOrder);

	//新增订单
	public void addOrder(Order order);

	//新增订单下的属性值
	public void addOrderParam(OrderParam orderParam);

	//通过userId获取所有订单
	public List<Order> getOrderListByUserId(int userId);

	//通过产品id获取该产品服务所有信息
	public List<DeviceSourceParam> getServiceProductByProductId(int productId);

	//更新订单状态
	public void updateOrderStatus(Order order);

	//删除订单
	public void deleteOrder(Order order);

	//获取单个订单
	public Order getSingleOrder(Order order);
	
	//获取单个订单byOrderId
	public Order getSingleOrderByOrderId(String orderId);
	
	public List<Order> getOrders();
	
	public List<Order> getOrdersForManage(OrderQueryVO orderQueryVO);
	
	public OrderParam getOrderParamByInst(OrderParam orderParam);

//	查询未支付订单个数
	public int getNoPayOrder(int userId);

//	查询即将过期的订单个数
	public int getExpireOrder(int userId);
	
	//批量更新状态
	public void updateOrderListStatus(@Param("orderList") List<Order> orderList);

	//批量添加
	public void addOrderList(@Param("orderList") List<Order> orderList);

	//批量添加
	public void addOrderParamList(@Param("paramList") List<OrderParam> paramList);

	//新增备注--对象
	public void addRemark(Remark remark);

	//订单的条件查询
	public List<Order> getOrderList4Query(Order order);
	
	public List<Order> getNoDealedOrders();

}
