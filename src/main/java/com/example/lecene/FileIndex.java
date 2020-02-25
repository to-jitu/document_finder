package com.example.lecene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileIndex {
	
	Directory memoryIndex = new RAMDirectory();
	StandardAnalyzer analyzer = new StandardAnalyzer();
	
	public void indexDocument (String content, String filename, String type)throws IOException {
		
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
		IndexWriter writter = new IndexWriter(memoryIndex, indexWriterConfig);
		Document document = new Document();
		 
		document.add(new TextField("content", content, Field.Store.YES));
		document.add(new TextField("filename", filename, Field.Store.YES));
		document.add(new TextField("type", type, Field.Store.YES));
		 
		writter.addDocument(document);
		writter.close();
	}
	
	public List<Document> searchIndex(String field, String queryString) throws ParseException, IOException{
		StandardAnalyzer analyzer = new StandardAnalyzer();
		Query query = new QueryParser(field, analyzer)
			      .parse(queryString);
			 
			    IndexReader indexReader = DirectoryReader.open(memoryIndex);
			    IndexSearcher searcher = new IndexSearcher(indexReader);
			    TopDocs topDocs = searcher.search(query, 10);
			    List<Document> documents = new ArrayList<Document>();
			    for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			        documents.add(searcher.doc(scoreDoc.doc));
			    }
			    return documents;
	}

}
