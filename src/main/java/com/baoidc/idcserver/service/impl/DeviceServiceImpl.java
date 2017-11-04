package com.baoidc.idcserver.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

import com.baoid.idcserver.vo.ServerRentVo;
import com.baoidc.idcserver.cache.SerializerUtils;
import com.baoidc.idcserver.core.AES256Cipher;
import com.baoidc.idcserver.core.DateUtil;
import com.baoidc.idcserver.core.DurationUtil;
import com.baoidc.idcserver.core.ErrorCode;
import com.baoidc.idcserver.core.OrderStatus;
import com.baoidc.idcserver.core.OrderType;
import com.baoidc.idcserver.core.RedisUtil;
import com.baoidc.idcserver.core.ResponseMessage;
import com.baoidc.idcserver.core.SourceTag;
import com.baoidc.idcserver.core.SystemContant;
import com.baoidc.idcserver.core.TimeType;
import com.baoidc.idcserver.dao.AssetManageDAO;
import com.baoidc.idcserver.dao.DeviceInstanceDAO;
import com.baoidc.idcserver.dao.DeviceServerDAO;
import com.baoidc.idcserver.dao.IUserDAO;
import com.baoidc.idcserver.po.Chest;
import com.baoidc.idcserver.po.ConsumeRecord;
import com.baoidc.idcserver.po.DeviceInstance;
import com.baoidc.idcserver.po.DeviceSourceParam;
import com.baoidc.idcserver.po.InstanceIp;
import com.baoidc.idcserver.po.InstanceParam;
import com.baoidc.idcserver.po.Order;
import com.baoidc.idcserver.po.OrderParam;
import com.baoidc.idcserver.po.Remark;
import com.baoidc.idcserver.po.ServerRoom;
import com.baoidc.idcserver.po.Servers;
import com.baoidc.idcserver.po.UserAccount;
import com.baoidc.idcserver.po.UserContacts;
import com.baoidc.idcserver.service.IDeviceService;
import com.baoidc.idcserver.service.IUserAccountService;
import com.baoidc.idcserver.vo.query.ColumnIndexVO;
import com.baoidc.idcserver.vo.query.OrderQueryVO;
@Service

public class DeviceServiceImpl implements IDeviceService { //controller
	
	@Autowired
	private IUserAccountService userAccountService;
	@Autowired
	private DeviceServerDAO deviceServerDao;
	
	@Autowired
	private IUserDAO userDao;
	
	@Autowired
	private DeviceInstanceDAO instanceDao;
	
	@Autowired
	private RedisUtil redisUtil;
	
	@Autowired
	private DeviceInstanceDAO deviceInstDAO;
	
	@Autowired
	private AssetManageDAO assetManageDAO;
	
	public ServerRentVo getDeviceSourceParam() {
		
		List<ServerRoom> serverRoomList = deviceServerDao.getServerRoomList();
		
		List<DeviceSourceParam> paramList = deviceServerDao.getDeviceSourceParam();
		
		ServerRentVo serverRentVo = new ServerRentVo();
		
		serverRentVo.setServerRoomList(serverRoomList);
		serverRentVo.setSourceParamList(paramList);
		return serverRentVo;
	}
	//提交当前配置的服务产品前的验证订单各个项目里面的属性值是否正确，并计算总的价格，返回真实完整的订单信息
	public Order confirmNewOrder(Order order) {
		double price = 0;//总价格
		int count = 1;//总数量
		int duration = 1;//默认一个月总时长
		double saleNum = 1;//默认折扣为1
		//int ttlcount = 0;//总的被选中的线路
		OrderParam ttlop = new OrderParam();//线路有多项，但都属于一个订单里面，所以只需要一个对象
		OrderParam diskop = new OrderParam();//硬盘可能会有多块，但都属于一个订单里面，所以只需要一个对象
		String diskText = "";//被选中的硬盘值
		String ttlText = "";//被选中的线路值
		int diskCount = 0;//被选中的硬盘块数
		List<OrderParam> opList = order.getOrderParamList();
		List<OrderParam> newOplist = new ArrayList<OrderParam>();//完善的OrderParam List
		Jedis jedis = redisUtil.getJedis();
		jedis.select(1);
		for(int i=0;i<opList.size();i++){//每次循环都是一个项目
			OrderParam op = opList.get(i);
			String encrypt = op.getEncrypt();//加密的key
			byte[] bs = jedis.get(SerializerUtils.serialize(encrypt));
			if(bs != null){//查询有值
				
				DeviceSourceParam sourParam = (DeviceSourceParam) SerializerUtils.unserialize(bs);//获取其对应的对象DeviceSourceParam
			
				OrderParam op2 = new OrderParam();//新的orderParam
				double nprice = 0;//每一个项目计算后的价格
				String tagValue = sourParam.getTagValue();//标识：ram
				//考虑线性增长的DeviceSourceParam;带宽
				if(SourceTag.WIDTH.getTagVal().equals(tagValue) || SourceTag.IPCOUNT.getTagVal().equals(tagValue)){
					double wunitPrice = sourParam.getPrice();//单价
					int step = sourParam.getSourceParamStep();//step
					int min = sourParam.getSourceParamMin();//最小值
					int value = Integer.parseInt(op.getParamValue());//提交的值,可能带了单位，这里页面也有可能对值做了修改
					
					op2.setParamValue(op.getParamValue());//带了单位，需要判断是否页面修改了，
					
					nprice = (value-min)/step * wunitPrice;//计算价格。
					newOplist.add(op2);//放入新的list
					price += nprice;
				}
				else if(SourceTag.DISK.getTagVal().equals(tagValue)){//为硬盘，需要考虑多块硬盘的情况
					diskCount ++;
					nprice = sourParam.getPrice();
					if(diskCount >1){//到了第二块硬盘，需要组成如：120GB+1T
						diskText += ("+"+sourParam.getSourceParam());
					}else{
						diskText += sourParam.getSourceParam();
					}
					price += nprice;
					diskop.setParamValue(diskText);//说明，如：电信；如有多个，则会覆盖前面的
					diskop.setParamName(sourParam.getTagName());//类型，例如：内存,亦会覆盖
					diskop.setTagValue(sourParam.getTagValue());//标识，例如：ram，亦会覆盖
				}
				/*else if(SourceTag.IPCOUNT.getTagVal().equals(tagValue)){//为ip线性增长
					double wunitPrice = sourParam.getPrice();//单价
					int step = sourParam.getSourceParamStep();//step
					int min = sourParam.getSourceParamMin();//为页面传过来的最小值，即为选择的线路个数
					int value = Integer.parseInt(op.getParamValue());//提交的值,可能带了单位，这里页面也有可能对值做了修改
					
					op2.setParamValue(op.getParamValue());//带了单位，需要判断是否页面修改了，
					
					nprice = (value-min)/step * wunitPrice;//计算价格。
					newOplist.add(op2);//放入新的list
					price += nprice;
					
				}*/
				else if(SourceTag.TTL.getTagVal().equals(tagValue)){//线路的计算，是看数量
					ttlText += (sourParam.getSourceParam()+" ");
					/*ttlcount ++;//计数
					if(ttlcount > 1){//默认一个，免费
						nprice += sourParam.getPrice();
					}*/
					ttlop.setParamValue(ttlText);//说明，如：电信；如有多个，则会覆盖前面的
					ttlop.setParamName(sourParam.getTagName());//类型，例如：内存
					ttlop.setTagValue(sourParam.getTagValue());//标识，例如：ram
					
				}else if(SourceTag.COUNT.getTagVal().equals(tagValue)){//总数量
					count = Integer.parseInt(op.getParamValue());//提交的值,可能带了单位
					op2.setParamValue(count+"");
					newOplist.add(op2);//放入新的list
					price += nprice;
				}
				else if(SourceTag.DURATION.getTagVal().equals(tagValue)){//时长
					duration = sourParam.getSourceParamStep();
					op2.setParamValue(sourParam.getSourceParam());//说明，例如：一个月
					newOplist.add(op2);//放入新的list
					price += nprice;
					if(sourParam.getSaleNum() != 0 ){//
						saleNum = sourParam.getSaleNum();//时长上的折扣
					}
					
				}
				else{//为非线性，每一个encrypt对应的源数据存储都有
					op2.setParamValue(sourParam.getSourceParam());//带了单位
					nprice = sourParam.getPrice();
					newOplist.add(op2);//放入新的list
					price += nprice;
					
				}
				//完善其公共的属性值
				op2.setParamName(sourParam.getTagName());//类型，例如：内存
				op2.setTagValue(tagValue);//标识：ram
				
			}else{//页面被修改了
				return null;
			}
		}
		//循环结束
		if(ttlop.getTagValue() != null){//说明有这个orderparam对象
			newOplist.add(ttlop);//放入新的list
		}
		if(diskop.getTagValue() != null){//说明有这个orderparam对象
			newOplist.add(diskop);//放入新的list
		}
		
		jedis.close();
		
		if(order.getFinalPrice() == 0){//判断管理员是否给客户议价
			price = price * count * duration * saleNum;//没有议价
		}else{
			price = order.getFinalPrice();//有议价
		}
		
		order.setPrice(price);
		order.setOrderParamList(newOplist);
		
		return order;
	}
	//提交当前配置的服务产品，生成对应的类型的订单
	public Order submitNewOrder(Order order) {
		Order secOrder = null;
		//检验提交的订单项目属性值是否与之前从数据库查询出来所匹配，防止页面修改，
		//如匹配正确，则完善其里面的属性和计算价格，
		Order confirmOrder = confirmNewOrder(order);
		if(confirmOrder != null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyymmddhhmmssSS");
			
			//String remark = confirmOrder.getRemark();//订单的备注
			Date date = new Date();
			String createTime = sdf.format(date);
			String orderId = SystemContant.COMMON_CONTANT+sdf2.format(date);
			confirmOrder.setCreateTime(createTime);
			confirmOrder.setOrderId(orderId);
			String orderType = confirmOrder.getOrderType();
			
			//该订单为升级类型，则需要将价格重新计算
			if((OrderType.SERVER_UP.getOrderType()+"").equals(orderType) || "6".equals(orderType)
					|| "7".equals(orderType)){
				double sourcePrice = confirmOrder.getPrice();//当前的价格为源标价
				double callprice = getProduct2UpPrice(confirmOrder);
				confirmOrder.setSourcePrice(sourcePrice);//升级订单独有的
				confirmOrder.setPrice(callprice);
			}
			
			deviceServerDao.addOrder(confirmOrder);//新增订单
			
			/*if(remark != null){//有备注
				deviceServerDao.addRemark(remark);
			}*/
			
			List<OrderParam> paramList = confirmOrder.getOrderParamList();
			
			//判断是否为：机柜实例里面的服务器升级，如果是，则需要新增orderParam（服务器id的Tagval）
			/*if(order.getServerId() != 0){//页面提交过来的服务器id
				OrderParam serverOp = new OrderParam();
				serverOp.setOrderId(orderId);
				serverOp.setParamValue(order.getServerId()+"");
				serverOp.setParamName(SourceTag.SERVER_ID_NAME.getTagVal());//服务器id
				serverOp.setTagValue(SourceTag.SERVER_ID.getTagVal());
				paramList.add(serverOp);
			}*/
			for(int i=0;i<paramList.size();i++){//更新每个param的orderId，并insert入库
				OrderParam orderParam = paramList.get(i);
				orderParam.setOrderId(orderId);
				deviceServerDao.addOrderParam(orderParam);
			}
			confirmOrder.setOrderParamList(paramList);
			Order newOrder = deviceServerDao.getSingleOrder(confirmOrder);
			secOrder = getSecurity4Order(newOrder,1,SourceTag.SUBMITORDER_SEC.getTagVal());//加密存储
		}
		return secOrder;	
	}
	
	//升级的订单价格计算
	public double getProduct2UpPrice(Order order){
		double sprice = order.getPrice();//订单目前一个月配置的价格
		int price = (int) sprice;//订单升级返回的价格
		DeviceInstance ins = deviceInstDAO.getInstanceByInstanceId(order.getInstanceId());
		String stime = ins.getStartTime();//开始时间
		String etime = ins.getEndTime();//到期时间
		
		String sd = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(sd);//格式化
		String ntime = DateUtil.dateToStr(new Date(), sd);//当前时间
		
		Calendar sCalendar = new GregorianCalendar();
		Calendar nCalendar = new GregorianCalendar();
		Calendar eCalendar = new GregorianCalendar();
		try {
			sCalendar.setTime(sdf.parse(stime));
			nCalendar.setTime(sdf.parse(ntime));
			eCalendar.setTime(sdf.parse(etime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if((sCalendar.getTimeInMillis() - nCalendar.getTimeInMillis()) >= 0){//判断是否在服务生效开始后才升级
			//nCalendar = sCalendar;
			ntime = stime;//未生效，则将开始时间赋值给当前
		}
		
		int balance = DateUtil.getBalance(etime,ntime,sd,TimeType.DAY);//获取剩余的天数
		if(balance < 1){
			balance = 1;
		}
		//计算价格
		if(sprice > 30){
			price = (int) (sprice/30 * balance);//不会四舍五入，
		}
		return price;
	}
	
	//通过userId获取其对应的所有的订单
	public HashMap<String, List<Order>> getOrderListByUserId(int userId) {
		List<Order> orderList = deviceServerDao.getOrderListByUserId(userId);
		List<UserContacts> contactsList = userDao.getUserContactsById(userId);//获取联系人
		
		List<Order> secOrderList = new ArrayList<Order>();//加密安全的orderlist
		HashMap<String, List<Order>> map = new HashMap<String, List<Order>>();
		
		//完善订单里面需要关联的ip集合
		for(int i=0;i<orderList.size();i++){
			Order sorder = orderList.get(i);
			List<InstanceIp> ipList = new ArrayList<InstanceIp>();//订单关联的ip集合
			
			//说明此订单为机柜租用里面的服务器升级
			if(sorder.getServerId() != 0){
				Servers sv = assetManageDAO.getServerByServerId(sorder.getServerId());
				ipList.addAll(sv.getIpList());//将该实例的ip集合放入订单关联的ip集合
				
			}else{
				//获取订单关联的实例
				//获取由订单产生的实例，by orderId---即该订单是创建实例时候生成的订单；那么订单id存在于实例对象中,因为一个订单可能会生成多个实例，所以需要遍历
				List<DeviceInstance> diList = instanceDao.getInstanceByorderId(sorder.getOrderId());
				if(diList != null && diList.size() != 0){//说明有这样的订单
					for(int j=0;j<diList.size();j++){
						ipList.addAll(diList.get(j).getIpList());//将该实例的ip集合放入订单关联的ip集合
					}
				}
				//获取为升级的订单关联的实例,
				int instanceId = sorder.getInstanceId();
				//DeviceInstance instance = new DeviceInstance();
				if(instanceId != 0){//说明该订单为普通的实例升级或者续费而来
					DeviceInstance instance = instanceDao.getInstanceByInstanceId(instanceId);
					if(instance != null){
						List<InstanceIp> instanceIpList = instance.getIpList();
						if(instanceIpList != null && instanceIpList.size() > 0){
							ipList.addAll(instance.getIpList());//将该实例的ip集合放入订单关联的ip集合
						}
					}
					sorder.setDeviceInstance(instance);//订单详情的时候需要
				}
			}
			
			//设置订单关联的ip集合
			sorder.setIpList(ipList);
			//每个订单都有联系人集合
			sorder.setUserContactsList(contactsList);
			//存入redis中
			Order secOrder = getSecurity4Order(sorder,1,SourceTag.SUBMITORDER_SEC.getTagVal());//订单加密
			secOrderList.add(secOrder);
		}
		
		map.put("aaData", secOrderList);//返回给datatable特定类型的数据
		return map;
	}
	//通过服务产品的id获取对应的购买页面信息
	public ServerRentVo getServiceProduct4page(int productId) {
		
		//List<ServerRoom> serverRoomList = deviceServerDao.getServerRoomList();
		List<DeviceSourceParam> paramList = deviceServerDao.getServiceProductByProductId(productId);
		//将产品放入redis,其中加密后的id为key
		Jedis jedis = redisUtil.getJedis();
		AES256Cipher aes = null;
		try {
			  aes = new AES256Cipher();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		jedis.select(1);//
		for(int i=0;i<paramList.size();i++){
			DeviceSourceParam param = paramList.get(i);
			String encrypt = null;
			try {
				encrypt = aes.encrypt(param.getId()+"", SourceTag.NEWORDER_SEC.getTagVal(), "");//key
			} catch (Exception e) {
				e.printStackTrace();
			}
			param.setEncrypt(encrypt);//放入页面
			jedis.set(SerializerUtils.serialize(encrypt),SerializerUtils.serialize(param));//将此对象放入jedis
			
		}
		jedis.close();
		ServerRentVo serverRentVo = new ServerRentVo();
		//serverRentVo.setServerRoomList(serverRoomList);
		
		
		serverRentVo.setSourceParamList(paramList);
		
		return serverRentVo;
	}
	//支付订单
	public ResponseMessage payProductOrder(Order order) {
		ResponseMessage res = null;
		//验证订单,在取出订单时候每一个订单都已存在redis中，且带有标识
		Order secOrder = confirmPayOrderSec(order,1);
		if(secOrder != null){//订单正确
			ConsumeRecord consumeRecord = new ConsumeRecord();
			UserAccount userAccount = userAccountService.getUserAccountInfo(secOrder.getUserId());//获取当前账户信息
			Double balance = userAccount.getAccountBalance();
			DeviceInstance deviceInst = null;
			int instanceId = secOrder.getInstanceId();
			if(instanceId != 0 ){
				deviceInst = deviceInstDAO.getInstanceByInstanceId(instanceId);
			}
			if(balance < secOrder.getPrice()){//余额不足
				res = new ResponseMessage(ErrorCode.BALANCE_NOT_ENOUGH);
			}else{//更新订单状态
				//新增支付时间
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String payTime = sdf.format(new Date());
				
				String orderType = secOrder.getOrderType();//订单类型
				if(orderType.equals("1") || orderType.equals("2") || orderType.equals("3") || orderType.equals("5")){//为创建：服务器租用、设备托管、机柜租用、服务续费
					//需要新增订单的开始时间、结束时间
					List<OrderParam> orderParams = secOrder.getOrderParamList();
					int duration = getOrderDuration(orderParams);
					String endTime = DateUtil.getExpireDate(payTime, duration);//支付时间即为开始时间
					secOrder.setStartTime(payTime);
					secOrder.setEndTime(endTime);
				}
				
				secOrder.setPayTime(payTime);
				secOrder.setStatus(OrderStatus.PAIED.getStatusVal());
				//secOrder.setInstanceId("");//给mybatise判断用,创建需要更新订单的开始时间和结束 时间
				deviceServerDao.updateOrderStatus(secOrder);
				//更新账户信息，生成一条消费记录和更新账户信息
				consumeRecord.setAccountId(userAccount.getAccountId());
				consumeRecord.setConsumeAmt(secOrder.getPrice());
				consumeRecord.setOrderNo(secOrder.getOrderId());
				
				int serviceId = 0;
				int recordInsType = 0;//
				if(deviceInst != null){//为续费或者升级，则instanceType为实例关联的serviceId
					serviceId = deviceInst.getServiceId();//大类型：设备托管
					switch (serviceId) {
					case 1:
						recordInsType = 1;//设备租用
						break;
					case 2:
						recordInsType = 2;//设备托管
						break;
					case 3:
						recordInsType = 3;//机柜租用
						break;
					default:
						break;
					}
				}else{//为创建，则为订单类型即可，但是数据类型不同，所以需要转化下
					String orderType2 = secOrder.getOrderType();
					switch (orderType2) {
					case "1":
						recordInsType = 1;//设备租用
						break;
					case "2":
						recordInsType = 2;//设备托管
						break;
					case "3":
						recordInsType = 3;//机柜租用
						break;
					default:
						break;
					}
					
				}
				consumeRecord.setInstanceType(recordInsType);
				consumeRecord.setOrderType(secOrder.getOrderType());
				consumeRecord.setUserId(userAccount.getUserId());
				consumeRecord.setProductName(secOrder.getProductName());
				userAccountService.addNewConsumeRecord(consumeRecord);
				res = new ResponseMessage(ErrorCode.PAY_SUCCESS);
			}
		}else{
			res = new ResponseMessage(ErrorCode.PAY_FAIL);//订单被修改了，redis中未匹配到
		}
		return res;
	}
	//批量支付
	public ResponseMessage payProductOrderList(List<Order> orderList) {
		ResponseMessage resm = new ResponseMessage();
		if(orderList.size() != 0){
			for(int i=0;i<orderList.size();i++){
				ResponseMessage res = payProductOrder(orderList.get(i));
				if(res.getFlag().equals("E")){//订单被修改了，redis中未匹配到
					resm.setFlag("E");
					return resm;
				}
				if(res.getFlag().equals("N")){//余额不足 了，后面的不给支付
					resm.setFlag("N");
					return resm;//跳出
				}
				if(res.getFlag().equals("Y")){//余额不足 了，后面的不给支付
					resm.setFlag("Y");
				}
			}
		}else{
			resm.setFlag("E");
		}
		
		return resm;
	}
	
	
	//加密存储！将从数据库中取出来的订单order加密放入redis中。保证再次从页面获取出来为同一对象,
	public Order getSecurity4Order(Order order, int storeIndex, String secKey){
		//加密放入redis
		Jedis jedis = redisUtil.getJedis();
		AES256Cipher aes = null;
		try {
			  aes = new AES256Cipher();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		jedis.select(storeIndex);//
			String encrypt = null;
			try {
				encrypt = aes.encrypt(order.getOrderId(), secKey, "");//key
			} catch (Exception e) {
				e.printStackTrace();
			}
			order.setEncrypt(encrypt);//将唯一标识放入对象中至页面
			jedis.set(SerializerUtils.serialize(encrypt),SerializerUtils.serialize(order));//将此对象放入jedis
			
		jedis.close();
		
		return order;
		
	}
	//解密取出！对支付订单时的订单对象的验证
	public Order confirmPayOrderSec(Order order,int storeIndex) {
		Order secOrder = null;
		Jedis jedis = redisUtil.getJedis();
		jedis.select(storeIndex);
		String encrypt = order.getEncrypt();//加密的key
		byte[] bs = jedis.get(SerializerUtils.serialize(encrypt));
		if(bs != null){//有这个对象
			secOrder = (Order) SerializerUtils.unserialize(bs);//获取其对应的对象DeviceSourceParam
		}
		return secOrder;
	}
	
	//删除订单
	public ResponseMessage deleteOrder(Order order) {
		deviceServerDao.deleteOrder(order);
		ResponseMessage res = new ResponseMessage(ErrorCode.DEL_ORDER_FAIL);
		return res;
	}
	
	//管理员查询订单列表
	public Map<String, List<Order>> getOrders() {
		Map<String, List<Order>> orderMap = new HashMap<String, List<Order>>();
		List<Order> orderList = deviceServerDao.getOrders();
		
		orderMap.put("aaData", orderList);
		return orderMap;
	}
	
	//管理员获取订单列表（只查询）
	public Map<String, List<Order>> getOrdersForManage(OrderQueryVO orderQueryVO) {
		Map<String, List<Order>> orderMap = new HashMap<String, List<Order>>();
		List<Order> orderList = deviceServerDao.getOrdersForManage(orderQueryVO);
		
		//遍历查询给每个订单联系人
		for(int i=0;i<orderList.size();i++){
			Order order = orderList.get(i);
			List<UserContacts> contacts = userDao.getUserContactsById(order.getUserId());
			order.setUserContactsList(contacts);
			
			int instanceId = order.getInstanceId();
			DeviceInstance instance = new DeviceInstance();
			if(instanceId != 0){//这个订单关联实例的
				instance = instanceDao.getInstanceByInstanceId(instanceId);
			}
			//不论是否关联，都需要set一个，这样防止报错
			order.setDeviceInstance(instance);
			
		}
		//完善订单里面需要关联的ip集合
		for(int i=0;i<orderList.size();i++){
			Order sorder = orderList.get(i);
			List<InstanceIp> ipList = new ArrayList<InstanceIp>();//订单关联的ip集合
			
			//说明此订单为机柜租用里面的服务器升级
			if(sorder.getServerId() != 0){
				Servers sv = assetManageDAO.getServerByServerId(sorder.getServerId());
				ipList.addAll(sv.getIpList());//将该实例的ip集合放入订单关联的ip集合
				
			}else{
				//获取订单关联的实例
				//获取由订单产生的实例，by orderId---即该订单是创建实例时候生成的订单；那么订单id存在于实例对象中,因为一个订单可能会生成多个实例，所以需要遍历
				List<DeviceInstance> diList = instanceDao.getInstanceByorderId(sorder.getOrderId());
				if(diList != null && diList.size() != 0){//说明有这样的订单
					for(int j=0;j<diList.size();j++){
						ipList.addAll(diList.get(j).getIpList());//将该实例的ip集合放入订单关联的ip集合
					}
				}
				//获取为升级的订单关联的实例,
				int instanceId = sorder.getInstanceId();
				//DeviceInstance instance = new DeviceInstance();
				if(instanceId != 0){//说明该订单为普通的实例升级或者续费而来
					DeviceInstance instance = instanceDao.getInstanceByInstanceId(instanceId);
					if(instance != null){
						List<InstanceIp> instanceIpList = instance.getIpList();
						if(instanceIpList != null && instanceIpList.size() > 0){
							ipList.addAll(instance.getIpList());//将该实例的ip集合放入订单关联的ip集合
						}
					}
					sorder.setDeviceInstance(instance);//订单详情的时候需要
				}
			}
			//设置订单关联的ip集合
			sorder.setIpList(ipList);
		}
		
		orderMap.put("aaData", orderList);
		return orderMap;
	}
	
	
	// 提交存储要显示的列名
	public ResponseMessage saveColumnIndex4Table(ColumnIndexVO columnIndexVO) {
		ResponseMessage res = new ResponseMessage();
		//加密放入redis
		Jedis jedis = redisUtil.getJedis();
		AES256Cipher aes = null;
		try {
			  aes = new AES256Cipher();
		jedis.select(1);//redis的库
		String ttkey = columnIndexVO.getUrl();
		String encrypt =aes.encrypt(ttkey, SourceTag.COLUMN_INDEX.getTagVal(), "");//key//redis的key
		jedis.set(SerializerUtils.serialize(encrypt),SerializerUtils.serialize(columnIndexVO));//将此对象放入jedis
		jedis.close();
		}catch (Exception e) {
			e.printStackTrace();
		} 
		
		res.setFlag("Y");
		return res;
	}
	// 获取存储要显示的列名
	public ResponseMessage getColumnIndex4Table(ColumnIndexVO columnIndexVO) {
		ResponseMessage res = new ResponseMessage();
		//加密放入redis
		Jedis jedis = redisUtil.getJedis();
		AES256Cipher aes = null;
		try {
			  aes = new AES256Cipher();
		jedis.select(1);//redis的库
		String ttkey = columnIndexVO.getUrl();
		String encrypt =aes.encrypt(ttkey, SourceTag.COLUMN_INDEX.getTagVal(), "");//key//redis的key
		byte[] bs = jedis.get(SerializerUtils.serialize(encrypt));//取出
		if(bs != null){//有这个对象
			ColumnIndexVO coIndex = (ColumnIndexVO) SerializerUtils.unserialize(bs);//获取其对应的对象
			res.setFlag("Y");
			res.setObj(coIndex);
		}else{
			res.setFlag("N");
		}
		jedis.close();
		}catch (Exception e) {
			e.printStackTrace();
		} 
		return res;
	}
//	查询未支付订单个数
	public int getNoPayOrder(int userId) {
		return deviceServerDao.getNoPayOrder(userId);
	}
	//查询即将过期的订单个数
	public int getExpireOrder(int userId) {
		return 0;
		//return deviceServerDao.getExpireOrder(userId);
	}
	
	
	//获取订单时长
	private int getOrderDuration(List<OrderParam> orderParams){
		int duration = 1;
		for (OrderParam orderParam : orderParams) {
			String tagValue = orderParam.getTagValue();
			if((SourceTag.DURATION.getTagVal()).equalsIgnoreCase(tagValue)){
				String durationStr = orderParam.getParamValue();
				duration = DurationUtil.durationStrToNum(durationStr);
			}
		}
		return duration;
	}
	
	//获取实例时长
	private int getInstDuration(List<InstanceParam> instParams){
		int duration = 1;
		for (InstanceParam instParam : instParams) {
			String tagValue = instParam.getTagValue();
			if((SourceTag.DURATION.getTagVal()).equalsIgnoreCase(tagValue)){
				String durationStr = instParam.getParamValue();
				duration = DurationUtil.durationStrToNum(durationStr);
			}
		}
		return duration;
	}
	//订单的条件查询
	@Override
	public List<Order> orderQuery4user(Order order) {
		List<Order> orderList = deviceServerDao.getOrderList4Query(order);//条件查询
		List<UserContacts> contactsList = userDao.getUserContactsById(order.getUserId());//获取联系人
		
		List<Order> secOrderList = new ArrayList<Order>();//加密安全的orderlist
		
		//完善订单里面需要关联的ip集合
		for(int i=0;i<orderList.size();i++){
			Order sorder = orderList.get(i);
			List<InstanceIp> ipList = new ArrayList<InstanceIp>();//订单关联的ip集合
			
			//说明此订单为机柜租用里面的服务器升级
			if(sorder.getServerId() != 0){
				Servers sv = assetManageDAO.getServerByServerId(sorder.getServerId());
				if(sv != null){
					ipList.addAll(sv.getIpList());//将该实例的ip集合放入订单关联的ip集合
				}
				
			}else{
				//获取订单关联的实例
				//获取由订单产生的实例，by orderId---即该订单是创建实例时候生成的订单；那么订单id存在于实例对象中,因为一个订单可能会生成多个实例，所以需要遍历
				List<DeviceInstance> diList = instanceDao.getInstanceByorderId(sorder.getOrderId());//已经查询出通过实例的device_id关联的ip集合、机柜除外
				if(diList != null && diList.size() != 0){//说明有这样的订单
					for(int j=0;j<diList.size();j++){
						if((OrderType.RACK_RENT.getOrderType()+"").equals(sorder.getOrderType())){//为机柜租用的订单，即该订单关联的实例所关联的ip集合是通过chestId获取的
							//通过chestId获取chest,得到ip集合
							Chest chest = assetManageDAO.getChestByChestId(diList.get(j).getChestId());
							if(chest != null){
								diList.get(j).setIpList(chest.getIpList());//
							}
						}
						
						ipList.addAll(diList.get(j).getIpList());//将该实例的ip集合放入订单关联的ip集合
					}
				}
				//获取为升级的订单关联的实例,
				int instanceId = sorder.getInstanceId();
				//DeviceInstance instance = new DeviceInstance();
				if(instanceId != 0){//说明该订单为普通的实例升级或者续费而来
					DeviceInstance instance = instanceDao.getInstanceByInstanceId(instanceId);
					if(instance != null){
						List<InstanceIp> instanceIpList = instance.getIpList();
						if(instanceIpList != null && instanceIpList.size() > 0){
							ipList.addAll(instance.getIpList());//将该实例的ip集合放入订单关联的ip集合
						}
					}
					sorder.setDeviceInstance(instance);//订单详情的时候需要
				}
			}
			//设置订单关联的ip集合
			sorder.setIpList(ipList);
			//每个订单都有联系人集合
			sorder.setUserContactsList(contactsList);
			
			//最后验证当前订单对象关联的ip与页面条件查询的ip对象  
			String queryIp = order.getQueryIpstr();
			if( queryIp != null && queryIp != ""){
				queryIp = queryIp.trim();//去掉首尾空格
				for(int j=0;j<ipList.size();j++){
					String ipStr = ipList.get(j).getIpStr();//如192.168.3.3
					if(ipStr != null && ipStr.contains(queryIp)){
						//存入redis中
						Order secOrder = getSecurity4Order(sorder,1,SourceTag.SUBMITORDER_SEC.getTagVal());//订单加密
						secOrderList.add(secOrder);
					}
				}
			}else{
				//存入redis中
				Order secOrder = getSecurity4Order(sorder,1,SourceTag.SUBMITORDER_SEC.getTagVal());//订单加密
				secOrderList.add(secOrder);
			}
			
		}
		
		return secOrderList;
	}
	@Override
	public List<Order> getNoDealedOrder() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Order getSingleOrder(Order order) {
		return deviceServerDao.getSingleOrder(order);
	}
	
	
	
	
	
	
	
	
}
