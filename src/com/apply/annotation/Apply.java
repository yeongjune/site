package com.apply.annotation;

public @interface Apply {
	
	public String name();
	
	public boolean display() default false;
	
}
