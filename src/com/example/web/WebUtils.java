package com.example.web;

public class WebUtils {

	/**
	 * Extracts first part before "/" from MIME type
	 * @param contentType
	 * @return
	 */
	public static String ExtractHeader(String contentType) {
		String mediaType = contentType; 
		int delimiter = contentType.indexOf("/");
		if(delimiter>-1)
			mediaType = contentType.substring(0, delimiter);
		return mediaType;
	}
}
