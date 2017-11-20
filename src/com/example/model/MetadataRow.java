package com.example.model;

import java.sql.Timestamp;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MetadataRow {

	private MetadataTag tag;
	private String StringValue;
	private float NumValue;
	private Timestamp DateTimeValue;
	private int RowID;
			
	public MetadataRow(MetadataTag tag, int RowID) {
		this.tag = tag;
		this.RowID = RowID;
	}
		
	public void setValue(String stringValue) {
		StringValue = stringValue;
		//try to parsing 
		if(tag.getType().equals("Num")) {
    		Matcher m = Pattern.compile("\\d+").matcher(StringValue);
    		if(m.find()){
    			String val = m.group();
    			NumValue = Float.parseFloat(val);
    		}
    	}
    	if(tag.getType().equals("DateTime")) {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.ENGLISH);
    		Date date = sdf.parse(StringValue, new ParsePosition(0));
    		if(date != null)
    			DateTimeValue = new java.sql.Timestamp(date.getTime());
    	}
	}

	public MetadataTag getTag() {
		return tag;
	}

	public String getStringValue() {
		return StringValue;
	}

	public int getRowID() {
		return RowID;
	}
	
	public float getNumValue() {
		return NumValue;
	}

	public Timestamp getDateTimeValue() {
		return DateTimeValue;
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + tag.hashCode();
		result = 31 * result + StringValue.hashCode();
		result = 31 * result + RowID;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == this) return true;
		if(obj instanceof  MetadataRow)
			return tag.equals(((MetadataRow) obj).tag)&&
					StringValue.equals(((MetadataRow) obj).StringValue)&&
					RowID == ((MetadataRow) obj).RowID;
		return false;
	}

	@Override
	public String toString() {
		return tag.toString()+
				" ("+RowID+") "+
				": "+StringValue;
	}

}
