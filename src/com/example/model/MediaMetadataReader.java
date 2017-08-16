package com.example.model;

import java.io.File;
import java.util.Map;

public interface MediaMetadataReader {
		
	public Map<String, Map<String, String>> getMetadata(File file);
}
