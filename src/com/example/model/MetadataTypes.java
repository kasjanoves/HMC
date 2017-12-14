package com.example.model;

import java.util.Arrays;

public enum MetadataTypes {
	
	NUM(new String[] {"=", "&lt", "&gt", "&lt=", "&gt=", "between"}, "number"),
	DATETIME(new String[] {"=", "&lt", "&gt", "&lt=", "&gt=", "between"}, "date"),
	STRING(new String[] {"=", "like"}, "text");
	
	private String[] Comparators;
	private String InputType;
	private MetadataTypes(String[] comp, String inputType) {
		Comparators = comp;
		InputType = inputType;
	}
	
	public Iterable<String> Comparators() {
		return Arrays.asList(Comparators);
	}

	public String getInputType() {
		return InputType;
	}
		
}
