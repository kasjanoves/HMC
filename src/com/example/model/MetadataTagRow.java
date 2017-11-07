package com.example.model;

public class MetadataTagRow {
	private String Destination;
	private String Directory;
	private String Tag;
	private String Type;
	public final static String TABLE_NAME = "METADATA_TYPES";
	
	public String getDestination() {
		return Destination;
	}
	public void setDestination(String destination) {
		Destination = destination;
	}
	public String getDirectory() {
		return Directory;
	}
	public void setDirectory(String directory) {
		Directory = directory;
	}
	public String getTag() {
		return Tag;
	}
	public void setTag(String tag) {
		Tag = tag;
	}
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
	}
		
}
