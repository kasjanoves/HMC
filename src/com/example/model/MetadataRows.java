package com.example.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class MetadataRows {
	private int MediaRowID;
	private String destination;
	private Map<Integer, String> items = new HashMap<Integer, String>();
	
	public MetadataRows(int mediaRowID, String destination) {
		MediaRowID = mediaRowID;
		this.destination = destination;
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
	
	public void fillItems(Map<String, Map<String, String>> metadataMap) throws ClassNotFoundException, SQLException {
		JDBCUtilities util = new JDBCUtilities("root","root");
		Connection conn = util.getConnection();
		MetadataTagRow mDataRow = new MetadataTagRow();
		for(Entry<String, Map<String, String>> dirEntry : metadataMap.entrySet()) {
			for(Entry<String, String> tagEntry : dirEntry.getValue().entrySet()) {
				int TagRow;
				mDataRow.setDestination(destination);
				mDataRow.setDirectory(dirEntry.getKey());
				mDataRow.setTag(tagEntry.getKey());
				TagRow = DBTables.insertMetadataTagRow(conn, mDataRow);
				if(TagRow != -1)
					items.put(TagRow, tagEntry.getValue());
			}
		}
		util.closeConnection(conn);
	}
}
