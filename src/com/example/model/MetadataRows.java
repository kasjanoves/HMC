package com.example.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class MetadataRows {
	private int MediaRowID;
	private JDBCUtilities util;
	private Map<Integer, String> items = new HashMap<Integer, String>();
	
	public MetadataRows(JDBCUtilities util, int mediaRowID) {
		MediaRowID = mediaRowID;
		this.util = util;
	}

	public int getMediaRowID() {
		return MediaRowID;
	}
	
	public void addItem(int MetadataRowID, String value) {
		items.put(MetadataRowID, value);
	}
	
	public Iterable<Entry<Integer, String>> getItems() {
		return items.entrySet();
	}
	
	public void fillItems(Map<MetadataTag, String> metadata) throws ClassNotFoundException, SQLException {
		Connection conn = util.getConnection();
		//MetadataTag mDataRow = new MetadataTag();
		for(Entry<MetadataTag, String> tagEntry : metadata.entrySet()) {
			int TagRowID;
			TagRowID = DBTables.insertMetadataTagRow(conn, tagEntry.getKey());
			if(TagRowID != -1)
				items.put(TagRowID, tagEntry.getValue());
		}
		util.closeConnection(conn);
	}
}
