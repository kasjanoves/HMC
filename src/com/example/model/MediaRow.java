package com.example.model;

import java.util.Date;

public class MediaRow {
	private String MediaType;
	private String Description;
	private String RelativePath;
	private long Size;
	private Date CreationDate;
	public final static String TABLE_NAME = "MEDIA";
	
	public MediaRow() {	}

	public String getMediaType() {
		return MediaType;
	}

	public void setMediaType(String mediaType) {
		MediaType = mediaType;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getRelativePath() {
		return RelativePath;
	}

	public void setRelativePath(String relativePath) {
		RelativePath = relativePath;
	}

	public long getSize() {
		return Size;
	}

	public void setSize(long size) {
		Size = size;
	}

	public Date getCreationDate() {
		return CreationDate;
	}

	public void setCreationDate(Date creationDate) {
		CreationDate = creationDate;
	}
	
	
}
