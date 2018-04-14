package com.ramizmehran.monitoringInterface.beans.requests;

import java.util.Map;

public class MeterRequest {
	private String requestRandomNumber;
	private String processingTime;
	private String className;
	private String content;
	private String response;
	private String statusCode;
	private String clientIp;
	private String applicationName;
	private Map<String, String> extraParams;
	private ErrorLogRequest error;

	public MeterRequest(String requestRandomNumber, String processingTime, String className, String content,
			String response, String statusCode, String clientIp, String applicationName) {
		super();
		this.requestRandomNumber = requestRandomNumber;
		this.processingTime = processingTime;
		this.className = className;
		this.content = content;
		this.response = response;
		this.statusCode = statusCode;
		this.clientIp = clientIp;
		this.applicationName = applicationName;
	}

	public MeterRequest(String requestRandomNumber, String className, String content, String clientIp, String applicationName) {
		this.requestRandomNumber = requestRandomNumber;
		this.className = className;
		this.content = content;
		this.clientIp = clientIp;	
		this.applicationName = applicationName;
		this.processingTime = Long.toString(System.currentTimeMillis());
	}

	public String getRequestRandomNumber() {
		return requestRandomNumber;
	}
	public void setRequestRandomNumber(String requestRandomNumber) {
		this.requestRandomNumber = requestRandomNumber;
	}
	public String getProcessingTime() {
		return processingTime;
	}
	public void setProcessingTime(String processingTime) {
		this.processingTime = processingTime;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getClientIp() {
		return clientIp;
	}
	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	public Map<String, String> getExtraParams() {
		return extraParams;
	}

	public void setExtraParams(Map<String, String> extraParams) {
		this.extraParams = extraParams;
	}

	public ErrorLogRequest getError() {
		return error;
	}

	public void setError(ErrorLogRequest error) {
		this.error = error;
	}

	@Override
	public String toString() {
		return "[" + requestRandomNumber + "]"+ "[" + processingTime + "]" + "[" + applicationName + "]" + 
				className + ":" + clientIp + ":" + content + ":" + statusCode + ":" + response;
	}
}
