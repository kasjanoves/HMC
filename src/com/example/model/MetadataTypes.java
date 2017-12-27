package com.example.model;

import java.util.Arrays;

public enum MetadataTypes {
	
	NUM(new String[] {"=", "&lt", "&gt", "&lt=", "&gt=", "between"},
			"number",
			"NUM_VALUE"),
	DATETIME(new String[] {"=", "&lt", "&gt", "&lt=", "&gt=", "between"},
			"date",
			"DATA_VALUE"),
	STRING(new String[] {"=", "like"},
			"text",
			"VALUE");
	
	private String[] Comparators;
	private String InputType;
	private String SQLColName;
	private MetadataTypes(String[] comp, String inputType, String sqlColName) {
		Comparators = comp;
		InputType = inputType;
		SQLColName = sqlColName;
	}
	
	public Iterable<String> Comparators() {
		return Arrays.asList(Comparators);
	}

	public String getInputType() {
		return InputType;
	}

	public String getSQLColName() {
		return SQLColName;
	}
			
}
