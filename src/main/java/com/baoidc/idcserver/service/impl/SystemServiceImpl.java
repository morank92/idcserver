package com.baoidc.idcserver.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.relation.RoleNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

import com.baoidc.idcserver.core.AES256Cipher;
import com.baoidc.idcserver.core.DateUtil;
import com.baoidc.idcserver.core.ErrorCode;
import com.baoidc.idcserver.core.IdcServerUtils;
import com.baoidc.idcserver.core.RedisUtil;
import com.baoidc.idcserver.core.ResponseMessage;
import com.baoidc.idcserver.dao.ISystemDAO;
import com.baoidc.idcserver.po.ArticleContent;
import com.baoidc.idcserver.po.ArticleType;
import com.baoidc.idcserver.po.Operation;
import com.baoidc.idcserver.po.RoleMenu;
import com.baoidc.idcserver.po.SysApi;
import com.baoidc.idcserver.po.SysManageUser;
import com.baoidc.idcserver.po.SysMenu;
import com.baoidc.idcserver.po.SysSource;
import com.baoidc.idcserver.po.User;
import com.baoidc.idcserver.po.UserRole;
import com.baoidc.idcserver.service.ISystemService;

@Service
public class SystemServiceImpl implements ISystemService {
	
	@Autowired
	private ISystemDAO systemDAO;
	
	@Autowired
	private RedisUtil redisUtil;

	//查询指定角色要显示的菜单
	public List<SysMenu> getMenuResource(int parentId,int roleId) {
		Map<String, Integer> queryMap = new HashMap<String, Integer>();
		queryMap.put("parentId", parentId);
		queryMap.put("roleId", roleId);
		List<SysMenu> sysMenuList = systemDAO.getRoleMenuResources(queryMap);
		return getNewMenuList(sysMenuList);
	}
	
	//查询系统菜单
	public List<SysMenu> getMenuResources() {
		List<SysMenu> sysMenuList = getNewMenuList(systemDAO.getMenuResources());
		return sysMenuList;
	}
	
	//获取系统目录树
	public List<SysMenu> getMenuTree() {
		List<SysMenu> sysMenuList = getNewMenuList(systemDAO.getMenuTree());
		return sysMenuList;
	}

	//查询系统用户角色
	public List<UserRole> getUserRoles() {
		return systemDAO.getUserRoles();
	}
	
	//填加系统菜单
	public void addNewSysMenu(SysMenu sysMenu) {
		int maxSeq = systemDAO.getMaxSeq();
		sysMenu.setSeq(maxSeq + 1);
		sysMenu.setStatus(0);
		sysMenu.setType(0);
		systemDAO.addNewMenu(sysMenu);
		//添加菜单和角色的关系
		List<RoleMenu> roleMenus = new ArrayList<RoleMenu>();
		RoleMenu rm = new RoleMenu();
		rm.setMenuId(sysMenu.getId());
		rm.setRoleId(sysMenu.getRoleId());
		roleMenus.add(rm);
		systemDAO.addUserRoleMenus(roleMenus);
	}
	
	//创建系统管理员用户
	public void createSysManageUser(SysManageUser sysManageUser) {
		if(sysManageUser != null){
			sysManageUser.setPassword(encryptPassword(sysManageUser.getPassword()));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String createTime = sdf.format(new Date());
			sysManageUser.setCreateTime(createTime);
			systemDAO.createSysManageUser(sysManageUser);
		}
	}
	
	//删除管理员用户
	public void delManageUserById(int manageUserId) {
		systemDAO.delManageUserById(manageUserId);
	}
	
	//修改管理员用户
	public void modifyManageUser(SysManageUser sysManageUser) {
		if(sysManageUser != null){
			sysManageUser.setPassword(encryptPassword(sysManageUser.getPassword()));
			systemDAO.modifyManageUserById(sysManageUser);
		}
	}

	//删除菜单
	public void deleteMenuByMenuId(int menuId) {
		SysMenu sysMenu = systemDAO.getFullMenuById(menuId);
		if(sysMenu !=  null && sysMenu.getNodes() != null && sysMenu.getNodes().size() > 0){ //如果要删除的菜单含有子菜单，一并删除
			systemDAO.deleteSubMenuByParentId(sysMenu.getNodes());
		}
		systemDAO.deleteMenuByMenuId(menuId);
	}
	
	//修改菜单
	public void updateSysMenu(SysMenu sysMenu) {
		systemDAO.updateSystemMenu(sysMenu);
	}
	
	//获取系统管理员列表
	public List<SysManageUser> getSysManageUserList() {
		return systemDAO.getSysManageUserList();
	}

	//获取系统API列表
	public Map<String, List<SysApi>> getSysApiList() {
		Map<String, List<SysApi>> sysApiDataMap = new HashMap<String, List<SysApi>>();
		List<SysApi> sysApiList = systemDAO.getSysApiList();
		if(sysApiList != null && sysApiList.size() >  0){
			sysApiDataMap.put("aaData", sysApiList);
		}
		return sysApiDataMap;
	}
	
	//添加Api
	public void addNewSysApi(SysApi sysApi) {
		systemDAO.addNewSystemApi(sysApi);
	}
	
	//删除Api
	public void delSysApiById(int id) {
		systemDAO.delSysApiById(id);
	}
	
	//修改API
	public void updateSysApi(SysApi sysApi) {
		systemDAO.updateSysApi(sysApi);
	}
	
	public void createUserRole(UserRole userRole) {
		systemDAO.createNewUserRole(userRole);
		int userRoleId = userRole.getId();
		if(userRoleId !=  0){
			int[] menuIds = userRole.getMenuIds();
			if(menuIds != null && menuIds.length > 0){
				List<RoleMenu> roleMenus = new ArrayList<RoleMenu>();
				for(int i = 0;i < menuIds.length;i++){
					RoleMenu roleMenu = new RoleMenu();
					roleMenu.setRoleId(userRoleId);
					roleMenu.setMenuId(menuIds[i]);
					roleMenus.add(roleMenu);
				}
				systemDAO.addUserRoleMenus(roleMenus);
			}
		}
	}

	public void updateUserRole(UserRole userRole) {
		systemDAO.updateUserRole(userRole);
		int userRoleId = userRole.getId();
		systemDAO.deleteRoleMenuByRoleId(userRoleId);
		int[] menuIds = userRole.getMenuIds();
		if(menuIds != null && menuIds.length > 0){
			List<RoleMenu> roleMenus = new ArrayList<RoleMenu>();
			for(int i = 0;i < menuIds.length;i++){
				RoleMenu roleMenu = new RoleMenu();
				roleMenu.setRoleId(userRoleId);
				roleMenu.setMenuId(menuIds[i]);
				roleMenus.add(roleMenu);
			}
			systemDAO.addUserRoleMenus(roleMenus);
		}
	}

	public void deleteUserRole(int id) {
		systemDAO.deleteUserRole(id);
		systemDAO.deleteRoleMenuByRoleId(id);
	}
	
	public List<SysSource> getSysSourceList() {
		List<SysSource> sysSourceList = new ArrayList<SysSource>();
		List<SysSource> sourceList = systemDAO.getSysSources();
		if(sourceList != null && sourceList.size() > 0){
			for (SysSource sysSource : sourceList) {
				String sourceOpts = sysSource.getSourceOpts();
				String[] optArray = sourceOpts.split(",");
				List<Operation> opts = new ArrayList<Operation>();
				for(int i = 0 ;i < optArray.length; i++){
					int optId = Integer.parseInt(optArray[i]);
					Operation opt = systemDAO.getOperationById(optId);
					opts.add(opt);
				}
				sysSource.setOptList(opts);
				sysSourceList.add(sysSource);
			}
		}
		return sysSourceList;
	}

	//递归修改List里面list的值
	private List<SysMenu> getNewMenuList(List<SysMenu> orginSysMenuList){
		List<SysMenu> newSysMenu = new ArrayList<SysMenu>();
		for(int i = 0;i<orginSysMenuList.size();i++){
			SysMenu sysMenu = orginSysMenuList.get(i);
			if(sysMenu.getParentMenu() == null){ //设置根菜单
				SysMenu rootParentMenu = new SysMenu();
				rootParentMenu.setId(0);
				rootParentMenu.setText("根菜单");
				sysMenu.setParentMenu(rootParentMenu);
			}
			List<SysMenu> subMenuList = sysMenu.getNodes();
			if(subMenuList == null || subMenuList.size() == 0){//子菜单元素为空
				sysMenu.setNodes(null);
			}else{//在菜单元素不为空
				sysMenu.setNodes(getNewMenuList(sysMenu.getNodes()));
			}
			newSysMenu.add(sysMenu);
		}
		return newSysMenu;
	}
	
	
	//管理员登录
	public ResponseMessage manageLogin(String userName, String password,HttpServletRequest request) {
		ResponseMessage resMessage = null;
		if(userName != null){
			SysManageUser manageUser = systemDAO.getSysManageUserByUserName(userName);
			if(manageUser == null){
				//throw new ServerException(ErrorCode.NOT_USER); //用户不存在
				resMessage = new ResponseMessage(ErrorCode.NOT_USER);
			}else{
				String userPassword = manageUser.getPassword();
				if(password != null && StringUtils.isNotBlank(password)){
					if(userPassword.equals(encryptPassword(password))){
						//throw new ServerException(ErrorCode.USER_LOGIN_SUCCESS); //验证通过
						resMessage = new ResponseMessage(ErrorCode.USER_LOGIN_SUCCESS);
						request.setAttribute("USER-ID", manageUser.getId());
						StringBuilder sBuilder = new StringBuilder();
						sBuilder.append(manageUser.getId()).append("|");
						String tokenCode = IdcServerUtils.createTokenCode(manageUser.getUserName(),manageUser.getId(),1);
						sBuilder.append(tokenCode).append("|");
						sBuilder.append(manageUser.getRoleId());
						redisUtil.set(manageUser.getUserName(), tokenCode, 1800);
						request.setAttribute("USER-ID",manageUser.getId());
						
						//判断是否为客户经理,如果是则新增该用户一对redis属性值，作为访问资源的权限（在filter中从redis取出判断）
						if(manageUser.getRoleId() != 0 && manageUser.getRoleId() == 3){//目前客户经理的角色权限通过唯一ID失败-17/06/26
							redisUtil.set(tokenCode,manageUser.getRoleId()+"",1800);//作为后台访问权限,时效与token一致
						}
						sBuilder.append("|"+manageUser.getRoleId());//角色id，给页面cookie存储，作为页面访问资源的权限
						
						resMessage.setData(sBuilder.toString());
						
					}else{
						//throw new ServerException(ErrorCode.USER_PASSWORD_WRONG); //密码错误
						resMessage = new ResponseMessage(ErrorCode.USER_PASSWORD_WRONG);
					}
				}
			}
		}
		return resMessage;
	}
	
	//管理员退出
	public void manageLogout(String userName) {
		Jedis jedis = redisUtil.getJedis();
		jedis.del(userName);
		jedis.close();
	}
	
	//创建系统文章
	public void createArticleContent(ArticleContent articleContent) {
		if(articleContent != null){
			articleContent.setPublishTime(DateUtil.dateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
			systemDAO.createArticleContent(articleContent);
		}
	}

	//创建系统文章类别
	public void createArticleType(ArticleType articleType) {
		if(articleType != null){
			systemDAO.createArticleType(articleType);
		}
	}

	//查询系统文章列表
	public List<ArticleContent> getArticleContentList() {
		return systemDAO.getArticleContentList();
	}

	//查询系统文章类别列表
	public List<ArticleType> getArticleTypeList() {
		return systemDAO.getArticleTypeList();
	}

	//通过ID查询系统文章
	public ArticleContent getArticleContentById(int id) {
		return systemDAO.getArticleContentById(id);
	}

	//通过ID查询系统文章类别
	public ArticleType getArticleTypeById(int id) {
		return systemDAO.getArticleTypeById(id);
	}
	
	//删除文章类型
	public void deleteArticleType(int id) {
		//删除文章类型
		if(id != 0){
			systemDAO.deleteArticleType(id);
		}
		//删除所属文章类别的文章
		systemDAO.deleteArticleContentByTypeId(id);
	}
	
	//删除文章
	public void deleteArticleContent(int id) {
		systemDAO.deleteArticleContentById(id);
	}
	
	//修改文章
	public void updateArticleContent(ArticleContent articleContent) {
		systemDAO.updateArticleContent(articleContent);
	}
	
	//查询文章列表供用户系统前台页面显示
	public List<ArticleContent> getArticleContentListForUser() {
		return systemDAO.getArticleContentListForUser();
	}
	
	//查询文章详情
	public ArticleContent getArticleContentDetail(int id) {
		return systemDAO.getArticleContentById(id);
	}

	/**
	 * 密码加密
	 * @param password
	 * @return
	 */
	private String encryptPassword(String password){
		String encryptedPass = "";
		if(password != null && StringUtils.isNotBlank(password)){
			try{
				AES256Cipher  aes = new AES256Cipher();
				encryptedPass = aes.encrypt(password, "lanysec", "");
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return encryptedPass;
	}

	//通过管理员id获取其对象
	public SysManageUser getSysManageUserById(int manageId) {
		return systemDAO.getSysManageUserById(manageId);
	}

}
