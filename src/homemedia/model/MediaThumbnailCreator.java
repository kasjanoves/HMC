package homemedia.model;

import java.io.File;

public interface MediaThumbnailCreator {
	String getThumbnail(File file, String relPath);
}
