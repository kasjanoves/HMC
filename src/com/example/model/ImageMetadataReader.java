package com.example.model;

import java.util.Map;


/**
 * https://github.com/drewnoakes/metadata-extractor/releases
 * @author kasjyanoves
 *
 */
public class ImageMetadataReader implements MediaMetadataReader {

	public Map<String, Map<String, String>> getMetadata(String fileName) {
	
		Map<String, Map<String, String>> mmap = null;
		
		try {
            Metadata metadata = ImageMetadataReader.readMetadata(file);

            print(metadata);
        } catch (ImageProcessingException e) {
            // handle exception
        } catch (IOException e) {
            // handle exception
        }
		
		return mmap;
	}
}
