package homemedia.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import homemedia.data.InsertMetadataTagRowProvider;
import homemedia.data.JDBCUtilities;
import java.util.Set;

public class MetadataRows {
	
	private int MediaRowID;
	private JDBCUtilities util;
	private Set<MetadataRow> items = new HashSet<MetadataRow>();
	
	public MetadataRows(JDBCUtilities util, int mediaRowID) {
		MediaRowID = mediaRowID;
		this.util = util;
	}

	public int getMediaRowID() {
		return MediaRowID;
	}
	
//	public void addItem(int MetadataRowID, String value) {
//		items.put(MetadataRowID, value);
//	}
	
	public Iterable<MetadataRow> getItems() {
		return items;
	}
	
	public void fillItems(Map<MetadataTag, String> metadata) throws SQLException {
		Connection conn = util.getConnection();
		InsertMetadataTagRowProvider insertMetadataTagRow = 
				new InsertMetadataTagRowProvider(conn, null);
		for(Entry<MetadataTag, String> tagEntry : metadata.entrySet()) {
			int TagRowID;
			insertMetadataTagRow.setmDataTag(tagEntry.getKey());
			TagRowID = insertMetadataTagRow.execute();
			if(TagRowID != -1) {
				MetadataRow newRow = new MetadataRow(tagEntry.getKey(), TagRowID);
				newRow.setValue(tagEntry.getValue());
				items.add(newRow);
			}
				
		}
		util.closeConnection(conn);
	}
}
