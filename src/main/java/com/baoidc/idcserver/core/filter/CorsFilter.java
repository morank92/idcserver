package com.baoidc.idcserver.core.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

public class CorsFilter implements Filter {
	
	private String allowOrigin;
	private String allowMethods;
	private String allowCredential;
	private String allowHeaders;
	private String exposeHeaders;

	public void destroy() {
	}

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)res;
		if(StringUtils.isNotEmpty(allowOrigin)){
			List<String> allowOriginList = Arrays.asList(allowOrigin.split(","));
			if(allowOriginList != null && allowOriginList.size() > 0){
				String currentOrigin = request.getHeader("Origin");
				if(allowOriginList.contains(currentOrigin)){
					response.setHeader("Access-Control-Allow-Origin", currentOrigin);
				}
			}
 		}
		
		if(StringUtils.isNotEmpty(allowMethods)){
			response.setHeader("Access-Control-Allow-Methods", allowMethods);
		}
		
		if(StringUtils.isNotEmpty(allowCredential)){
			response.setHeader("Access-Control-Allow-Credentials", allowCredential);
		}
		
		if(StringUtils.isNotEmpty(allowHeaders)){
			response.setHeader("Access-Control-Allow-Headers", allowHeaders);
		}
		
		if(StringUtils.isNotEmpty(exposeHeaders)){
			response.setHeader("Access-Control-Expose-Headers", exposeHeaders);
		}
		chain.doFilter(req, res);
	}

	public void init(FilterConfig config) throws ServletException {
		allowOrigin = config.getInitParameter("allowOrigin");
		allowMethods = config.getInitParameter("allowMethods");
		allowCredential = config.getInitParameter("allowCredentials");
		allowHeaders = config.getInitParameter("allowHeaders");
		exposeHeaders = config.getInitParameter("exposeHeaders");
	}

}
