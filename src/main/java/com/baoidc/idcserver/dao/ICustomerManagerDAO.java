package com.baoidc.idcserver.dao;

import com.baoidc.idcserver.po.CustomerManager;

public interface ICustomerManagerDAO {
	
	public CustomerManager getCustomerManagerById(int customerManagerId);
	
	public void newCustomerManager(CustomerManager customerManager);

}
