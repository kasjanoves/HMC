package com.example.model;

public class MetadataTag {
	
	public final static String TABLE_NAME = "METADATA_TYPES";
	
	private String Destination;
	private String Directory;
	private String Tag;
	private MetadataTypes Type;
	
	public MetadataTag(String destination, String directory, String tag) {
		super();
		Destination = destination;
		Directory = directory;
		Tag = tag;
	}		
	public MetadataTag(String destination, String directory, String tag, MetadataTypes type) {
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
	public MetadataTypes getType() {
		return Type;
	}
	public void setType(MetadataTypes type) {
		Type = type;
	}
	public String toString() {
		return Destination+"\\"+Directory+"\\"+Tag;
	}
		
	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + Destination.hashCode();
		result = 31 * result + Directory.hashCode();
		result = 31 * result + Tag.hashCode();
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj == this) return true;
		if(obj instanceof MetadataTag) {
			return Destination.equals(((MetadataTag) obj).Destination)&&
					Directory.equals(((MetadataTag) obj).Directory)&&
					Tag.equals(((MetadataTag) obj).Tag);
		}
		return false;
	}
		 
}
