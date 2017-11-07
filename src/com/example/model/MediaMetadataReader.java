package com.example.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

public interface MediaMetadataReader {
		
	/**
	 * @param file
	 * @return Map<String, Map<String, Map<String, String>>>
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public Map<String, Map<String, Map<String, String>>> extractMetadata(File file) 
			throws FileNotFoundException, IOException;
}
