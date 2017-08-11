package com.example.model;

import java.util.Map;

public interface MediaMetadataReader {
	public Map<String, Map<String, String>> getMetadata(String fileName);
}
