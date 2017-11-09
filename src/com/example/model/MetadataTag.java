package com.example.model;

public class MetadataTag {
	
	public final static String TABLE_NAME = "METADATA_TYPES";
	
	private String Destination;
	private String Directory;
	private String Tag;
	private String Type;
	
	public MetadataTag(String destination, String directory, String tag) {
		super();
		Destination = destination;
		Directory = directory;
		Tag = tag;
	}		
	public MetadataTag(String destination, String directory, String tag, String type) {
		this(destination, directory, tag);
		Type = type;
	}
	public String getDestination() {
		return Destination;
	}
	public String getDirectory() {
		return Directory;
	}
	public String getTag() {
		return Tag;
	}
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
	}
	public String toString() {
		return Destination+"\\"+Directory+"\\"+Tag;
	}
		
	@Override
	public int hashCode() {
		return Destination.hashCode()+Directory.hashCode()+Tag.hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof MetadataTag) {
			return Destination.equals(((MetadataTag) obj).Destination)&&
					Directory.equals(((MetadataTag) obj).Directory)&&
					Tag.equals(((MetadataTag) obj).Tag);
		}
		return super.equals(obj);
	}
		 
}
