package com.example.model;

import java.io.File;
import java.util.Map;

import javax.imageio.ImageIO;

import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.embed.swing.JFXPanel;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class VideoThumbCreatorImpl implements MediaThumbnailCreator {
	
	//to avoid "Toolkit not initialized" exception
	final JFXPanel fxPanel = new JFXPanel();
	
	public String getThumbnail(File file, String relPath) {
			
		String videoFileName = FileUtils.ExctractFileName(file.getAbsolutePath());
		String thumbFileName = FileUtils.FileNameWithoutExt(videoFileName)+"thmb.png";
		String thumbPath = file.getAbsolutePath().replaceAll(videoFileName,	thumbFileName);
				
		Media media = new Media(file.toURI().toString());
		ObservableMap<String, Object> metadata = media.getMetadata();
		metadata.addListener((MapChangeListener<String, Object>) change -> {
			System.out.println("MapChangeListener");
		});
		metadata = media.getMetadata();
		MediaPlayer mediaPlayer = new MediaPlayer(media);
		mediaPlayer.setOnReady(new Runnable() {
			public void run() {
				System.out.println("getThumbnail MediaPlayer ready");
				int width = mediaPlayer.getMedia().getWidth();
				int height = mediaPlayer.getMedia().getHeight();
				WritableImage wim = new WritableImage(width, height);
				MediaView mv = new MediaView();
				mv.setFitWidth(width);
				mv.setFitHeight(height);
				mv.setMediaPlayer(mediaPlayer);
				mv.snapshot(null, wim);
				try {
				    ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", new File(thumbPath));
				} catch (Exception s) {
					s.printStackTrace();
				    System.out.println(s);
				} finally {
					mediaPlayer.dispose();
				}			
			}
		});
						
		return relPath.replaceAll(videoFileName, thumbFileName);
	}

}
