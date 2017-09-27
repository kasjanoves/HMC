package com.example.model;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class VideoMetadataReaderImpl implements MediaMetadataReader {
	private Map<String, Set<String>> destination;
	private Map<String, Map<String, String>> mmap = new HashMap<String, Map<String, String>>();
	
	//to avoid "Toolkit not initialized" exception
	final JFXPanel fxPanel = new JFXPanel();
	
	public VideoMetadataReaderImpl(Map<String, Map<String, Set<String>>> reqMetadata) {
		destination = reqMetadata.get("video");
	}

	public void extractMetadata(File file, MetadataRows mdataRows, JDBCUtilities util)
			throws ClassNotFoundException, SQLException {
		Media media = new Media(file.toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(media);
		mediaPlayer.setOnReady(new Runnable() {
			public void run() {
				System.out.println("extractMetadata MediaPlayer ready");
				Map<String, Object> metadata = media.getMetadata();
				System.out.println(metadata);
			}
		});
		
	}
	
	

}
