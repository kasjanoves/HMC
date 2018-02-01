package homemedia.model;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.RowSet;

public class MediaFactoriesSupplier {
	
	Map<String, MediaFactory> factories = new HashMap<String, MediaFactory>();
	
	public MediaFactoriesSupplier() {
		factories.put("image", new ImageFactory());
		factories.put("video", new VideoFactory());
	}
	
	public MediaFactory getFactory(String mediaType) {
		return factories.get(mediaType);
	}
	
	public MediaFactory getFactory(RowSet rs) throws SQLException {
		String mediaType = rs.getString("TYPE");
		return factories.get(mediaType);
	}
}
