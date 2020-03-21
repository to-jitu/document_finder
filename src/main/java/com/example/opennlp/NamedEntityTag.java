package com.example.opennlp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.util.Span;

public class NamedEntityTag {
	SimpleTokenizer tokenizer;
	
	public  NamedEntityTag(){
		tokenizer = SimpleTokenizer.INSTANCE;
	}
	
	public Boolean findPersons(String content) throws IOException {
		String str[] = null;		
	    String[] tokens = tokenizer.tokenize(content);	 
	    //File file = ResourceUtils.getFile("classpath:nermodels/en-ner-person.bin");
	    InputStream inputStreamNameFinder = getClass().getClassLoader().getResourceAsStream("nermodels/en-ner-person.bin");

	      //Loading the NER-person model 
	    //InputStream inputStreamNameFinder = new 
	         //FileInputStream("src/main/resources/nermoels/en-ner-person.bin"); 
	    
	    TokenNameFinderModel model = new TokenNameFinderModel(inputStreamNameFinder);
	    //Instantiating the NameFinderME class 
	    NameFinderME nameFinder = new NameFinderME(model);       
	    //Finding the names in the sentence 
	    Span nameSpans[] = nameFinder.find(tokens);        
	    if (nameSpans.length > 0)
	    	return true;
	    else 
	    	return false;
	}

	public Boolean findLocations(String content) throws IOException {
		String str[] = null;
	    String[] tokens = tokenizer.tokenize(content);	 
	    //File file = ResourceUtils.getFile("classpath:nermodels/en-ner-location.bin");
	    //InputStream inputStreamNameFinder = new FileInputStream(file);
	    InputStream inputStreamNameFinder = getClass().getClassLoader().getResourceAsStream("nermodels/en-ner-location.bin");

	      //Loading the NER-person model 
	    //InputStream inputStreamNameFinder = new 
	      //   FileInputStream(new File("src/main/resources/nermoels/en-ner-location.bin").getAbsolutePath()); 
	    
	    TokenNameFinderModel model = new TokenNameFinderModel(inputStreamNameFinder);
	    //Instantiating the NameFinderME class 
	    NameFinderME nameFinder = new NameFinderME(model);       
	    //Finding the names in the sentence 
	    Span nameSpans[] = nameFinder.find(tokens);        
	    if (nameSpans.length > 0)
	    	return true;
	    else 
	    	return false;
	}
	
	public Boolean findDates(String content) throws IOException {
		List<NamedEntity> entityLst = new ArrayList<NamedEntity>();
		NamedEntity entity = null ;
	    String[] tokens = tokenizer.tokenize(content);	 
	    
	   // File file = ResourceUtils.getFile("classpath:nermodels/en-ner-date.bin");
	    //InputStream inputStreamNameFinder = new FileInputStream(file);
	    InputStream inputStreamNameFinder = getClass().getClassLoader().getResourceAsStream("nermodels/en-ner-date.bin");

	    //Loading the NER-person model 
	    //InputStream inputStreamNameFinder = new 
	         //FileInputStream(new File("src/main/resources/nermoels/en-ner-date.bin").getAbsolutePath()); 
	    
	    TokenNameFinderModel model = new TokenNameFinderModel(inputStreamNameFinder);
	    //Instantiating the NameFinderME class 
	    NameFinderME nameFinder = new NameFinderME(model);       
	    //Finding the names in the sentence 
	    Span nameSpans[] = nameFinder.find(tokens);        
	    //Printing the names and their spans in a sentence 
	    if (nameSpans.length > 0)
	    	return true;
	    else 
	    	return false;
	}
	
	public Boolean findOrg(String content) throws IOException {
		List<NamedEntity> entityLst = new ArrayList<NamedEntity>();
		NamedEntity entity = null ;
	    String[] tokens = tokenizer.tokenize(content);	 
	    //File file = ResourceUtils.getFile("classpath:nermodels/en-ner-organization.bin");
	    //InputStream inputStreamNameFinder = new FileInputStream(file);
	    InputStream inputStreamNameFinder = getClass().getClassLoader().getResourceAsStream("/nermodels/en-ner-organization.bin");

	      //Loading the NER-person model 
	    //InputStream inputStreamNameFinder = new 
	      //   FileInputStream(new File("src/main/resources/nermoels/en-ner-organization.bin").getAbsolutePath()); 
	    
	    TokenNameFinderModel model = new TokenNameFinderModel(inputStreamNameFinder);
	    //Instantiating the NameFinderME class 
	    NameFinderME nameFinder = new NameFinderME(model);       
	    //Finding the names in the sentence 
	    Span nameSpans[] = nameFinder.find(tokens);        
	    //Printing the names and their spans in a sentence 
	    if (nameSpans.length > 0)
	    	return true;
	    else 
	    	return false;
	}
}
