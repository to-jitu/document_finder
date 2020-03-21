package com.example.lucene;

import org.apache.lucene.document.Document;

public class SearchedDocument {
	
	private String highLightText;
	private Document document;
	public String getHighLightText() {
		return highLightText;
	}
	public void setHighLightText(String highLightText) {
		this.highLightText = highLightText;
	}
	public Document getDocument() {
		return document;
	}
	public void setDocument(Document document) {
		this.document = document;
	}

}
