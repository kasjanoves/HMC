package com.example.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;

public class DBTables {
		
	public static void createMediaTable(Connection con, String dbName) throws SQLException {
	    String createString =
	    	"create table if not exists " + dbName +
	        ".MEDIA" +
	        "(ID integer AUTO_INCREMENT, " +
	        "TYPE varchar(15), " +
	        "DESCRIPTION varchar(150), " +
	        "PATH varchar(250) NOT NULL, " +
	        "SIZE integer, " +
	        "CREATION_DATE datetime, " +
	        "PRIMARY KEY (ID))";

	    Statement stmt = null;
	    try {
	        stmt = con.createStatement();
	        stmt.executeUpdate(createString);
	    } catch (SQLException e) {
	        JDBCUtilities.printSQLException(e);
	    } finally {
	        if (stmt != null) { stmt.close(); }
	    }
	}
	
	public static void createMetadataTable(Connection con, String dbName) throws SQLException {
		
		String createString =
		    	"create table if not exists " + dbName +
		        ".METADATA" +
		        "(MEDIA_ID integer not null, " +
		        "MDATA_ID integer not null, " +
		        "VALUE varchar(150), " +
		        "PRIMARY KEY (MEDIA_ID, MDATA_ID))";

		    Statement stmt = null;
		    try {
		        stmt = con.createStatement();
		        stmt.executeUpdate(createString);
		    } catch (SQLException e) {
		        JDBCUtilities.printSQLException(e);
		    } finally {
		        if (stmt != null) { stmt.close(); }
		    }
		
	}
	
	public static void insertMediaRow(Connection con, MediaRow mediaRow) throws SQLException {
		String queryString = "insert into " + MediaRow.TABLE_NAME +
			            ".MEDIA " +
			            "values(NULL,?,?,?,?,?)";
		//System.out.println(queryString);
		
		java.sql.PreparedStatement insertRow = null;
		try {
	    	insertRow = con.prepareStatement(queryString);
	    	insertRow.setString(1, mediaRow.getMediaType());
	    	insertRow.setString(2, mediaRow.getDescription());
	    	insertRow.setString(3, mediaRow.getRelativePath());
	    	insertRow.setLong(4, mediaRow.getSize());
	    	insertRow.setTimestamp(5, new java.sql.Timestamp(mediaRow.getCreationDate().getTime()));
	    	insertRow.executeUpdate();
	    } catch (SQLException e) {
	    	JDBCUtilities.printSQLException(e);
	    } finally {
	        if (insertRow != null) { insertRow.close(); }
	    }
	}
	
	
	/**
	 * Returns first 10 rows in MEDIA table
	 * @param con
	 * @param dbName
	 * @return ResultSet
	 * @throws SQLException
	 */
	public static ResultSet getMedia(Connection con, String dbName) throws SQLException {
		String query =
		        "select TYPE, DESCRIPTION, PATH " +
		        "from " + dbName + ".MEDIA limit 10";
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = con.createStatement();
	        rs = stmt.executeQuery(query);
	    } catch (SQLException e) {
	    	JDBCUtilities.printSQLException(e);
	    }
				
		return rs;
	}
		
	
}
