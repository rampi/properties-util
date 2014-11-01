package cl.i2b.properties.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface Properties {
	
	String path() default "";
	
	String driverClassName() default "";
	
	String jdbc() default "";
	
	String username() default "";
	
	String password() default "";
	
	String query() default "";
	
	String key() default "";
	
	String value() default "";

}