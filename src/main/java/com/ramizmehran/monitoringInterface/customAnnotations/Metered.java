package com.ramizmehran.monitoringInterface.customAnnotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Metered {
	   String value() default "";
	   String[] logRequestBodyParamNames() default {};
	   String[] logHeaderParamNames() default {};
}
