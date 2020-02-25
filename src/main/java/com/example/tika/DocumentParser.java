package com.example.tika;

import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.tika.Tika;

public class DocumentParser {
	
	public String parseDocument(String url) {
		String content = null;
		// "/Users/jitendranarayan/Sample.ppt"
		try {
			InputStream stream = new FileInputStream(url);
			System.out.println(stream);
	        Tika tika = new Tika();
	        content = tika.parseToString(stream);
			System.out.println("Hello - " + content);
		}catch (Exception ex) {
			ex.printStackTrace();
		}
		return content;
	}

}
