package com.example.model;

import java.io.File;
import java.io.IOException;
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
	
	private Set<MetadataTag> reqMetadata;
	private final String destination = "image";
	
	public ImageMetadataReaderImpl(Set<MetadataTag> reqMetadata) {
		this.reqMetadata = reqMetadata;
	}
	
	public Map<MetadataTag, String> extractMetadata(File file) throws IOException {
		
		Map<MetadataTag, String> mmap = new HashMap<MetadataTag, String>();
		
		try {
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            for (Directory directory : metadata.getDirectories()) {
            	String dir = directory.getName();
        	   	for (Tag tag : directory.getTags()) {
                    System.out.println(tag);
        	   		MetadataTag newTag = new MetadataTag(destination, dir, tag.getTagName());
        	   		if(reqMetadata.contains(newTag)) {
        	   			for(MetadataTag reqTag : reqMetadata)
        	   				if(reqTag.equals(newTag)) {
        	   					newTag.setType(reqTag.getType());
        	   					mmap.put(newTag, tag.getDescription());
        	   				}	
                	}
                }
            }
        } catch (ImageProcessingException e) {
        	e.printStackTrace();
        }
		
		return mmap;
	}
}
