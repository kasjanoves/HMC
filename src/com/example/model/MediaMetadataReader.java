package com.example.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

public interface MediaMetadataReader {
		
	public Map<String, Map<String, String>> extractMetadata(File file) 
			throws FileNotFoundException, IOException;
}
