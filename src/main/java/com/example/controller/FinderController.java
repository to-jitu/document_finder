package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.lucene.FileIndex;
import com.example.lucene.SearchedDocument;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@RestController
@EnableSwagger2
@RequestMapping("/finder")

class FinderController {
	
	private FileIndex fileIndex;
 
    @GetMapping("search")
    public List<FinderResponse> findAll(@RequestParam String searchStr) {
    	List<FinderResponse> lst = new ArrayList<FinderResponse>();
    	System.out.println("Search String - " + searchStr);
    	FinderResponse response = new FinderResponse();
    	
    	try {
    		if(searchStr != null && fileIndex != null) {
    			//searchStr = searchStr.toLowerCase();
    			List<SearchedDocument> documents = null;
    			//search Index
    			documents = fileIndex.searchIndex(searchStr);
	    		
		    	for(SearchedDocument doc: documents) {
		    		response = new FinderResponse();
		    		Document document = doc.getDocument();
		    		response.setFilepath(document.get("filename"));
		    		if(document.get("createdStr") != null) {
		    			response.setFileCreationDt(document.get("createdStr"));
		    		}
		    		if(document.get("modifiedStr") != null) {
		    			response.setFileModifiedDt(document.get("modifiedStr"));
		    		}
		    		response.setHightlightText(doc.getHighLightText());
		    		lst.add(response);
		    	}
		    	if (documents.size() == 0) {
		    		response.setFilepath("NO HITS");
		    		lst.add(response);
		    	}
		    	
    		}
    	}catch(Exception ex) {ex.printStackTrace();}
        return lst;
    }
    
    @GetMapping("index")
    public void createIndex(@RequestParam String dataDir, @RequestParam String indexDir) {
       
        //call to store in memory index
        try{
        	fileIndex = new FileIndex(indexDir);
        	fileIndex.indexDocument(dataDir);
        }catch(Exception ex) {
        	ex.printStackTrace();
        }

    }
    
    
}
