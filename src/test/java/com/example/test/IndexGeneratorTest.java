package com.example.test;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;

import com.example.lucene.FileIndex;
import com.example.lucene.SearchedDocument;

public class IndexGeneratorTest {
	
	public static  void main(String args[]) throws java.text.ParseException, ParseException {
		String dataDir =  "/Users/jitendranarayan/datadir";
		String indexDir =  "/Users/jitendranarayan/disk";
		// create index
		FileIndex index;
		try {
			index = new FileIndex(indexDir); 
			//index.indexDocument(dataDir);//1501761033000
			// serach index
			System.out.println(""); 
			System.out.println("Searching Document");
			//List<SearchedDocument> docs = index.searchIndex("content", "Autoshapes");
			List<SearchedDocument> docs = index.searchIndex("fileName:Jitendra");
			for(SearchedDocument document:docs) {
				Document doc = document.getDocument();
				System.out.println("modifiedStr - " + doc.get("modifiedStr"));
				System.out.println("createdStr - " + doc.get("createdStr"));
				System.out.println("FileName - " + doc.get("filename"));
			}
		}catch(IOException ex) {
			ex.printStackTrace();
		}
		
	}

}
