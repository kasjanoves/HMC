package com.example.model;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class VideoThumbCreatorImpl implements MediaThumbnailCreator {
		
	public String getThumbnail(File file, String relPath) {
		
		String videoFileName = FileUtils.ExctractFileName(file.getAbsolutePath());
		String thumbFileName = FileUtils.FileNameWithoutExt(videoFileName)+"thmb.png";
		String thumbPath = file.getAbsolutePath().replaceAll(videoFileName,	thumbFileName);
				
		Media media = new Media(file.toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(media);
		mediaPlayer.setOnReady(new Runnable() {
			public void run() {
				System.out.println("MediaPlayer ready!");
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
				    System.out.println(s);
				}			
			}
		});
						
		return relPath.replaceAll(videoFileName, thumbFileName);
	}

}
