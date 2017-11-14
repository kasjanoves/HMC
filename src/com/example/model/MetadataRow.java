package com.example.model;

public class MetadataRow {

	private MetadataTag tag;
	private String value;
	private int RowID;
			
	public MetadataRow(MetadataTag tag, String value, int RowID) {
		this.tag = tag;
		this.value = value;
		this.RowID = RowID;
	}
	
	public MetadataTag getTag() {
		return tag;
	}

	public String getValue() {
		return value;
	}

	public int getRowID() {
		return RowID;
	}

	@Override
	public int hashCode() {
		return tag.hashCode()+value.hashCode()+RowID;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof  MetadataRow)
			return tag.equals(((MetadataRow) obj).tag)&&
					value.equals(((MetadataRow) obj).value)&&
					RowID == ((MetadataRow) obj).RowID;
		return super.equals(obj);
	}

	@Override
	public String toString() {
		return tag.toString()+
				" ("+RowID+") "+
				": "+value;
	}
	
	

}
