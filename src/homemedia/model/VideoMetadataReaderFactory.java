package homemedia.model;

import java.util.Set;

public class VideoMetadataReaderFactory implements MetadataReaderFactory {
	
	private Set<MetadataTag> reqMetadata;
	
	public VideoMetadataReaderFactory(Set<MetadataTag> reqMetadata) {
		this.reqMetadata=reqMetadata;
	}

	@Override
	public MediaMetadataReader getInstance() {
		return new VideoMetadataReaderImpl(reqMetadata);
	}

}
