package com.example.prettytime;

import java.util.Date;
import java.util.List;

import org.ocpsoft.prettytime.nlp.PrettyTimeParser;

public class DateTimeFormat {
	
	public static List<Date> formatDate(String dateTimeText) {
		
		List<Date> dates = new PrettyTimeParser().parse(dateTimeText);
	    return dates;
	}

}
