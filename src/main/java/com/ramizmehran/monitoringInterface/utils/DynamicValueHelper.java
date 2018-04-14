package com.ramizmehran.monitoringInterface.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ramizmehran.monitoringInterface.services.LoggingService;

public class DynamicValueHelper {
	private static final Logger LOG = LoggerFactory.getLogger(LoggingService.class);
	private static final String SEPARATOR = ".";

	// Works only for level one conversions
	public static Object getValue(Object object, String fieldName){
	    Class<?> clazz = object.getClass();
		try{
		    int index = fieldName.indexOf(SEPARATOR);
		    if(index > -1){
		    	String superFieldName = fieldName.substring(0, index);
		    	Object superObject = getValue(object, superFieldName);

	    		return getValue(superObject,fieldName.substring(index+1));
		    } else {
		    	if(object instanceof Map)
		    		return ((Map)object).get(fieldName);
		    	
			    Field field = clazz.getDeclaredField(fieldName); //Note, this can throw an exception if the field doesn't exist.
			    field.setAccessible(true);
		    	return field.get(object);
		    }
    	} catch (Exception e){
    		LOG.error("Field:{} not present",fieldName,e);
    	}
    	
    	return "";
	}
	

	// Works only for level one conversions
	public static Map<String,String> getDataMemberValues(Object object, String[] variableNames){
		Map<String,String> dataMap = new HashMap<String,String>();
				    
	    for(String fieldName:variableNames){
	    	String fieldKeyName = fieldName.substring(fieldName.lastIndexOf(SEPARATOR)+1);
	    	Object data = null;
	    	try {
	    		data = getValue(object, fieldName);
	    	} catch (NullPointerException e){
	    		LOG.error("Null in get dynamic value for :{}:{}",object,fieldName);
	    	}
	    	if(data != null)
	    		dataMap.put(fieldKeyName, data.toString());
	    }
	    

		return dataMap;
	}

	public static Map<String, String> getHeaderValues(HttpServletRequest httpRequest, String[] logHeaderParamNames) {
		Map<String, String> headerMap = new HashMap<String, String>();
		for(String headerKey:logHeaderParamNames){
			headerMap.put(headerKey, httpRequest.getHeader(headerKey));
		}

		return headerMap;
	}
}
