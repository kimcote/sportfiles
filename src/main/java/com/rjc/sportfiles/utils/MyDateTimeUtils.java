package com.rjc.sportfiles.utils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.stereotype.Component;

@Component
public class MyDateTimeUtils {

	public XMLGregorianCalendar getXMLGregorianCalender(LocalDateTime dateTime) {
		
		try {
			GregorianCalendar gcal = GregorianCalendar.from(dateTime.atZone(ZoneId.systemDefault()));
		
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public XMLGregorianCalendar getXMLGregorianCalendar (long tsOriginal) {
		long tsAdjusted = tsOriginal+631065600;  
    
		LocalDateTime dateAdjusted = LocalDateTime.of(1970,1,1,0,0).plus(tsAdjusted, ChronoUnit.SECONDS);
		
		return getXMLGregorianCalender(dateAdjusted);
	}
	
	public XMLGregorianCalendar addSeconds(XMLGregorianCalendar gregDate, BigDecimal seconds) {
		
		try {
			DatatypeFactory df = DatatypeFactory.newInstance();
		
			long millisecs = seconds.multiply(BigDecimal.valueOf(1000)).longValue();
//			System.out.println("milliSecs="+millisecs);
//			System.out.println("gregDate B4="+gregDate);
			gregDate.add(df.newDuration(millisecs));

//			System.out.println("gregDate AF="+gregDate);
			return gregDate;
		
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
//		LocalDateTime timeB = gregDate.toGregorianCalendar().toZonedDateTime().toLocalDateTime();
//		
//		timeB=timeB.plus(seconds, ChronoUnit.SECONDS);
//		return getXMLGregorianCalender(timeB);
	}
	
	public XMLGregorianCalendar minusSeconds(XMLGregorianCalendar gregDate, int seconds) {
		
		LocalDateTime timeB = gregDate.toGregorianCalendar().toZonedDateTime().toLocalDateTime();
		
		timeB=timeB.minus(seconds, ChronoUnit.SECONDS);
		return getXMLGregorianCalender(timeB);
	}
	
	public String clientDateString(XMLGregorianCalendar gregdate) {
		
	  TimeZone tz = TimeZone.getTimeZone("UTC");
	  DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
	  df.setTimeZone(tz); // strip timezone
	  return df.format(gregdate);
	}
}
