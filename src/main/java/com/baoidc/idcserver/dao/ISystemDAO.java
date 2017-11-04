package com.baoidc.idcserver.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baoidc.idcserver.po.ArticleContent;
import com.baoidc.idcserver.po.ArticleType;
import com.baoidc.idcserver.po.CustomerManager;
import com.baoidc.idcserver.po.Operation;
import com.baoidc.idcserver.po.RoleMenu;
import com.baoidc.idcserver.po.SysApi;
import com.baoidc.idcserver.po.SysManageUser;
import com.baoidc.idcserver.po.SysMenu;
import com.baoidc.idcserver.po.SysSource;
import com.baoidc.idcserver.po.UserRole;

public interface ISystemDAO {
	
	public List<SysMenu> getRoleMenuResources(Map<String, Integer> queryMap);
	
	public List<SysMenu> getMenuResources();
	
	public List<UserRole> getUserRoles();
	
	public void addNewMenu(SysMenu sysMenu);
	
	public int getMaxSeq();
	
	public void deleteMenuByMenuId(int menuId);
	
	public SysMenu getFullMenuById(int id);
	
	public void deleteSubMenuByParentId(List<SysMenu> submenu);
	
	public void updateSystemMenu(SysMenu sysMenu);
	
	public List<SysApi> getSysApiList();
	
	public void addNewSystemApi(SysApi sysApi);
	
	public void delSysApiById(int id);
	
	public void updateSysApi(SysApi sysApi);
	
	public List<UserRole> getUserRoleList();
	
	public void createNewUserRole(UserRole userRole);
	
	public void updateUserRole(UserRole userRole);
	
	public void deleteUserRole(int id);
	
	public void createSysSource(SysSource sysSource);
	
	public List<SysSource> getSysSources();
	
	public Operation getOperationById(int id);
	
	public List<SysMenu> getMenuTree();
	
	public void addUserRoleMenus(@Param("roleMenus") List<RoleMenu> roleMenus);
	
	public void deleteRoleMenuByRoleId(int roleId);
	
	public List<SysManageUser> getSysManageUserList();
	
	public void createSysManageUser(SysManageUser sysManageUser);
	
	public void delManageUserById(int id);
	
	public void modifyManageUserById(SysManageUser sysManageUser);
	
	public SysManageUser getSysManageUserByUserName(String userName);
	
	public void createArticleType(ArticleType articleType);
	
	public void createArticleContent(ArticleContent articleContent);
	
	public List<ArticleContent> getArticleContentList();
	
	public List<ArticleType> getArticleTypeList();
	
	public ArticleContent getArticleContentById(int id);
	
	public ArticleType getArticleTypeById(int id);
	
	public void deleteArticleType(int id);
	
	public void deleteArticleContentById(int id);
	
	public void deleteArticleContentByTypeId(int articleTypeId);
	
	public void updateArticleContent(ArticleContent articleContent);
	
	public List<ArticleContent> getArticleContentListForUser();

	//通过客户的id查询其关联的客户经理
	public SysManageUser getCustomerManageByUserId(Integer userId);

	//通过sql语句中的role_id = 3查询出所有的客户经理
	public List<SysManageUser> getAllCustomerManager();

	//通过管理员id获取其对象
	public SysManageUser getSysManageUserById(int manageId);

}
