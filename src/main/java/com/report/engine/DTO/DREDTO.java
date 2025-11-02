package com.report.engine.DTO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DREDTO {

	@Value("${DRE.DB.INFO.URL}")
	private String dburl;

	@Value("${DRE.DB.INFO.USN}")
	private String dbunm;

	@Value("${DRE.DB.INFO.PWD}")
	private String dbpwd;

	public String getDbunm() {
		return dbunm;
	}

	public void setDbunm(String dbunm) {
		this.dbunm = dbunm;
	}

	public String getDburl() {
		return dburl;
	}

	public void setDburl(String dburl) {
		this.dburl = dburl;
	}

	public String getDbpwd() {
		return dbpwd;
	}

	public void setDbpwd(String dbpwd) {
		this.dbpwd = dbpwd;
	}
}