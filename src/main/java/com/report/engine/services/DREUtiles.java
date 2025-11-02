package com.report.engine.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class DREUtiles {

	@SuppressWarnings("unused")
	public boolean cvl(String val) {
		if (val.trim().isEmpty()) {
			return false;
		} else if (val.length() <= 0) {
			return false;
		} else if (val.equals(null) || val.equals(" ")) {
			return false;
		} else if (val == null) {
			return false;
		} else {
			return true;
		}
	}

	public String nvl(String val, String defaultval) {
		if (val.trim().isEmpty() || val.length() <= 0 || val == null) {
			return defaultval;
		} else {
			return val;
		}
	}

	public String getTimeStamp() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:MM:ss");
		Date d = new Date(System.currentTimeMillis());
		String a = sdf.format(d);
		return a;
	}
	
	public String dateconverter(String date) {
	    String result = null;
	    try {
	        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
	        Date parsedDate = inputFormat.parse(date);
	        
	        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yy");
	        result = outputFormat.format(parsedDate);
	    } catch (ParseException e) {
	        e.printStackTrace();
	    }
	    return result;
	}

	public static void main(String[] args) {

	}

}