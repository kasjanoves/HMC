package com.example.model;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
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
 * @author kasjyanoves
 *
 */
public class ImageMetadataReaderImpl implements MediaMetadataReader {
	
	private Map<String, Set<String>> destination;
	
	public ImageMetadataReaderImpl(Map<String, Map<String, Set<String>>> reqMetadata) {
		destination = reqMetadata.get("image");
	}

	public void extractMetadata(File file, MetadataRows mdataRows, JDBCUtilities util) 
			throws ClassNotFoundException, SQLException {
	
		Map<String, Map<String, String>> mmap = new HashMap<String, Map<String, String>>();
		
		try {
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            for (Directory directory : metadata.getDirectories()) {
            	Set<String> reqDirectory = destination.get(directory.getName());
            	if(reqDirectory != null) {
	            	Map<String, String> mTags = new HashMap<String, String>();
	                for (Tag tag : directory.getTags()) {
	                    //System.out.println(tag);
	                	if(reqDirectory.contains(tag.getTagName())) {
	                		mTags.put(tag.getTagName(), tag.getDescription());
	                	}
	                }
	                if(!mTags.isEmpty()) mmap.put(directory.getName(), mTags);
            	}
            }
        } catch (IOException | ImageProcessingException e) {
        	e.printStackTrace();
        }
		
		//System.out.println(mmap);
		mdataRows.fillItems(mmap);
		Connection conn = util.getConnection();
    	DBTables.insertMetadataRows(conn, mdataRows);
    	util.closeConnection(conn);
	}
}
