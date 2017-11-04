package com.baoidc.idcserver.rest.service;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

public class IdcServerApplication extends ResourceConfig {
	
	public IdcServerApplication(){
		register(RequestContextFilter.class);
		register(UserRestService.class);
		register(JacksonFeature.class);
		register(ProductRestService.class);
		register(WorkOrderRestService.class);
		register(UserAccountRestService.class);
		//packages("com.baoidc.idcserver.core");
		register(UserMaterialRestService.class);
		register(DeviceInstanceRestService.class);
		register(FinanceManageRestService.class);
		register(AssetManageRestService.class);
		register(SnmpRestService.class);
		register(SystemRestService.class);
		register(DataReportRestService.class);
		register(ProductConfRestService.class);
		register(UserOptLogRestService.class);
		register(TouclickVerifyRestService.class);
		register(ReportConfRestService.class);
	}

}
