package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.lecene.FileIndex;
import com.example.tika.DocumentParser;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@RestController
@EnableSwagger2
@RequestMapping("/finder")

class FinderController {
 
	@Autowired
	public FileIndex fileIndex;
 
    @GetMapping("search")
    public List<String> findAll(@RequestParam String searchStr) {
    	List<String> lst = new ArrayList<String>();
    	try {
	    	List<Document> documents = fileIndex.searchIndex("content", searchStr);
	    	for(Document document:documents) {
	    		lst.add(document.get("content"));
	    		lst.add(document.get("filename"));
	    	}
	    	if (documents.size() == 0) lst.add("NO HITS");
    	}catch(Exception ex) {ex.printStackTrace();}
        return lst;
    }
    
    @GetMapping("index")
    public void createIndex() {
        
        //call to store in memory index
        // Use Tika to parse the ppt, then use lucene to index
    	DocumentParser parser = new DocumentParser();
    	String filename = "/Users/jitendranarayan/Sample.ppt";
    	String content = parser.parseDocument(filename);
        try{
        	fileIndex.indexDocument(content, filename, "ppt");
        }catch(Exception ex) {
        	ex.printStackTrace();
        }

    }
    
    
}
