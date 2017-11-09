package com.example.model;

public class MetadataRow extends MetadataTag{

	private String value;
	private int RowID;
	
	public MetadataRow(String destination, String directory, String tag) {
		super(destination, directory, tag);
		// TODO Auto-generated constructor stub
	}
	
	public MetadataRow(MetadataTag tag, String value, int RowID) {
		super(tag.getDestination(),
				tag.getDirectory(),
				tag.getTag());
		this.value = value;
		this.RowID = RowID;
	}

}
