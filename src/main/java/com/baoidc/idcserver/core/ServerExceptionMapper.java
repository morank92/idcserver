package com.baoidc.idcserver.core;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ServerExceptionMapper implements ExceptionMapper<Exception> {

	public Response toResponse(Exception exception) {
		Response.ResponseBuilder ResponseBuilder = null;
		
		if(exception instanceof ServerException){
			ServerException exp = (ServerException) exception;
			ErrorEntity entity = new ErrorEntity(exp.getCode(),exp.getMessage());
			ResponseBuilder = Response.ok(entity, MediaType.APPLICATION_JSON);
		}else{
			ErrorEntity entity = new ErrorEntity(ErrorCode.OTHER_ERR.getCode(),exception.getMessage());
			ResponseBuilder = Response.ok(entity, MediaType.APPLICATION_JSON);
		}
		
		return ResponseBuilder.build();
	}

}
