package com.baoidc.idcserver.dao;

import java.util.List;

import com.baoidc.idcserver.po.DeviceSourceParam;
import com.baoidc.idcserver.po.Order;
import com.baoidc.idcserver.po.OrderParam;
import com.baoidc.idcserver.po.ReportAttack;
import com.baoidc.idcserver.po.ReportConnection;
import com.baoidc.idcserver.po.ReportFlow;
import com.baoidc.idcserver.po.ServerRentOrder;
import com.baoidc.idcserver.po.ServerRoom;
import com.baoidc.idcserver.vo.query.EchartsQueryVO;
import com.baoidc.idcserver.vo.query.OrderQueryVO;


public interface IReportDAO {

	//
	List<ReportFlow> getFlowReport(EchartsQueryVO echartsQueryVo);

	List<ReportConnection> getConnectsReport(EchartsQueryVO echartsQueryVo);

	List<ReportAttack> getAttDisReport(EchartsQueryVO echartsQueryVo);

	List<ReportAttack> getAttDetails(EchartsQueryVO echartsQueryVo);

	List<ReportAttack> getTopnReport(EchartsQueryVO echartsQueryVo);
	

}
