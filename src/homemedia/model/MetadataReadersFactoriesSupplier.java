package homemedia.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MetadataReadersFactoriesSupplier {

	Map<String, MetadataReaderFactory> factories = new HashMap<String, MetadataReaderFactory>();
	
	public MetadataReadersFactoriesSupplier(Set<MetadataTag> reqMetadata) {
		factories.put("image", new ImageMetadataReaderFactory(reqMetadata));
		factories.put("video", new VideoMetadataReaderFactory(reqMetadata));
	}
	
	public MetadataReaderFactory getFactory(String mediaType) {
		return factories.get(mediaType);
	}
}
