package com.baoidc.idcserver.service;

import java.util.HashMap;
import java.util.List;

import com.baoidc.idcserver.po.ReportAttack;
import com.baoidc.idcserver.po.ReportConnection;
import com.baoidc.idcserver.po.ReportFlow;
import com.baoidc.idcserver.po.Servers;
import com.baoidc.idcserver.vo.query.EchartsQueryVO;

public interface IReportService {

	//通过userId获取对应的ip
	List<Servers> getInstanceIpByuserId(int userId);

	List<ReportFlow> getFlowReport(EchartsQueryVO echartsQueryVo);

	List<ReportConnection> getConnectsReport(EchartsQueryVO echartsQueryVo);

	//List<ReportFlow> getAttReport(EchartsQueryVO echartsQueryVo);

	List<ReportAttack> getTopnReport(EchartsQueryVO echartsQueryVo);

	List<ReportAttack> getAttDisReport(EchartsQueryVO echartsQueryVo);

	//List<ReportAttack> getAttDetails(EchartsQueryVO echartsQueryVo);

	/*HashMap<String, Object> getAllReport(String tableType,String fenceip,
			String startGenerate, String endGenerate);

	HashMap<String, Object> getAllReport2(String tableType1, String domainName,
			String sd1, String ed1, String tableType2, String fenceip2,
			String sd2, String ed2);*/
	
}
