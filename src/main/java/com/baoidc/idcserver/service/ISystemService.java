package com.baoidc.idcserver.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.baoidc.idcserver.core.ResponseMessage;
import com.baoidc.idcserver.po.ArticleContent;
import com.baoidc.idcserver.po.ArticleType;
import com.baoidc.idcserver.po.SysApi;
import com.baoidc.idcserver.po.SysManageUser;
import com.baoidc.idcserver.po.SysMenu;
import com.baoidc.idcserver.po.SysSource;
import com.baoidc.idcserver.po.UserRole;

public interface ISystemService {
	
	public List<SysMenu> getMenuResource(int parentId,int roleId);
	
	public List<SysMenu> getMenuResources();
	
	public List<UserRole> getUserRoles();
	
	public void addNewSysMenu(SysMenu sysMenu);
	
	public void deleteMenuByMenuId(int menuId);
	
	public void updateSysMenu(SysMenu sysMenu);
	
	public Map<String, List<SysApi>> getSysApiList();
	
	public void addNewSysApi(SysApi sysApi);
	
	public void delSysApiById(int id);
	
	public void updateSysApi(SysApi sysApi);
	
	public void createUserRole(UserRole userRole);
	
	public void updateUserRole(UserRole userRole);
	
	public void deleteUserRole(int id);
	
	public List<SysSource> getSysSourceList();
	
	public List<SysMenu> getMenuTree();
	
	public List<SysManageUser> getSysManageUserList();
	
	public void createSysManageUser(SysManageUser sysManageUser);
	
	public void delManageUserById(int manageUserId);
	
	public void modifyManageUser(SysManageUser sysManageUser);
	
	public ResponseMessage manageLogin(String userName,String password, HttpServletRequest request);
	
	public void manageLogout(String userName);
	
	public void createArticleContent(ArticleContent articleContent);
	
	public void createArticleType(ArticleType articleType);
	
	public List<ArticleContent> getArticleContentList();
	
	public List<ArticleType> getArticleTypeList();
	
	public ArticleContent getArticleContentById(int id);
	
	public ArticleType getArticleTypeById(int id);
	
	public void deleteArticleType(int id);
	
	public void deleteArticleContent(int id);
	
	public void updateArticleContent(ArticleContent articleContent);
	
	public List<ArticleContent> getArticleContentListForUser();
	
	public ArticleContent getArticleContentDetail(int id);

	//通过管理员id获取其对象
	public SysManageUser getSysManageUserById(int manageId);

}
