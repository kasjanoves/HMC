package com.example.model;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

public interface MediaMetadataReader {
		
	public void extractMetadata(File file, MetadataRows mdataRows, JDBCUtilities util) 
			throws ClassNotFoundException, SQLException;
}
