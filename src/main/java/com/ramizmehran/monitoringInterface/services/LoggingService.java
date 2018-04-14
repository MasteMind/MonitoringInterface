package com.ramizmehran.monitoringInterface.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ramizmehran.monitoringInterface.beans.constants.ParameterConstants;
import com.ramizmehran.monitoringInterface.beans.requests.ErrorLogRequest;
import com.ramizmehran.monitoringInterface.beans.requests.MeterRequest;
import com.ramizmehran.monitoringInterface.customAnnotations.Metered;
import com.ramizmehran.monitoringInterface.utils.DynamicValueHelper;

@Service
public class LoggingService {
	private static final Logger LOG = LoggerFactory.getLogger(LoggingService.class);

	@Autowired
	RestTemplate restTemplate;
	@Value("${spring.application.name:DefaultValue}")
	private String applicationName;

	public void initializeRequest(Metered meteredAnnotation, String className, String remoteAddr, Object requestBody, HttpServletRequest httpRequest) {
		String randomNumber = MDC.get(ParameterConstants.REQUEST_RANDOM_NUMBER);
		String qualifier = meteredAnnotation.value();
		MeterRequest req = new MeterRequest(
				randomNumber==null?"0":randomNumber,
						className,
						qualifier,
						remoteAddr,
						applicationName);

		req.setExtraParams(DynamicValueHelper.getDataMemberValues(requestBody,meteredAnnotation.logRequestBodyParamNames()));
		req.setExtraParams(DynamicValueHelper.getHeaderValues(httpRequest,meteredAnnotation.logHeaderParamNames()));
		httpRequest.setAttribute("MeterRequest", req);
		httpRequest.setAttribute("MeterRequestStartTime", System.currentTimeMillis());
	}

	@Async
	public void sendMeterLog(String className, Metered meteredAnnotation, long processingTime, Object response,HttpServletRequest httpRequest,HttpServletResponse httpResponse, Object requestBody){
		MeterRequest req = (MeterRequest) httpRequest.getAttribute("MeterRequest"); 
		req.setProcessingTime(Long.toString(processingTime));
		req.setResponse(response!=null?response.toString():"");
		req.setStatusCode(Integer.toString(httpResponse.getStatus()));


		LOG.info("Posting metered log :{}",req);
		restTemplate.postForEntity(ParameterConstants.LOGGING_SERVER_URI+ParameterConstants.METERED_LOGGING_URI, req, ResponseEntity.class);
		
		// if response is not 200 OK(), enqueue it or set to retry
	}
	
	@Async
	public void sendExceptionLog(HttpServletRequest httpRequest,Exception exception){
		MeterRequest req = (MeterRequest) httpRequest.getAttribute("MeterRequest");
		long reqStartTime = (Long)httpRequest.getAttribute("MeterRequestStartTime");
		ErrorLogRequest errorReq = new ErrorLogRequest(req, exception,reqStartTime);

		LOG.info("Posting error log :{}",errorReq);
		restTemplate.postForEntity(ParameterConstants.LOGGING_SERVER_URI+ParameterConstants.ERROR_LOGGING_URI, errorReq, ResponseEntity.class);

		// if response is not 200 OK(), enqueue it or set to retry
	}

}
