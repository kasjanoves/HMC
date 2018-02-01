package homemedia.model;

import java.util.Set;

public class ImageMetadataReaderFactory implements MetadataReaderFactory {
	
	private Set<MetadataTag> reqMetadata;
	
	public ImageMetadataReaderFactory(Set<MetadataTag> reqMetadata) {
		this.reqMetadata=reqMetadata;
	}

	@Override
	public MediaMetadataReader getInstance() {
		return new ImageMetadataReaderImpl(reqMetadata);
	}

}
