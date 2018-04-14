package com.ramizmehran.monitoringInterface.beans.requests;

import java.util.Map;

public class ErrorLogRequest {
	private String applicationName;
	private String requestRandomNumber;
	private String time;
	private String className;
	private String content;
	private String clientIp;
	private Object exception;
	private Map<String, String> extraParams;

	public ErrorLogRequest(String requestRandomNumber, String time, String className, String content,
			Object exception) {
		super();
		this.requestRandomNumber = requestRandomNumber;
		this.time = time;
		this.className = className;
		this.content = content;
		this.exception = exception;
	}
	public ErrorLogRequest(MeterRequest req, Exception ex,long reqStartTime) {
		applicationName = req.getApplicationName();
		requestRandomNumber = req.getRequestRandomNumber();
		time =  Long.toString((System.currentTimeMillis() - reqStartTime));
		className = req.getClassName();
		content = req.getContent();
		exception = ex;
		clientIp = req.getClientIp();
		extraParams = req.getExtraParams();
	}
	public String getRequestRandomNumber() {
		return requestRandomNumber;
	}
	public void setRequestRandomNumber(String requestRandomNumber) {
		this.requestRandomNumber = requestRandomNumber;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
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
	public Object getException() {
		return exception;
	}
	public void setException(Object exception) {
		this.exception = exception;
	}

	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	public String getClientIp() {
		return clientIp;
	}
	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}
	public Map<String, String> getExtraParams() {
		return extraParams;
	}

	public void setExtraParams(Map<String, String> extraParams) {
		this.extraParams = extraParams;
	}
	@Override
	public String toString() {
		return requestRandomNumber + ":" + time + ":" + className + ":" + content;
	}
}
