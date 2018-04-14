package com.ramizmehran.monitoringInterface.aspects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ramizmehran.monitoringInterface.customAnnotations.Metered;
import com.ramizmehran.monitoringInterface.services.LoggingService;

@Aspect
@Component
public class MeteredLoggingAspect {
	private static final Logger LOG = LoggerFactory.getLogger(MeteredLoggingAspect.class);
	@Autowired
	LoggingService loggingService;

	@Around("execution(@com.ramizmehran.monitoringInterface.customAnnotations.Metered * *(..)) && @annotation(meteredAnnotation) && args(@org.springframework.web.bind.annotation.RequestBody requestBody,..)")
    public Object logDuration(ProceedingJoinPoint joinPoint, Metered meteredAnnotation, Object requestBody) throws Throwable {
		LOG.info("Received_Request:");
		HttpServletRequest httpRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		HttpServletResponse httpResponse = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
		loggingService.initializeRequest(meteredAnnotation,joinPoint.getSignature().getDeclaringTypeName(),httpRequest.getRemoteAddr(),requestBody,httpRequest);

        long startTime = System.currentTimeMillis();	//capture the start time 
        Object result = joinPoint.proceed();	//execute the method and get the result
        long endTime = System.currentTimeMillis();	//capture the end time
        
        long duration = (endTime - startTime);	//calculate the duration and print results
        loggingService.sendMeterLog(joinPoint.getSignature().getDeclaringTypeName(), meteredAnnotation, duration,result,httpRequest,httpResponse,requestBody);

        LOG.info("Received_Processed:");
        return result;	        
        //return the result to the caller 
	}

	@AfterThrowing(pointcut="execution(@com.ramizmehran.monitoringInterface.customAnnotations.Metered * *(..))", throwing = "ex")
    public void logAfterThrowingAllMethods(Exception ex) throws Throwable {
		HttpServletRequest httpRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		
		loggingService.sendExceptionLog(httpRequest, ex);
	}
}
