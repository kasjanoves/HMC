package com.example.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

public interface MediaMetadataReader {
		
	/**
	 * @param file
	 * @return Map<MetadataTag, String>
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public Map<MetadataTag, String> extractMetadata(File file) 
			throws FileNotFoundException, IOException;
}
