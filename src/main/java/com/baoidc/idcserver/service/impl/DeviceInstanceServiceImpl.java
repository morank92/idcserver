package com.baoidc.idcserver.service.impl;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

import com.baoid.idcserver.vo.CreateDeviceInstanceVO;
import com.baoid.idcserver.vo.DeviceInstanceVO;
import com.baoidc.idcserver.cache.SerializerUtils;
import com.baoidc.idcserver.core.AES256Cipher;
import com.baoidc.idcserver.core.DateUtil;
import com.baoidc.idcserver.core.DurationUtil;
import com.baoidc.idcserver.core.OrderStatus;
import com.baoidc.idcserver.core.RedisUtil;
import com.baoidc.idcserver.core.ResponseMessage;
import com.baoidc.idcserver.core.SourceTag;
import com.baoidc.idcserver.core.SystemContant;
import com.baoidc.idcserver.dao.AssetManageDAO;
import com.baoidc.idcserver.dao.DeviceInstanceDAO;
import com.baoidc.idcserver.dao.DeviceServerDAO;
import com.baoidc.idcserver.dao.IUserAccountDAO;
import com.baoidc.idcserver.dao.IUserDAO;
import com.baoidc.idcserver.po.AutoRenewConf;
import com.baoidc.idcserver.po.Chest;
import com.baoidc.idcserver.po.ConsumeRecord;
import com.baoidc.idcserver.po.DeviceInstance;
import com.baoidc.idcserver.po.DeviceSourceParam;
import com.baoidc.idcserver.po.InstanceIp;
import com.baoidc.idcserver.po.InstanceParam;
import com.baoidc.idcserver.po.Order;
import com.baoidc.idcserver.po.OrderParam;
import com.baoidc.idcserver.po.ProductInstance;
import com.baoidc.idcserver.po.ServerNum;
import com.baoidc.idcserver.po.Servers;
import com.baoidc.idcserver.po.User;
import com.baoidc.idcserver.po.UserAccount;
import com.baoidc.idcserver.service.IDeviceInstanceService;
import com.baoidc.idcserver.service.IDeviceService;
import com.baoidc.idcserver.service.IUserAccountService;
import com.baoidc.idcserver.vo.query.InstanceQuery;
import com.baoidc.idcserver.vo.query.RenewVO;
@Service
public class DeviceInstanceServiceImpl implements IDeviceInstanceService { //controller
	
	/*@Autowired
	private IUserAccountService userAccountService;*/
	@Autowired
	private DeviceInstanceDAO deviceInstanceDao;
	
	@Autowired
	private DeviceServerDAO deviceServerDAO;
	
	@Autowired
	private AssetManageDAO assetManageDAO;
	@Autowired
	private RedisUtil redisUtil;
	
	@Autowired
	private DeviceServerDAO deviceServerDao;
	
	@Autowired
	private IDeviceService deviceService;
	
	@Autowired
	private IUserDAO userDAO;
	
	@Autowired
	private IUserAccountService userAccountService;
	
	@Autowired
	private IUserAccountDAO userAccountDAO;
	

	//获取用户所有的产品实例--
	public HashMap<String, List<DeviceInstance>> getInstanceListByUserId(int userId) {
		HashMap<String,List<DeviceInstance>> map = new HashMap<String, List<DeviceInstance>>();
		List<DeviceInstance> instanceList = deviceInstanceDao.getInstanceListByUserId(userId);
		
		for(int i=0;i<instanceList.size();i++){
			DeviceInstance instance = instanceList.get(i);
			List<InstanceParam> paramList = instance.getInstanceParamList();
			
			Class<?> clazz;
			try {
				clazz = Class.forName("com.baoidc.idcserver.po.ProductInstance");//获取大class
				ProductInstance productInstance = (ProductInstance) clazz.newInstance();//获取此大class的实例
				for(int j=0;j<paramList.size();j++){
						InstanceParam param = paramList.get(j);
						String tagName = param.getTagValue();
						String setName = "set"+tagName.substring(0,1).toUpperCase()+tagName.substring(1);//构建如“setModel”方法名
						Method setMethod = clazz.getMethod(setName, String.class);//获取大class的方法
						setMethod.invoke(productInstance, param.getParamValue());//执行此实例的set方法
					}
				
				instance.setProductInstance(productInstance);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		map.put("aaData", instanceList);
		return map;
	}

	//获取其对应的所有的产品实例by userId、serviceId
	public List<DeviceInstance> getInstanceList(
			DeviceInstance deviceInstance) {
		
		List<DeviceInstance> instanceList = deviceInstanceDao.getInstanceList(deviceInstance);
		
		for(int i=0;i<instanceList.size();i++){
			DeviceInstance instance = instanceList.get(i);
			List<InstanceParam> paramList = instance.getInstanceParamList();
			ProductInstance productIns = generateProdInst(paramList);//转化为对象
			instance.setProductInstance(productIns);
			
			List<InstanceIp> chestIpList = new ArrayList<InstanceIp>();//机柜上的ip集合、该实例为机柜时
			int chestId = instance.getChestId();
			if(chestId != 0){//当前实例为机柜,则它的ip集合需要从机柜中获取
				chestIpList = deviceInstanceDao.getInstanceIpByChestId(chestId);
				instance.setIpList(chestIpList);
			}
			
			//将获取到的完整Instance存入redis
			instance = setSecurity4Instance(instance,1,SourceTag.GETINSTANCE_SEC.getTagVal());
		}
		return instanceList;
	}
	
	//条件查询实例，如：ip地址
	public List<DeviceInstance> queryInstance(InstanceQuery instanceQuery) {
		List<DeviceInstance> instanceList = new ArrayList<DeviceInstance>();
		//如果为ip查询则需要从另外一张表先查
		String ipStr = instanceQuery.getIpStr();
		//因为为模糊查询所以--暂时未采用
		//ipStr = "%"+ipStr+"%";
		
		InstanceIp instanceIp  = deviceInstanceDao.getInstanceIpByIpstr(ipStr);
		if(instanceIp != null){
			//获取对应的实例:ip是关联设备的，设备再关联实例的。但是ip表里存在deviceId，实例也存在deviceId
			DeviceInstance instance = deviceInstanceDao.getInstanceByDeviceId(instanceIp.getInstanceId());
			if(instance != null){
				//判断此实例是否符合当次的条件查询
				int qserviceId = instanceQuery.getServiceId();
				int quserId = instanceQuery.getUserId();
				
				int userId = instance.getUserId();
				int serviceId = instance.getServiceId();
				
				if(qserviceId == serviceId && quserId == userId){//即查询出来的对象符合当次的查询
					List<InstanceParam> paramList = instance.getInstanceParamList();
					//构建对象ProductInstance
					ProductInstance productIns = generateProdInst(paramList);//转化为对象
					instance.setProductInstance(productIns);
					//将获取到的完整Instance存入redis
					instance = setSecurity4Instance(instance,1,SourceTag.GETINSTANCE_SEC.getTagVal());
					//并入集合返回
					instanceList.add(instance);
				}
			}
		}
		
		return instanceList;
	}

	//加密存储！将从数据库中取出来的订单order加密放入redis中。保证再次从页面获取出来为同一对象,
	public DeviceInstance setSecurity4Instance(DeviceInstance instance, int storeIndex, String secKey){
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
				encrypt = aes.encrypt(instance.getInstanceId()+"", secKey, "");//key
			} catch (Exception e) {
				e.printStackTrace();
			}
			instance.setEncrypt(encrypt);//将唯一标识放入对象中至页面
			jedis.set(SerializerUtils.serialize(encrypt),SerializerUtils.serialize(instance));//将此对象放入jedis
			
		jedis.close();//必须得关闭
		
		return instance;
		
	}
	//解密取出
	public DeviceInstance getSecurity4Instance(DeviceInstance instance,int storeIndex) {
		DeviceInstance secInstance = null;
		Jedis jedis = redisUtil.getJedis();
		jedis.select(storeIndex);
		String encrypt = instance.getEncrypt();//加密的key
		byte[] bs = jedis.get(SerializerUtils.serialize(encrypt));
		if(bs != null){//有这个对象
			secInstance = (DeviceInstance) SerializerUtils.unserialize(bs);//获取其对应的对象DeviceSourceParam
		}
		jedis.close();//必须得关闭
		return secInstance;
	}
	
	
	//获取实例by instanceId
	public DeviceInstance getInstanceByInstanceId(int instanceId) {
		DeviceInstance instance = deviceInstanceDao.getInstanceByInstanceId(instanceId);
		return instance;
	}

	//从匹配的列表中选择出设备后。创建产品实例
	public void generationInstance(CreateDeviceInstanceVO createInstanceVO) {
		String orderId = createInstanceVO.getOrderId();//订单id
		Order order = deviceServerDAO.getSingleOrderByOrderId(orderId);
		int userId = order.getUserId();
		User user = new User(); 
		user.setId(userId);
		List<OrderParam> orderParams =  order.getOrderParamList();
		int count = getInstanceCount(orderParams);
		int duration = getOrderDuration(orderParams);//时长
		OrderParam opRoom = getTagValByTag(orderParams,SourceTag.ROOM.getTagVal());//通过标签获取该标签对象
		String instRoom = opRoom.getParamValue();//机房
		
		String startTime = DateUtil.dateToStr2(new Date(), "yyyy-MM-dd");//返回第二天：yyyy-MM-dd 00:00:00
		
		String endTime = DateUtil.getExpireDate(startTime, duration);//获取到期时间
		List<ServerNum> serviceNumList = createInstanceVO.getNumbers();
		if(serviceNumList != null){
			for (ServerNum serverNum : serviceNumList) {//选择的资产，
				DeviceInstance deviceInstance = new DeviceInstance();
				deviceInstance.setServiceId(order.getServiceId());
				deviceInstance.setInstanceType(Integer.parseInt(order.getOrderType()));
				deviceInstance.setUserId(order.getUserId());
				deviceInstance.setPrice(order.getPrice() / count);//总价
				deviceInstance.setSourcePrice(order.getPrice() / (count*duration));//源价格，配置的价格
				deviceInstance.setStartTime(startTime);
				deviceInstance.setEndTime(endTime);
				deviceInstance.setStatus(0);
				deviceInstance.setRoom(instRoom);//机房
				
				int serverId = serverNum.getServerId(); //获取传过来的资产实例ID
				//构建实例关联设备的参数
				String deviceName = "";//实例的设备类型
				String deviceNum = "";//实例的设备编号
				int deviceId = 0;//实例关联的设备id
				Servers server = new Servers();//初始。
				
				if(order.getOrderType().equals("3")){//如果为机柜租用，
					Chest chest = assetManageDAO.getChestByChestId(serverId);
					deviceName = "机柜";
					deviceNum = chest.getChestNumber();
					deviceId = chest.getChestId();
					
					deviceInstance.setChestId(deviceId);//关联的机柜id
					
					//更新资产状态
					chest.setUser(user);
					chest.setUseStatus(2);
					assetManageDAO.updateChest(chest);
				}else{//更新资产状态
					server = assetManageDAO.getServerByServerId(serverId);//获取资产
					deviceName = server.getDeviceType();
					deviceNum = server.getServerNumber();
					deviceId = server.getServerId();
					
					deviceInstance.setDeviceId(deviceId);//关联的设备id
					
					//更新资产状态
					server.setUser(user);
					server.setUseStatus(1);
					assetManageDAO.updateServers(server);
				}
				deviceInstance.setOrderId(orderId);//实例关联订单的id
				deviceInstance.setDeviceName(deviceName);//设备类型名称
				deviceInstance.setInstanceNum(deviceNum);//设备编号
				
				//生成实例
				deviceInstanceDao.generateNewInstance(deviceInstance);//已返回主键
				
				//新增instanceParam,但是需要排除‘数量’选项
				List<InstanceParam> instanceParams = new ArrayList<InstanceParam>();
				for (OrderParam orderParam : orderParams) {
					//判断是否为“数量”
					if(SourceTag.COUNT.getTagVal().equals(orderParam.getTagValue())){//则跳出此次，下一个继续
						continue;
					}else{
						InstanceParam instParam = new InstanceParam();
						instParam.setInstanceId(deviceInstance.getInstanceId());//主键关联
						instParam.setParamName(orderParam.getParamName());
						instParam.setParamValue(orderParam.getParamValue());
						instParam.setTagValue(orderParam.getTagValue());
						instanceParams.add(instParam);
					}
					
				}
				
				if(order.getOrderType().equals("2")){ //服务器托管需要将服务器信息加到服务器托管实例中
					//获取服务器型号信息
					String serverDetail = server.getServerDetail();
					InstanceParam serverDetailInstParam = new InstanceParam();
					serverDetailInstParam.setInstanceId(deviceInstance.getInstanceId());
					serverDetailInstParam.setParamName("型号");
					serverDetailInstParam.setParamValue(serverDetail);
					serverDetailInstParam.setTagValue("model");
					instanceParams.add(serverDetailInstParam);
					//获取内存信息
					String memory = server.getShowMemory();
					InstanceParam memoryInstParam = new InstanceParam();
					memoryInstParam.setInstanceId(deviceInstance.getInstanceId());
					memoryInstParam.setParamName("内存");
					memoryInstParam.setParamValue(memory);
					memoryInstParam.setTagValue("ram");
					instanceParams.add(memoryInstParam);
					//获取磁盘信息
					String disk = server.getShowDisk();
					InstanceParam diskInstParam = new InstanceParam();
					diskInstParam.setInstanceId(deviceInstance.getInstanceId());
					diskInstParam.setParamName("硬盘");
					diskInstParam.setParamValue(disk);
					diskInstParam.setTagValue("disk");
					instanceParams.add(diskInstParam);
				}
				
				//生成实例参数
				deviceInstanceDao.addInstanceParams(instanceParams);
				
			}
		}
		//更新订单状态
		order.setStatus(2);
		deviceServerDAO.updateOrderStatus(order);
	}
	
	//通过tag获取对应的值
	public OrderParam getTagValByTag(List<OrderParam> opList,String tag){
		OrderParam op = new OrderParam();
		for(int i=0;i<opList.size();i++){
			OrderParam op2 = opList.get(i);
			if(tag.equals(op2.getTagValue())){
				op = op2;
			}
		}
		return op;
	}

	//更新产品实例属性（升级）
	public void updateInstance(String orderId) {
		if(orderId != null && StringUtils.isNotBlank(orderId)){
			Order order = deviceServerDAO.getSingleOrderByOrderId(orderId);
			if(order != null){
				int instanceId = order.getInstanceId();//此订单关联的实例id
				if(order.getOrderType().equals("6")){//为机柜租用里面的服务器ddos升级
					
				}else{
					
					//更新实例参数
					List<OrderParam> orderParamList = order.getOrderParamList();
					List<InstanceParam> instanceParamList = new ArrayList<InstanceParam>();
					for (OrderParam orderParam : orderParamList) {
						
						String tagValue = orderParam.getTagValue();
						
						InstanceParam instanceParam = new InstanceParam();
						instanceParam.setInstanceId(instanceId);
						instanceParam.setTagValue(orderParam.getTagValue());
						
						instanceParam = deviceInstanceDao.getInstParam(instanceParam);
						String paramValue = "";
						
						if(SourceTag.DISK.getTagVal().equalsIgnoreCase(tagValue)){ //硬盘
							
							paramValue = instanceParam.getParamValue() + "+" + orderParam.getParamValue();
							
						}else if(SourceTag.RAM.getTagVal().equalsIgnoreCase(tagValue)){ //内存
							String orderParamVal = orderParam.getParamValue();
							String instParamVal = instanceParam.getParamValue();
							String orderParamNum = orderParamVal.substring(0,orderParamVal.indexOf("G"));
							String instParamNum = instParamVal.substring(0,instParamVal.indexOf("G"));
							String instParamUnit = instParamVal.substring(instParamVal.indexOf("G"),instParamVal.length());
							StringBuilder paramValBuilder = new StringBuilder();
							int paramValNum = Integer.parseInt(orderParamNum) + Integer.parseInt(instParamNum);
							paramValBuilder.append(paramValNum).append(instParamUnit);
							paramValue = paramValBuilder.toString();
						}else{
							paramValue = orderParam.getParamValue();
						}
						
						instanceParam.setParamName(orderParam.getParamName());
						instanceParam.setParamValue(paramValue);
						
						instanceParamList.add(instanceParam);
					}
					deviceInstanceDao.updateInstanceParam(instanceParamList);
					
				}
				//更新实例价格--包含：实例的源价格
				double orderPrice = order.getPrice();
				double soPrice = order.getSourcePrice();//升级订单的标配价格
				DeviceInstance deviceInstance = deviceInstanceDao.getInstanceByInstanceId(instanceId);
				if(deviceInstance != null){
					double instancePrice = deviceInstance.getPrice();//实例的总消费价格
					double sPrice = deviceInstance.getSourcePrice();//实例的标配原价格
					instancePrice = instancePrice + orderPrice;//总消费价格
					sPrice = sPrice + soPrice;//该实例的原价格
					deviceInstance.setPrice(instancePrice);
					deviceInstance.setSourcePrice(sPrice);
					deviceInstanceDao.updateInstance(deviceInstance);
				}
				
				//更新订单状态
				order.setStatus(2);
				deviceServerDAO.updateOrderStatus(order);
			}
		}
	}

	//创建实例续费的订单,并且支付
	public List<Order> submitRenewOrder(RenewVO renewvo) {
		
		List<Order> orderList = new ArrayList<Order>();
		List<OrderParam> paramList = new ArrayList<OrderParam>();
		
		String[] instanceEncrypt = renewvo.getInstanceEncrypt();
		String durationEncrypt = renewvo.getDurationEncrypt();//仅有一个时长duixiang信息
		int countDur = 1;//时长
		String durName = "";//时长：一个月
		String tagName = "";
		String tagValue = "";
		
		Jedis jedis = redisUtil.getJedis();
		jedis.select(1);
		//取出选择的源时长对象
		byte[] bs = jedis.get(SerializerUtils.serialize(durationEncrypt));
		jedis.close();
		
		if(bs != null){//查询有值
			DeviceSourceParam sourParam = (DeviceSourceParam) SerializerUtils.unserialize(bs);//获取其对应的对象DeviceSourceParam
			countDur = sourParam.getSourceParamStep();//时长：1
			durName = sourParam.getSourceParam();//如：一个月，
			tagName = sourParam.getTagName();//为：时长
			tagValue = sourParam.getTagValue();//为：duration，标识
		}
		
		//查询出被选择的实例,并为每一个实例生成一个订单
		//此订单只有一个OrderParam
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddhhmmssSS");
		
		//总的价格
		//double countPrice = 0;
		for(int i=0;i<instanceEncrypt.length;i++){
			
			Date date = new Date();
			
			DeviceInstance instance = new DeviceInstance();
			Order order = new Order();//当前订单
			
			instance.setEncrypt(instanceEncrypt[i]);
			//取出对应的完整实例对象
			DeviceInstance secInstance = getSecurity4Instance(instance,1);
			double secPrice = secInstance.getSourcePrice();//实例的源价格
			order.setPrice(secPrice * countDur);//续费时长计算后的价格
			//countPrice += (secPrice * countDur);//总的价格
			
			String createTime = sdf.format(date);//创建时间
			String orderId = SystemContant.COMMON_CONTANT+sdf2.format(date);//订单号,防止订单号重复
			order.setCreateTime(createTime);
			order.setOrderId(orderId);
			order.setUserId(renewvo.getUserId());
			order.setInstanceId(secInstance.getInstanceId());
			order.setStatus(OrderStatus.UNPAY.getStatusVal());
			order.setOrderType(renewvo.getOrderType()+"");//订单类型
			//deviceServerDao.addOrder(order);
			//构建OrderParam
			OrderParam op = new OrderParam();
			op.setOrderId(orderId);//订单id
			op.setParamName(tagName);//时长
			op.setParamValue(durName);//一个月
			op.setTagValue(tagValue);//标识
			//deviceServerDao.addOrderParam(op);//添加
			//Order renewSecOrder = deviceService.getSecurity4Order(singleOrder,1,SourceTag.SUBMITORDER_SEC.getTagVal());//加密存储
			orderList.add(order);//作为返回页面的订单
			
			paramList.add(op);
		}
		deviceServerDao.addOrderList(orderList);
		
		ArrayList<Order> newOrderList = new ArrayList<Order>();//从数据库查询出新增的订单集合，因为页面后续提交需要通过orderType关联出productName字段属性
		for(int i=0;i<orderList.size();i++){
			Order singleOrder = deviceServerDao.getSingleOrder(orderList.get(i));//刚添加成功的订单
			newOrderList.add(singleOrder);
		}
		
		deviceServerDao.addOrderParamList(paramList);
		
		return newOrderList;
		
		//ResponseMessage res = payProductOrderList(orderList);
	}
	
	
	//批量支付--订单需要增加开始时间和到期时间
	public boolean payProductOrderList(List<Order> orderList){
		ResponseMessage res = new ResponseMessage();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//支付，批量
		UserAccount userAccount = userAccountService.getUserAccountInfo(orderList.get(0).getUserId());//获取当前账户信息
		Double balance = userAccount.getAccountBalance();//余额
		
		double countPrice = 0;//总价
		
		List<ConsumeRecord> recordList = new ArrayList<ConsumeRecord>();//消费记录
		
		//给每个订单增加支付时间
		for(int i=0;i<orderList.size();i++){
			Order sorder = orderList.get(i);
			//获取该订单关联的实例
			DeviceInstance ins = deviceInstanceDao.getInstanceByInstanceId(sorder.getInstanceId());
			
			ConsumeRecord record = new ConsumeRecord(); //消费记录
			//新增支付时间
			String payTime = sdf.format(new Date());
			
			sorder.setPayTime(payTime);//支付时间
			sorder.setStatus(OrderStatus.PAIED.getStatusVal());//状态
			
			//需要新增订单的开始时间、结束时间
			List<OrderParam> orderParams = sorder.getOrderParamList();
			int duration = getOrderDuration(orderParams);//订单上的时长
			String endTime = DateUtil.getExpireDate(payTime, duration);//计算出结束时间
			sorder.setStartTime(payTime);//支付时间即为开始时间
			sorder.setEndTime(endTime);
			
			//更新账户信息，生成一条消费记录和更新账户信息
			record.setOrderType(sorder.getOrderType());//续费的消费记录里面的类型为实例的类型
			record.setAccountId(userAccount.getAccountId());
			record.setConsumeAmt(sorder.getPrice());
			record.setOrderNo(sorder.getOrderId());
			record.setUserId(userAccount.getUserId());
			record.setProductName(sorder.getProductName());
			
			int serviceId = ins.getServiceId();//大类型：设备托管
			int recordInsType = 0;//
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
			record.setInstanceType(recordInsType);
			
			recordList.add(record);
			
			double price = orderList.get(i).getPrice();
			countPrice += price;
		}
		
		if(balance >= countPrice){//可以支付
			//批量,更新
			deviceServerDao.updateOrderListStatus(orderList);
			
			for(int i=0;i<recordList.size();i++){
				userAccountService.addNewConsumeRecord(recordList.get(i));
			}
			//userAccountDAO.addNewConsumeRecordList(recordList);
			return true;
		}else{
			return false;
		}
	}
	

	//查询待回收产品实例列表
	public Map<String, List<DeviceInstance>> getPreRecycleInstanceList() {
		Map<String, List<DeviceInstance>> preRecycleInstMap = new HashMap<String, List<DeviceInstance>>();
		List<DeviceInstance> deviceInstList = deviceInstanceDao.getPreRecycleInstanceList();
		/*List<DeviceInstanceVO> deviceInstVOList = new ArrayList<DeviceInstanceVO>();
		if(deviceInstList != null && deviceInstList.size() > 0){
			for (DeviceInstance deviceInst : deviceInstList) {
				DeviceInstanceVO deviceInstVO = new DeviceInstanceVO();
				deviceInstVO.setId(deviceInst.getInstanceId());
				deviceInstVO.setServiceId(deviceInst.getServiceId());
				deviceInstVO.setInstanceNum(deviceInst.getInstanceNum());
				deviceInstVO.setStatus(deviceInst.getStatus());
				deviceInstVO.setDeviceTypeName(deviceInst.getDeviceName());
				deviceInstVO.setInstanceType(deviceInst.getInstanceType());
				User user = userDAO.getUserInfoById(deviceInst.getUserId()); //获取用户信息
				deviceInstVO.setUser(user);
				deviceInstVO.setStartTime(deviceInst.getStartTime());
				deviceInstVO.setEndTime(deviceInst.getEndTime());
				deviceInstVO.setPrice(deviceInst.getPrice());
				deviceInstVO.setProductName(deviceInst.getProductName());
				
				List<InstanceParam> paramList = deviceInst.getInstanceParamList();
				deviceInstVO.setInstanceParamList(paramList);
				
				deviceInstVO.setProductInstance(generateProdInst(paramList));
				
				deviceInstVO.setIpList(deviceInst.getIpList());
				deviceInstVOList.add(deviceInstVO);
			}
		}*/
		for(int i=0;i<deviceInstList.size();i++){
			DeviceInstance instance = deviceInstList.get(i);
			List<InstanceParam> paramList = instance.getInstanceParamList();
			
			Class<?> clazz;
			try {
				clazz = Class.forName("com.baoidc.idcserver.po.ProductInstance");//获取大class
				ProductInstance productInstance = (ProductInstance) clazz.newInstance();//获取此大class的实例
				for(int j=0;j<paramList.size();j++){
						InstanceParam param = paramList.get(j);
						String tagName = param.getTagValue();
						String setName = "set"+tagName.substring(0,1).toUpperCase()+tagName.substring(1);//构建如“setModel”方法名
						Method setMethod = clazz.getMethod(setName, String.class);//获取大class的方法
						setMethod.invoke(productInstance, param.getParamValue());//执行此实例的set方法
					}
				
				instance.setProductInstance(productInstance);
			} catch (Exception e) {
				e.printStackTrace();
			}
			User user = userDAO.getUserInfoById(instance.getUserId());
			instance.setUser(user);
			instance = setSecurity4Instance(instance,1,SourceTag.GETINSTANCE_SEC.getTagVal());
		}
		preRecycleInstMap.put("aaData", deviceInstList);
		return preRecycleInstMap;
	}
	
	
	//资产回收
	public void assetRecycle(int instanceId) {
		DeviceInstance deviceInst = deviceInstanceDao.getInstanceByInstanceId(instanceId);
		
		if(deviceInst != null){//
			//修改实例状态
			deviceInst.setStatus(3); //将实例状态改成已回收状态
			deviceInstanceDao.updateInstance(deviceInst);
			//更新资产状态
			int serviceId = deviceInst.getServiceId();
			switch (serviceId) {
			case 1: //设备租用
				Servers server = assetManageDAO.getServerByServerId(deviceInst.getDeviceId());
				server.setUseStatus(0);
				User user = new User();
				user.setId(0);
				server.setUser(user);
				assetManageDAO.recycleServer(server); //更新资产状态
				break;
			case 2: //设备托管
				int serverId = deviceInst.getDeviceId();
				//Servers trustServer = assetManageDAO.getServerByServerId(serverId);
				assetManageDAO.deleteServer(serverId); //删除服务器
				assetManageDAO.deleteServerIp(serverId); //删除服务器IP
				assetManageDAO.deleteServerPort(serverId); //删除服务器端口
				break;
			case 3: //机柜租用
				int chestId = deviceInst.getChestId();
				Chest chest = assetManageDAO.getChestByChestId(chestId);
				chest.setUseStatus(0);
				User chestUser = new User();
				chestUser.setId(0);
				chest.setUser(chestUser);
				assetManageDAO.updateChest(chest);
				//删除机柜下的服务器
				assetManageDAO.delServerByChestId(chestId); //根据机柜ID删除机柜下的服务器
				assetManageDAO.delPortByChestId(chestId); //根据机柜ID删除与服务器管理的交换机端口
				assetManageDAO.updateIpUnUsedByChestId(chestId); //修改ip状态
				break;
			default:
				break;
			}
		}
	}
	
	//实例续费处理
	public boolean renewInstance(String orderId) {
		boolean flag = false;
		if(orderId !=  null && StringUtils.isNotBlank(orderId)){
			Order order = deviceServerDAO.getSingleOrderByOrderId(orderId);
			if(order != null ){
				if(order.getStatus() == 1){//为已支付但未处理的订单
					
					int instanceId = order.getInstanceId();
					DeviceInstance deviceInst = deviceInstanceDao.getInstanceByInstanceId(instanceId); //获取订单对应的实例ID
					List<OrderParam> orderParams = order.getOrderParamList();
					int orderDuration = getOrderDuration(orderParams);
					int instStatus = deviceInst.getStatus();
					if(instStatus == 2){ //已过有效期，待回收状态
						String startTime = DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss");
						String endTime = DateUtil.getExpireDate(startTime, orderDuration);
						deviceInst.setStartTime(startTime);
						deviceInst.setEndTime(endTime);
						deviceInst.setStatus(0);
						deviceInstanceDao.updateInstance(deviceInst);
						
						//重新计算时长
						InstanceParam instParam = new InstanceParam();
						instParam.setInstanceId(instanceId);
						OrderParam orderParam = new OrderParam();
						orderParam.setOrderId(orderId);
						orderParam.setTagValue("duration");
						orderParam = deviceServerDAO.getOrderParamByInst(orderParam);
						instParam.setParamValue(orderParam.getParamValue());
						instParam.setTagValue("duration");
						List<InstanceParam> instParams = new ArrayList<InstanceParam>();
						instParams.add(instParam);
						deviceInstanceDao.updateInstanceParam(instParams);
					}else{ //未过有效期--更新实例一些属性值
						String endTime = DateUtil.getExpireDate(deviceInst.getEndTime(), orderDuration);
						deviceInst.setEndTime(endTime);
						//更新实例价格
						double orderPrice = order.getPrice();
						double instancePrice = deviceInst.getPrice();
						instancePrice = instancePrice + orderPrice;//总价格
						deviceInst.setPrice(instancePrice);
						deviceInstanceDao.updateInstance(deviceInst);
						
						//重新计算时长
						int instDuration = getInstDuration(deviceInst.getInstanceParamList());
						instDuration = instDuration + orderDuration;
						String instDurationStr = DurationUtil.numToDurationStr(instDuration);
						InstanceParam instParam = new InstanceParam();
						instParam.setInstanceId(instanceId);
						instParam.setParamValue(instDurationStr);
						instParam.setTagValue("duration");
						List<InstanceParam> instParams = new ArrayList<InstanceParam>();
						instParams.add(instParam);
						deviceInstanceDao.updateInstanceParam(instParams);
					}
					
					//更新订单状态
					order.setStatus(2);
					
					//判断是否为自动续费
					int productId = 8;
					if(Integer.parseInt(order.getOrderType()) == productId){//需要更新AutoRenewConf的状态：即已处理，恢复为初始状态，0
						AutoRenewConf conf = new AutoRenewConf();
						conf.setInstanceId(order.getInstanceId());
						conf.setStatus(0);
						deviceInstanceDao.updateAutoRenewConf(conf);
					}
					
					deviceServerDAO.updateOrderStatus(order);
					
					flag = true;
				}
			}
		}
		
		return flag;
	}

	//根据实例参数生成产品实例（公共方法）
	private ProductInstance generateProdInst(List<InstanceParam> instParamList){
		ProductInstance prodInst = null;
		//设置产品实例参数
		Class<?> clazz;
		try {
			clazz = Class.forName("com.baoidc.idcserver.po.ProductInstance");//获取大class
			prodInst = (ProductInstance) clazz.newInstance();//获取此大class的实例
			for(int j=0;j<instParamList.size();j++){
					InstanceParam param = instParamList.get(j);
					String tagName = param.getTagValue();
					String setName = "set"+tagName.substring(0,1).toUpperCase()+tagName.substring(1);//构建如“setModel”方法名
					Method setMethod = clazz.getMethod(setName, String.class);//获取大class的方法
					setMethod.invoke(prodInst, param.getParamValue());//执行此实例的set方法
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return prodInst;
	}
	
	//获取实例个数
	private int getInstanceCount(List<OrderParam> orderParams){
		int count = 1;
		for (OrderParam orderParam : orderParams) {
			String tagValue = orderParam.getTagValue();
			if((SourceTag.COUNT.getTagVal()).equalsIgnoreCase(tagValue)){
				count = Integer.parseInt(orderParam.getParamValue());
			}
		}
		return count;
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

	//处理升级订单，获取设备信息
	public Servers getDeviceByInstanceId4Order(int instanceId) {
		DeviceInstance instance = deviceInstanceDao.getInstanceByInstanceId(instanceId);
		if(instance != null){
			Servers server = assetManageDAO.getServerByServerId(instance.getDeviceId());
			return server;
		}else{
			return null;
		}
		
	}

	@Override
	public Map<String, Map<String, Integer>> getAllInstanceByHostRoom(
			String hostRoom,int userId)throws Exception {
		Map<String, Map<String, Integer>> map = new  HashMap<String,Map<String, Integer>>();
		List<DeviceInstance> list = deviceInstanceDao.getAllInstanceByHostRoom(hostRoom,userId);
		String str1 = "已过期";
		String str2 = "即将过期";
		String str3 = "近期创建";
		String str4 = "运行中";
		//设备租用
		HashMap<String, Integer> deviceRentMap = new HashMap<String,Integer>();
		deviceRentMap.put(str1, 0);
		deviceRentMap.put(str2, 0);
		deviceRentMap.put(str3, 0);
		deviceRentMap.put(str4, 0);
		//设备托管
		HashMap<String, Integer> deviceCollocationMap = new HashMap<String,Integer>();
		deviceCollocationMap.put(str1, 0);
		deviceCollocationMap.put(str2, 0);
		deviceCollocationMap.put(str3, 0);
		deviceCollocationMap.put(str4, 0);
		//机柜租用
		HashMap<String, Integer> ChestRentMap = new HashMap<String,Integer>();
		ChestRentMap.put(str1, 0);
		ChestRentMap.put(str2, 0);
		ChestRentMap.put(str3, 0);
		ChestRentMap.put(str4, 0);
		
		for (DeviceInstance deviceInstance : list) {
			String startTime = deviceInstance.getStartTime();
			String endTime = deviceInstance.getEndTime();
			String status = checkStatus(startTime,endTime);
			switch (deviceInstance.getServiceId()) {
			//设备租用
			case 1:
				deviceRentMap.put(status, deviceRentMap.get(status)+1);
				break;
			//设备托管
			case 2:
				deviceCollocationMap.put(status, deviceCollocationMap.get(status)+1);
				break;
			//机柜租用
			case 3:
				ChestRentMap.put(status, ChestRentMap.get(status)+1);
				break;
			default:
				break;
			}
		}
		map.put("服务器租用", deviceRentMap);
		map.put("服务器托管", deviceCollocationMap);
		map.put("机柜租用", ChestRentMap);
		return map;
	}
	
	/*
	 * 给个开始时间  结束时间   判断与当前时间的距离   
	 * 这里已7天为判断标尺
	 */
	public String checkStatus(String startTime,String endTime) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		GregorianCalendar gc = new GregorianCalendar();
		
		Date endDate = sdf.parse(endTime);
		gc.setTime(endDate);
		gc.add(Calendar.DAY_OF_MONTH, -7);
		Date befor7Day = gc.getTime();
		
		Date startDate = sdf.parse(startTime);
		gc.setTime(startDate);
		gc.add(Calendar.DAY_OF_MONTH, 7);
		Date after7Day = gc.getTime();

		Date now = new Date();
		if(now.getTime()>endDate.getTime()){
			return "已过期";
		}else if(now.getTime()>=befor7Day.getTime()){
			return "即将过期";
		}else if(now.getTime()<after7Day.getTime()){
			return "近期创建";
		}else{
			return "运行中";
		}
	}

	@Override
	public Servers getServerByServerId4UpOrder(int serverId) {
		return assetManageDAO.getServerByServerId(serverId);
	}

	
	//从数据库获取实例，保证数据同步统一性，insList：页面现有的实例集合
	@Override
	public List<DeviceInstance> getSourceInstance2db(List<DeviceInstance> instanceList) {
		ArrayList<DeviceInstance> sInsList = new ArrayList<DeviceInstance>();
		for(int i=0;i<instanceList.size();i++){
			DeviceInstance di = instanceList.get(i);
			if(di != null){
				//取出对应的完整实例对象
				DeviceInstance secInstance = getSecurity4Instance(di,1);
				
				if(secInstance != null){
					DeviceInstance ins = deviceInstanceDao.getSingleInstance(secInstance);
					
					if(ins.getInstanceId() != 0){
						//完善对象
						List<InstanceParam> paramList = ins.getInstanceParamList();
						
						ProductInstance productIns = generateProdInst(paramList);//转化为对象
						ins.setProductInstance(productIns);
						//将获取到的完整Instance存入redis
						ins = setSecurity4Instance(ins,1,SourceTag.GETINSTANCE_SEC.getTagVal());
						sInsList.add(ins);//返回数据
					}
				}
				
			}
			
			//DeviceInstance ins = deviceInstanceDao.getSingleInstance(instanceList.get(i));
		}
		return sInsList;
	}

	public List<DeviceInstance> getInstanceInUse() {
		return deviceInstanceDao.getInstanceInUse();
	}

	public void updateInstanceSingle(DeviceInstance deviceInstance) {
		deviceInstanceDao.updateInstance(deviceInstance);
	}

	@Override
	public DeviceInstance getSingleInstance(DeviceInstance deviceInstance) {
		return deviceInstanceDao.getSingleInstance(deviceInstance);
	}

	@Override//提交自动续费的配置
	public void submitAutoRenew(List<DeviceInstance> instanceList) {
		for(int i=0;i<instanceList.size();i++){
			DeviceInstance ins = instanceList.get(i);
			AutoRenewConf conf = new AutoRenewConf();
			conf.setInstanceId(ins.getInstanceId());
			conf.setDuration("1个月");
			deviceInstanceDao.addAutoRenewConf(conf);
		}
		
	}

	public void updateInstanceByAnyting(DeviceInstance deviceInstance) {
		deviceInstanceDao.updateInstanceByAnyting(deviceInstance);
	}

	@Override
	public void updateAutoRenewConf(AutoRenewConf conf) {
		deviceInstanceDao.updateAutoRenewConf(conf);
		
	}

	public AutoRenewConf getAutoRenewConf(int instanceId) {
		return deviceInstanceDao.getAutoRenewConfByInstId(instanceId);
	}

	@Override
	public void deleteAutoRenewConf(int instanceId) {
		deviceInstanceDao.deleteAutoRenewConf(instanceId);
		
	}
	
}













