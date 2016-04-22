package com.base.util;

import java.sql.Types;

import org.hibernate.Hibernate;
import org.hibernate.dialect.MySQLDialect;

public class ExtMysqlDialect extends MySQLDialect {
	
	public ExtMysqlDialect() {
	  super();
	  registerHibernateType(Types.LONGVARCHAR,Hibernate.STRING.getName());
	 }
}
