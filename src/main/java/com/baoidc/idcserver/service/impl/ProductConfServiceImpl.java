package com.baoidc.idcserver.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baoid.idcserver.vo.IntStringClass;
import com.baoidc.idcserver.dao.ProductConfDAO;
import com.baoidc.idcserver.po.DeviceSourceParam;
import com.baoidc.idcserver.service.ProductConfService;
@Service
public class ProductConfServiceImpl implements ProductConfService {
	
	@Autowired
	private ProductConfDAO productConfDao;
	
	//获取所有以配置的参数项
	public List<DeviceSourceParam> getAllConfParam(){
		List<DeviceSourceParam> list = productConfDao.getAllConfParam();
		return list;
	}

	//删除配置项
	public void deleteParam(int id){
		productConfDao.deleteParam(id);
		productConfDao.deleteProductParam(id);
	}
	
	//添加配置项同时修改更改的配置项
	public void configParam(List<DeviceSourceParam> paramList){
		List<DeviceSourceParam> addList = new ArrayList<DeviceSourceParam>(); 
		List<DeviceSourceParam> updateList = new ArrayList<DeviceSourceParam>(); 
		for (DeviceSourceParam deviceSourceParam : paramList) {
			if(deviceSourceParam.getId()!=0){
				updateList.add(deviceSourceParam);
			}else{
				addList.add(deviceSourceParam);
			}
		}
		if(addList.size()!=0){
			productConfDao.addParam(addList);
		}
		if(updateList.size()!=0){
			productConfDao.updateParam(updateList);
		}
	}
	//获取某一种产品的配置项
	public List<Integer> getProductParam(int proId){
		return productConfDao.getProductParam(proId);
	}
	//获取最外层的提供项
	public List<IntStringClass> getServerOffer(){
		return productConfDao.getServerOffer();
	}
	//根据最外层的提供想 查询当前所提供的产品
	public List<Integer> getProductOffer(int serverId){
		return productConfDao.getProductOffer(serverId);
	}
	
	//配置产品
	public void toConfProductParam(List<Integer> checkedArr){
		Integer proId = checkedArr.remove(0);
		productConfDao.removeProductParam(proId);
		productConfDao.toConfProductParam(checkedArr,proId);
	}
	//判断机房下是否有资产
	public boolean checkHostRoomUse(int id){
		int count1 = productConfDao.checkHostRoomServersUse(id);
		int count2 = productConfDao.checkHostRoomChestUse(id);
		if(count1>0 || count2>0){
			return true;
		}else{
			return false;
		}
	}
}
