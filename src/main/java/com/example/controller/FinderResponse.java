package com.example.controller;

public class FinderResponse {
	
	private String filepath;
	private String fileMimeType;
	private String fileCreationDt;
	private String fileModifiedDt;
	private String fileTags;
	private String hightlightText;
	
	
	public String getHightlightText() {
		return hightlightText;
	}
	public void setHightlightText(String hightlightText) {
		this.hightlightText = hightlightText;
	}
	public String getFilepath() {
		return filepath;
	}
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	public String getFileMimeType() {
		return fileMimeType;
	}
	public void setFileMimeType(String fileMimeType) {
		this.fileMimeType = fileMimeType;
	}
	public String getFileCreationDt() {
		return fileCreationDt;
	}
	public void setFileCreationDt(String fileCreationDt) {
		this.fileCreationDt = fileCreationDt;
	}
	public String getFileModifiedDt() {
		return fileModifiedDt;
	}
	public void setFileModifiedDt(String fileModifiedDt) {
		this.fileModifiedDt = fileModifiedDt;
	}
	public String getFileTags() {
		return fileTags;
	}
	public void setFileTags(String fileTags) {
		this.fileTags = fileTags;
	}

}
