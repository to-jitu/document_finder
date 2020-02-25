package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.lecene.FileIndex;
import com.example.tika.DocumentParser;

@SpringBootApplication
public class WebConfig {
	

	
    public static void main(String[] args) {
        SpringApplication.run(WebConfig.class, args);
    }
}