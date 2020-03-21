package com.example.lucene;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.miscellaneous.TypeAsSynonymFilter;
import org.apache.lucene.analysis.opennlp.OpenNLPPOSFilter;
import org.apache.lucene.analysis.opennlp.OpenNLPTokenizer;
import org.apache.lucene.analysis.opennlp.tools.NLPPOSTaggerOp;
import org.apache.lucene.analysis.opennlp.tools.NLPSentenceDetectorOp;
import org.apache.lucene.analysis.opennlp.tools.NLPTokenizerOp;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.analysis.util.ClasspathResourceLoader;
import org.apache.lucene.analysis.util.ResourceLoader;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
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
import org.apache.lucene.util.AttributeFactory;

import opennlp.tools.postag.POSModel;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerModel;

public class Test extends Analyzer {
	static Directory index = new RAMDirectory();
    protected TokenStreamComponents createComponents(String fieldName) {
    	Tokenizer source = null;
    	TokenFilter filter = null;
        try {

            ResourceLoader resourceLoader = new ClasspathResourceLoader(ClassLoader.getSystemClassLoader());
            
            InputStream stream = new FileInputStream(new File("/Users/jitendranarayan/en-token.bin"));
            TokenizerModel tokenizerModel = new TokenizerModel(stream); // OpenNLPOpsFactory.getTokenizerModel("en-token.bin", resourceLoader);
            NLPTokenizerOp tokenizerOp = new NLPTokenizerOp(tokenizerModel);

            InputStream stream1 = new FileInputStream("/Users/jitendranarayan/en-sent.bin");
            SentenceModel sentenceModel = new SentenceModel(stream1) ; //OpenNLPOpsFactory.getSentenceModel("en-sent.bin", resourceLoader);
            NLPSentenceDetectorOp sentenceDetectorOp = new NLPSentenceDetectorOp(sentenceModel);

            source = new OpenNLPTokenizer(
                    AttributeFactory.DEFAULT_ATTRIBUTE_FACTORY, sentenceDetectorOp, tokenizerOp);

            InputStream stream2 = new FileInputStream("/Users/jitendranarayan/en-pos-maxent.bin");
            POSModel posModel = new POSModel(stream2) ;//OpenNLPOpsFactory.getPOSTaggerModel("en-pos-maxent.bin", resourceLoader);
            NLPPOSTaggerOp posTaggerOp = new NLPPOSTaggerOp(posModel);

            filter = new OpenNLPPOSFilter(source, posTaggerOp);

            
        }
        catch (IOException e) {
            // Do something...
        	e.printStackTrace();
        }
        return new TokenStreamComponents(source, new TypeAsSynonymFilter(filter));
    }
    
    public static void main1(String args[]) throws IOException, ParseException {
    	//addIndex();
    	//serachIndex();
    	test();
    }
    
    public static void serachIndex() throws IOException, org.apache.lucene.queryparser.classic.ParseException{
    	
    	// Searching for documents containing both (at least one) adjective or (at least one) noun
    	final String searchPhrase = "VB";

    	// The maximum number of top n returned results
    	final int topN = 10;
    	DirectoryReader reader = DirectoryReader.open(index);
    	IndexSearcher searcher = new IndexSearcher(reader);

    	// fieldName was specified above to be string "body"
    	QueryParser parser = new QueryParser("body", new Test());
    	Query query = parser.parse(searchPhrase);
    	//QueryBuilder builder1 = new QueryBuilder(new Test());
    	//Query query = builder1.createPhraseQuery("body", "JJ JJ NN");
    	System.out.println(query);

    	TopDocs topDocs = searcher.search(query, topN);
    	System.out.printf("%s => %d hits\n", searchPhrase, topDocs.totalHits);
    	for(ScoreDoc scoreDoc: topDocs.scoreDocs){
    	    Document doc = searcher.doc(scoreDoc.doc);
    	    System.out.printf("\t%s\n", doc.get("body"));
    	}
    }
    
    public static void addIndex() throws IOException{
    	
    	Test analyzer = new Test();
    	IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
    	IndexWriter writer = new IndexWriter(index, indexWriterConfig);

    	final String fieldName = "body";

    	Document document1 = new Document();
    	// POS tags: [DT][JJ][JJ][NN][VBD][IN][DT][JJ][NNS][.]
    	//document1.add(new TextField(fieldName, "The quick brown fox jumped over the lazy dogs.", Field.Store.YES));
    	document1.add(new TextField(fieldName, "Give me your answer.", Field.Store.YES));
    	writer.addDocument(document1);

    	Document document2 = new Document();
    	// POS tags: [VB][PRP][TO][PRP][,][UH][.]
    	//document2.add(new TextField(fieldName, "Give it to me, baby!", Field.Store.YES));
    	document2.add(new TextField(fieldName, "Answer the question", Field.Store.YES));
    	writer.addDocument(document2);

    	Document document3 = new Document();
    	// POS tags: [NNP][NNP][VBZ][DT][JJ][NNS][.]
    	// Note that the token [Mr.] - including the dot - results in [NNP].
    	document3.add(new TextField(fieldName, "Mr. Robot is a great TV-series.", Field.Store.YES));
    	writer.addDocument(document3);

    	Document document4 = new Document();
    	// POS tags: [VB][PRP][TO][PRP][,][NNP][.]
    	document4.add(new TextField(fieldName, "Give them to us, Dalton!", Field.Store.YES));
    	writer.addDocument(document4);

    	writer.close();
    }
    
    public static void test() throws IOException{
      	final String text = "I like you";

    	Test analyzer = new Test();

    	// We're using a string reader to return a token stream. This allows us to observe the tokens
    	// while they are being processed by our analyzer.
    	TokenStream stream = analyzer.tokenStream("field", new StringReader(text));

    	// CharTermAttribute will contain the actual token word
    	CharTermAttribute termAtt = stream.addAttribute(CharTermAttribute.class);

    	// TypeAttribute will contain the OpenNLP POS (Treebank II) tag
    	TypeAttribute typeAtt = stream.addAttribute(TypeAttribute.class);

    	try {
    	    stream.reset();

    	    // Print all tokens until stream is exhausted
    	    while (stream.incrementToken()) {
    	        System.out.println(termAtt.toString() + ": " + typeAtt.type());
    	    }

    	    stream.end();

    	} finally {
    	    stream.close();
    	}
    }
}
