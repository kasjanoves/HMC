package com.example.model;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;


/**
 * https://github.com/drewnoakes/metadata-extractor/releases
 * @author kasjyanoves
 *
 */
public class ImageMetadataReaderImpl implements MediaMetadataReader {

	public Map<String, Map<String, String>> getMetadata(File file) {
	
		Map<String, Map<String, String>> mmap = new HashMap<String, Map<String, String>>();
		
		try {
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            for (Directory directory : metadata.getDirectories()) {
            	Map<String, String> mTags = new HashMap<String, String>();
                for (Tag tag : directory.getTags()) {
                    //System.out.println(tag);
                	mTags.put(tag.getTagName(), tag.getDescription());
                }
                mmap.put(directory.getName(), mTags);
            }
        } catch (IOException | ImageProcessingException e) {
        	e.printStackTrace();
        }
		
		System.out.println(mmap);
		return mmap;
	}
}
