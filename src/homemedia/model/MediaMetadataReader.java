package homemedia.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import com.drew.imaging.ImageProcessingException;

public interface MediaMetadataReader {
		
	/**
	 * @param file
	 * @return Map<MetadataTag, String>
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ImageProcessingException 
	 */
	Map<MetadataTag, String> extractMetadata(File file) 
			throws FileNotFoundException, IOException, ImageProcessingException;
	
	Map<MetadataTag, String> getAllMetadata();
	
	Map<MetadataTag, String> getRequiredMetadata();
}
