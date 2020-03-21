package com.example.test;

import java.util.Date;
import java.util.List;

import com.example.prettytime.DateTimeFormat;

public class DateTimeFormatTest {
	
	public static void main(String args[]) {
		DateTimeFormat format = new DateTimeFormat();
		List<Date> dates = format.formatDate("Around 31st August in the year 2008");
		for(Date date:dates) {
			System.out.println(date);
		}
	}

}
