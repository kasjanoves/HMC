package com.example.model;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;


/**
 * https://github.com/drewnoakes/metadata-extractor/releases
 *
 */
public class ImageMetadataReaderImpl implements MediaMetadataReader {
	
	private Map<String, Map<String, String>> destination;
	
	public ImageMetadataReaderImpl(Map<String, Map<String, Map<String, String>>> reqMetadata) {
		destination = reqMetadata.get("image");
	}
	
	public Map<String, Map<String, Map<String, String>>> extractMetadata(File file) throws IOException {
		Map<String, Map<String, Map<String, String>>> mmap = new HashMap<String, Map<String, Map<String, String>>>();
		
		try {
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            for (Directory directory : metadata.getDirectories()) {
            	Map<String, String> reqDirectory = destination.get(directory.getName());
            	if(reqDirectory != null) {
	            	Map<String, Map<String, String>> mTags = new HashMap<String, Map<String, String>>();
	                for (Tag tag : directory.getTags()) {
	                    //System.out.println(tag);
	                	if(reqDirectory.containsKey(tag.getTagName())) {
	                		mTags.put(tag.getTagName(), tag.getDescription());
	                	}
	                }
	                if(!mTags.isEmpty()) mmap.put(directory.getName(), mTags);
            	}
            }
        } catch (ImageProcessingException e) {
        	e.printStackTrace();
        }
		
		return mmap;
	}
}
