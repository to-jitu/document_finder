package com.example.tika;

import org.apache.tika.metadata.Metadata;

public class ParsedDocument {
	
	Metadata metadata;
	String content;
	
	public Metadata getMetadata() {
		return metadata;
	}
	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
