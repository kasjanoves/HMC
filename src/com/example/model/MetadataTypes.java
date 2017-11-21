package com.example.model;

import java.util.Arrays;

public enum MetadataTypes {
	
	NUM(new String[] {"=","<",">","<=",">=","between"}),
	DATETIME(new String[] {"=","<",">","<=",">=","between"}),
	STRING(new String[] {"=","like"});
	
	private String[] Comparators;
	private MetadataTypes(String[] comp) {
		Comparators = comp;
	}
	
	public Iterable<String> Comparators() {
		return Arrays.asList(Comparators);
	}
}
