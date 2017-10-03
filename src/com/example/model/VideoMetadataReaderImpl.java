package com.example.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.mp4parser.Box;
import org.mp4parser.IsoFile;
import org.mp4parser.boxes.iso14496.part12.MovieBox;

public class VideoMetadataReaderImpl implements MediaMetadataReader {
	private Map<String, Set<String>> destination;
	private Map<String, Map<String, String>> mmap = new HashMap<String, Map<String, String>>();
				
	public VideoMetadataReaderImpl(Map<String, Map<String, Set<String>>> reqMetadata) {
		destination = reqMetadata.get("video");
	}

	public Map<String, Map<String, String>> extractMetadata(File file) throws IOException {
		
		if (!file.exists()) {
            throw new FileNotFoundException("File " + file.getPath() + " not exists");
        }

        if (!file.canRead()) {
            throw new IllegalStateException("No read permissions to file " + file.getPath());
        }
        FileInputStream fis = new FileInputStream(file);
        IsoFile isoFile = new IsoFile(fis.getChannel());
		
//        AppleNameBox nam = Path.getPath(isoFile, "/moov[0]/udta[0]/meta[0]/ilst/©nam");
//        String xml = nam.getValue();
        MovieBox moov = isoFile.getMovieBox();
        for(Box b : moov.getBoxes()) {
        	System.out.println(b.getType());
            System.out.println(b);
        }
        
        isoFile.close();
        fis.close();
                            
		return mmap;
	}

		

}
