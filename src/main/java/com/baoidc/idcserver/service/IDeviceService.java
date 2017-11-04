package com.baoidc.idcserver.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baoid.idcserver.vo.ServerRentVo;
import com.baoidc.idcserver.core.ResponseMessage;
import com.baoidc.idcserver.po.Order;
import com.baoidc.idcserver.vo.query.ColumnIndexVO;
import com.baoidc.idcserver.vo.query.OrderQueryVO;

public interface IDeviceService {
	
	//获取设备资源信息
	public ServerRentVo getDeviceSourceParam();
	
	//生成订单
	public Order submitNewOrder(Order order);
	
	//获取订单
	public HashMap<String, List<Order>> getOrderListByUserId(int userId);
	
	//获取产品服务信息
	public ServerRentVo getServiceProduct4page(int productId);

	//订单支付
	public ResponseMessage payProductOrder(Order order);

	//删除订单
	public ResponseMessage deleteOrder(Order order);
	
	//获取订单列表
	public Map<String, List<Order>> getOrders();

	//验证新建订单
	public Order confirmNewOrder(Order order);
	
	//管理员获取订单列表
	public Map<String, List<Order>> getOrdersForManage(OrderQueryVO orderQueryVO);

	// 提交要显示的列名
	public ResponseMessage saveColumnIndex4Table(ColumnIndexVO columnIndexVO);

	//获取要显示的列名
	public ResponseMessage getColumnIndex4Table(ColumnIndexVO columnIndexVO);
	
	//加密存储！将从数据库中取出来的订单order加密放入redis中。保证再次从页面获取出来为同一对象,
	public Order getSecurity4Order(Order order, int storeIndex, String secKey);

	//批量支付订单
	public ResponseMessage payProductOrderList(List<Order> orderList);
	
//	查询未支付订单个数
	public int getNoPayOrder(int userId);
	
	//查询即将过期的订单个数
	public int getExpireOrder(int userId);

	//订单的条件查询
	public List<Order> orderQuery4user(Order order);
	
	public List<Order> getNoDealedOrder();

	public Order getSingleOrder(Order order);

	
}
