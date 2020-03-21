package com.example.tika;

import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.tika.config.TikaConfig;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;

public class TikaDocumentParser {
	
	public ParsedDocument parseDocument(String url) {
		ParsedDocument document = new ParsedDocument();
		String content = null;
		try {
			InputStream stream = new FileInputStream(url);
			Metadata meta = new Metadata();
			ContentHandler handler = new BodyContentHandler();
			Parser parser = new AutoDetectParser(new TikaConfig(new  TikaDocumentParser().getClass().getClassLoader()));
			parser.parse(stream, handler, meta, new ParseContext());
			content = handler.toString();
			document.setContent(content);
			document.setMetadata(meta);		
		}catch (Exception ex) {
			ex.printStackTrace();
		}
		return document;
	}
}
