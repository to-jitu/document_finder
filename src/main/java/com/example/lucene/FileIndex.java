package com.example.lucene;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.uhighlight.UnifiedHighlighter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.springframework.context.annotation.Configuration;

import com.example.opennlp.NamedEntityTag;
import com.example.prettytime.DateTimeFormat;
import com.example.tika.ParsedDocument;
import com.example.tika.TikaDocumentParser;

@Configuration
public class FileIndex {
	
	private Path indexPath;
	private Directory indexDir;
	private StandardAnalyzer analyzer;
	IndexWriterConfig indexWriterConfig;
	private IndexWriter writer;
	private TikaDocumentParser tikaDocParser;
	
	public FileIndex() {}
	
	public FileIndex(String indexDirPath) throws IOException{
		indexPath = FileSystems.getDefault().getPath(indexDirPath);
		indexDir = FSDirectory.open(indexPath);
		analyzer = new StandardAnalyzer();
		indexWriterConfig = new IndexWriterConfig(analyzer);
		indexWriterConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
		writer = new IndexWriter(indexDir, indexWriterConfig);
		tikaDocParser = new TikaDocumentParser();
	}
	
	private void deleteDocument() throws IOException{
		//delete indexes of all files in the directory and sub directory
		writer.deleteAll();
		writer.commit();
	}
	
	private void indexDocs(final IndexWriter writer, Path path) throws IOException 
    {
        //Directory?
        if (Files.isDirectory(path)) 
        {
            //Iterate directory
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() 
            {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException 
                {
                    try
                    {
                        //Index this file
                        indexDoc(writer, file, attrs.lastModifiedTime().toMillis());
                    } 
                    catch (IOException ioe) 
                    {
                        ioe.printStackTrace();
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } 
        else
        {
            //Index this file
            indexDoc(writer, path, Files.getLastModifiedTime(path).toMillis());
        }
    }
 
    private void indexDoc(IndexWriter writer, Path file, long lastModified) throws IOException 
    {
        try (InputStream stream = Files.newInputStream(file)) 
        {
        	// Tika Processing
        	System.out.println("Tika Processing for - " + file.toString());
        	ParsedDocument parsedDocument = tikaDocParser.parseDocument(file.toString());
			Metadata metaData = parsedDocument.getMetadata();
			String content = parsedDocument.getContent();
			Date creationDt = metaData.getDate(TikaCoreProperties.CREATED);
			Date modifiedDt = metaData.getDate(TikaCoreProperties.MODIFIED);
			String contentType = metaData.get(Metadata.CONTENT_TYPE);
			System.out.println("CREATION_DATE - " + (creationDt != null ? creationDt : null) + " -> " + (creationDt != null ? creationDt.getTime() : null));
			System.out.println("LAST_MODIFIED - " + (modifiedDt != null ? modifiedDt : null) + " -> " + (modifiedDt != null ? modifiedDt.getTime() : null));
			System.out.println("CONTENT_TYPE - " + contentType);
			
            //Create lucene Document
            Document document = new Document();
			// pre proces to find NER for automatic tagging
			NamedEntityTag namedTag = new NamedEntityTag();
			// date tag
			Boolean dateTag = namedTag.findDates(content);
			// Loop to get date entity and add to index
			if( dateTag  == true) {
				System.out.println("Addin index category date");
				document.add(new TextField("category", "date", Field.Store.YES));
				//document.add(new TextField("tag", ne.getValue(), Field.Store.YES));
			}
			//person tag
			boolean personTag= namedTag.findPersons(content);
			// Loop to get date entity and add to index
			if (personTag == true){
				System.out.println("Addin index category person");
				document.add(new TextField("category", "person", Field.Store.YES));
				//document.add(new TextField("tag", ne.getValue(), Field.Store.YES));
			}
			//location tag
			boolean locTag= namedTag.findLocations(content);
			// Loop to get date entity and add to index
			if (locTag == true){
				System.out.println("Addin index category location");
				document.add(new TextField("category", "location", Field.Store.YES));
				//document.add(new TextField("tag", ne.getValue(), Field.Store.YES));
			}
			//organization tag
			boolean orgTag= namedTag.findOrg(content);
			// Loop to get date entity and add to index
			if (orgTag == true){
				System.out.println("Addin index category  ograniation");
				document.add(new TextField("category", "organization", Field.Store.YES));
				//document.add(new TextField("tag", ne.getValue(), Field.Store.YES));
			}
						
			if(creationDt != null) {
				document.add(new LongPoint("created", creationDt.getTime()));
				document.add(new TextField("createdStr", creationDt.toString(), Field.Store.YES));
			}
			if(modifiedDt != null) {
				document.add(new LongPoint("modified", modifiedDt.getTime()));
				document.add(new TextField("modifiedStr", modifiedDt.toString(), Field.Store.YES));
			}
			document.add(new TextField("contenttype", contentType, Field.Store.YES));
			document.add(new TextField("content", content, Field.Store.YES));
			document.add(new StringField("filename", file.toString(), Field.Store.YES));			
             
            //Updates a document by first deleting the document(s) 
            //containing <code>term</code> and then adding the new
            //document.  The delete and then add are atomic as seen
            //by a reader on the same index
            writer.updateDocument(new Term("filename", file.toString()), document);
        }
    }
	
	public void indexDocument (String dataDirPath)throws IOException, java.text.ParseException {
		
		// delete any existing index
		//deleteDocument();
		
		final Path docDir = Paths.get(dataDirPath);
        //Its recursive method to iterate all files and directories
        indexDocs(writer, docDir);	
	
        writer.close();
	}
	
	public List<SearchedDocument> searchIndex(String searchString) throws IOException, ParseException{
		System.out.println("Search text - " + searchString);
		BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();
		
		// Get the date range query
		List<Date> dates = DateTimeFormat.formatDate(searchString);
		Query dateRangeQuery = null;
		Boolean isDateSearch = false;
		if(dates != null && dates.size() != 0) {
			isDateSearch = true;
			dateRangeQuery = getRangeDateQuery(dates);
			booleanQuery.add(dateRangeQuery, BooleanClause.Occur.MUST);
		}
		
		// anything related to file name search - wild search
		Boolean isSearchFileName = searchString.toLowerCase().contains("filename");
		Query wildSearchQuery = null;
		if(isSearchFileName == true) {
			String str = searchString.substring(searchString.lastIndexOf(":") + 1);
			if(str.contains(" ")){
				str = str.substring(0, str.indexOf(" "));
			}
			System.out.println("filename in search string - " + str);
			//Need a Wild Search Query
			wildSearchQuery = new WildcardQuery(new Term("filename", "*"+str+"*"));
			booleanQuery.add(wildSearchQuery, BooleanClause.Occur.MUST);
		}
		
		//anything related to automatic tag
		Boolean isTagSearch = searchString.contains("#");
		if(isTagSearch == true) {
			String tagValue = searchString.substring(searchString.lastIndexOf("#") + 1);
			if(tagValue.contains(" ")){
				tagValue = tagValue.substring(0, tagValue.indexOf(" "));
			}
			System.out.println("tag value in search string - " + tagValue);
			Term tagTerm = new Term("category", tagValue);
			Query tagQuery = new TermQuery(tagTerm);
			booleanQuery.add(tagQuery, BooleanClause.Occur.MUST);
		}
		searchString = searchString.toLowerCase();
		Query generalQueryForOR = new QueryParser( "content", analyzer).parse(searchString);
		booleanQuery.add(generalQueryForOR, BooleanClause.Occur.SHOULD);
		
		
		   
		   List<SearchedDocument> searchedDocs = new ArrayList<SearchedDocument>();
		   SearchedDocument searchedDoc = null;
		  
			    IndexReader indexReader = DirectoryReader.open(indexDir);
			    IndexSearcher searcher = new IndexSearcher(indexReader);
			    System.out.println("Query to be executed - " +booleanQuery.build().toString() );
			    TopDocs topDocs = searcher.search(booleanQuery.build(), 10);
		        UnifiedHighlighter highlighter = new UnifiedHighlighter(searcher, analyzer);
		        String[] fragments = highlighter.highlight("content", booleanQuery.build(), topDocs);
		        
			    int i = 0;
			    for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			    	searchedDoc = new SearchedDocument();
			    	searchedDoc.setDocument(searcher.doc(scoreDoc.doc));
			    	searchedDoc.setHighLightText(fragments[i]);
			    	searchedDocs.add(searchedDoc);
			        i++;
			        
			    }
			    return searchedDocs;
	}
	
	public Query getRangeDateQuery(List<Date> searchDates) throws IOException{
		List<Date> dates = new ArrayList<Date>();
		Query query  = null;
		if(searchDates != null && searchDates.size() > 0) {
			System.out.println("Queried Date - " + searchDates.get(0));
			// get min and max dates : TBD
			Calendar c = Calendar.getInstance();
			c.setTime(searchDates.get(0));
			c.add(Calendar.DAY_OF_MONTH, -30);
			dates.add(c.getTime());
			System.out.println("Before Date - " + c.getTime());
			Long long1 = c.getTimeInMillis();
			c = Calendar.getInstance();
			c.setTime(searchDates.get(0));
			c.add(Calendar.DAY_OF_MONTH, 30);
			dates.add(c.getTime());
			System.out.println("After Date - " + c.getTime());
			Long long2 = c.getTimeInMillis();
			
			query = LongPoint.newRangeQuery("modified", long1, long2);
		}
	    return query;
	}

}
