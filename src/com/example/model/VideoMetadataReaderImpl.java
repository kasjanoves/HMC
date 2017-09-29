package com.example.model;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.scene.media.Media;

public class VideoMetadataReaderImpl implements MediaMetadataReader {
	private Map<String, Set<String>> destination;
	private Map<String, Map<String, String>> mmap = new HashMap<String, Map<String, String>>();
	ObservableMap<String, Object> metadata;
			
	public VideoMetadataReaderImpl(Map<String, Map<String, Set<String>>> reqMetadata) {
		destination = reqMetadata.get("video");
	}

	public void extractMetadata(File file, MetadataRows mdataRows, JDBCUtilities util)
			throws ClassNotFoundException, SQLException {
//		Media media = new Media(file.toURI().toString());
//		metadata = media.getMetadata();
//		metadata.addListener((MapChangeListener<String, Object>) change -> {
//		    System.out.println("MapChangeListener");
//		});
		
	}
	
	

}
