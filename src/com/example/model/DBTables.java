package com.example.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map.Entry;

public class DBTables {
	
	public final static String DBNAME = "HMCATALOG";
		
	public static void createMediaTable(Connection con) throws SQLException {
	    String createString =
	    	"create table if not exists " + DBNAME +
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
	
	public static void createMetadataTable(Connection con) throws SQLException {
		
		String createString =
		    	"create table if not exists " + DBNAME +
		        ".METADATA" +
		        "(MEDIA_ID integer not null, " +
		        "MDATA_ID integer not null, " +
		        "VALUE varchar(150), " +
		        "PRIMARY KEY (MEDIA_ID, MDATA_ID)," + 
		        "FOREIGN KEY (MEDIA_ID)" + 
		        	"REFERENCES "+ DBNAME +".MEDIA(ID)" +
		        	"ON DELETE CASCADE," + 
		        "FOREIGN KEY (MDATA_ID)" + 
		        	"REFERENCES "+ DBNAME +".METADATA_TYPES(ID)" +
		        	"ON DELETE CASCADE)";
		//System.out.println(createString);
		
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
	
	public static void createMetadaTypesTable(Connection con) throws SQLException {
		
		String createString =
		    	"create table if not exists " + DBNAME +
		        ".METADATA_TYPES" +
		        "(ID integer AUTO_INCREMENT, " +
		        "DESTINATION varchar(15), " +
		        "DIRECTORY varchar(50), " +
		        "TAG varchar(50), " +
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
	
	public static void createTagsTable(Connection con) throws SQLException {
		
		String createString =
		    	"create table if not exists " + DBNAME +
		        ".TAGS" +
		        "(ID integer AUTO_INCREMENT, " +
		        "NAME varchar(35) not null, " +
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
	
	public static void createMediaTagsTable(Connection con) throws SQLException {
		
		String createString =
		    	"create table if not exists " + DBNAME +
		        ".MEDIA_TAGS" +
		        "(MEDIA_ID integer not null, " +
		        "TAG_ID integer not null, " +
		        "PRIMARY KEY (MEDIA_ID, TAG_ID)," + 
		        "FOREIGN KEY (MEDIA_ID)" + 
		        	"REFERENCES "+ DBNAME +".MEDIA(ID)" +
		        	"ON DELETE CASCADE," + 
		        "FOREIGN KEY (TAG_ID)" + 
		        	"REFERENCES "+ DBNAME +".TAGS(ID)" +
		        	"ON DELETE CASCADE)";
		//System.out.println(createString);
		
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
	
	public static int insertMediaRow(Connection con, MediaRow mediaRow) throws SQLException {
		String queryString = "insert into " + DBNAME +
					"." + MediaRow.TABLE_NAME +
			        " values(NULL,?,?,?,?,?)";
		int autoIncKey = -1;
		
		java.sql.PreparedStatement insertRow = null;
		ResultSet rs = null;
		try {
	    	insertRow = con.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS);
	    	insertRow.setString(1, mediaRow.getMediaType());
	    	insertRow.setString(2, mediaRow.getDescription());
	    	insertRow.setString(3, mediaRow.getRelativePath());
	    	insertRow.setLong(4, mediaRow.getSize());
	    	insertRow.setTimestamp(5, new java.sql.Timestamp(mediaRow.getCreationDate().getTime()));
	    	insertRow.executeUpdate();
	    	rs = insertRow.getGeneratedKeys();
	        if (rs.next()) {
	        	autoIncKey = rs.getInt(1);
	        } else {
	            // throw an exception from here
	        }
	    } catch (SQLException e) {
	    	JDBCUtilities.printSQLException(e);
	    } finally {
	    	if (rs != null) { rs.close(); }
	        if (insertRow != null) { insertRow.close(); }
	    }
		return autoIncKey;
	}
	
	public static int insertMetadataTagRow(Connection con, MetadataTagRow mDataRow) throws SQLException {
		String queryString = "select ID " +
		        "from " + DBNAME + "." + MetadataTagRow.TABLE_NAME +
		        " WHERE DESTINATION = '" + mDataRow.getDestination() + "'" +
		        " AND DIRECTORY = '" + mDataRow.getDirectory() + "'" +
		        " AND TAG = '" + mDataRow.getTag() + "'";
		//System.out.println(queryString);
			
		int autoIncKey = -1;
		
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = con.createStatement();
	        rs = stmt.executeQuery(queryString);
	        if(rs.next()) {
	        	autoIncKey = rs.getInt("ID");
	        	return autoIncKey;	//row already exists, returns its ID
	        }	
	    } catch (SQLException e) {
	    	JDBCUtilities.printSQLException(e);
	    	return autoIncKey;
	    } finally {	
	    	if (rs != null) { rs.close(); }
	    	if (stmt != null) { stmt.close(); }
	    }
		
		String insertString = "insert into " + DBNAME +
				"." + MetadataTagRow.TABLE_NAME +
		        " values(NULL,?,?,?)";
		
		java.sql.PreparedStatement insertRow = null;
		
		try {
	    	insertRow = con.prepareStatement(insertString, Statement.RETURN_GENERATED_KEYS);
	    	insertRow.setString(1, mDataRow.getDestination());
	    	insertRow.setString(2, mDataRow.getDirectory());
	    	insertRow.setString(3, mDataRow.getTag());
	    	insertRow.executeUpdate();
	    	rs = insertRow.getGeneratedKeys();
	        if (rs.next()) {
	        	autoIncKey = rs.getInt(1);
	        } else {
	            // throw an exception from here
	        }
	    } catch (SQLException e) {
	    	JDBCUtilities.printSQLException(e);
	    } finally {
	    	if (rs != null) { rs.close(); }
	        if (insertRow != null) { insertRow.close(); }
	    }
		return autoIncKey;
	}
	
	public static void insertMetadataRows(Connection con, MetadataRows mdataValues) throws SQLException {
		String queryString = "insert into " + DBNAME +
					".METADATA" +
			        " values(?,?,?)";
				
		java.sql.PreparedStatement insertRow = null;
		try {
	    	insertRow = con.prepareStatement(queryString);
	    	for(Entry<Integer, String> entry : mdataValues.getItems()) {
		    	insertRow.setInt(1, mdataValues.getMediaRowID());
		    	insertRow.setInt(2, entry.getKey());
		    	insertRow.setString(3, entry.getValue());
		    	insertRow.executeUpdate();
	    	}
	    } catch (SQLException e) {
	    	JDBCUtilities.printSQLException(e);
	    } finally {
	        if (insertRow != null) { insertRow.close(); }
	    }
	}
	
	public static int insertTagsRow(Connection con, String name) throws SQLException {
		String queryString = "insert into " + DBNAME +
					".TAGS" + 
			        " values(NULL,?)";
		int autoIncKey = -1;
		
		java.sql.PreparedStatement insertRow = null;
		ResultSet rs = null;
		try {
	    	insertRow = con.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS);
	    	insertRow.setString(1, name);
	    	insertRow.executeUpdate();
	    	rs = insertRow.getGeneratedKeys();
	        if (rs.next()) {
	        	autoIncKey = rs.getInt(1);
	        } else {
	            // throw an exception from here
	        }
	    } catch (SQLException e) {
	    	JDBCUtilities.printSQLException(e);
	    } finally {
	    	if (rs != null) { rs.close(); }
	        if (insertRow != null) { insertRow.close(); }
	    }
		return autoIncKey;
	}
	
	public static void insertMediaTagRow(Connection con, int MediaID, int TagID) throws SQLException {
		String queryString = "insert into " + DBNAME +
					".MEDIA_TAGS" +
			        " values(?,?)";
				
		java.sql.PreparedStatement insertRow = null;
		try {
	    	insertRow = con.prepareStatement(queryString);
	    	insertRow.setInt(1, MediaID);
		    insertRow.setInt(2, TagID);
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
	public static ResultSet getMedia(Connection con) throws SQLException {
		String queryString =
		        "select ID, TYPE, DESCRIPTION, PATH " +
		        "from " + DBNAME + ".MEDIA";
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = con.createStatement();
	        rs = stmt.executeQuery(queryString);
	    } catch (SQLException e) {
	    	JDBCUtilities.printSQLException(e);
	    }
				
		return rs;
	}
	
	public static ResultSet getMediaInfo(Connection con, int id) throws SQLException {
		String queryString =
				"SELECT MEDIA.*," + 
				"metadata_types.DIRECTORY," + 
				"metadata_types.TAG," + 
				"metadata.VALUE " + 
				"FROM "+ DBNAME +".MEDIA " + 
				"left join " + DBNAME + ".metadata on metadata.MEDIA_ID=media.ID " + 
				"left join " + DBNAME + ".metadata_types on metadata.MDATA_ID = metadata_types.ID " + 
				"where media.ID=" + String.valueOf(id);
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = con.createStatement();
	        rs = stmt.executeQuery(queryString);
	    } catch (SQLException e) {
	    	JDBCUtilities.printSQLException(e);
	    }
				
		return rs;
	}
		
	public static ResultSet getMediaByDescription(Connection con, String descr) throws SQLException {
		String queryString =
		        "select ID, TYPE, DESCRIPTION, PATH " +
		        "from " + DBNAME + ".MEDIA "+
		        "where DESCRIPTION like '%" + descr +"%'";
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = con.createStatement();
	        rs = stmt.executeQuery(queryString);
	    } catch (SQLException e) {
	    	JDBCUtilities.printSQLException(e);
	    }
				
		return rs;
	}
	
	public static ResultSet getMediaTags(Connection con, int id) throws SQLException {
		String queryString =
				"SELECT TAG_ID, NAME "
				+ "FROM "+ DBNAME +".MEDIA_TAGS, "+ DBNAME +".TAGS " + 
				"where MEDIA_ID = "+ String.valueOf(id)
				+ " and MEDIA_TAGS.TAG_ID=TAGS.ID";
						
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = con.createStatement();
	        rs = stmt.executeQuery(queryString);
	    } catch (SQLException e) {
	    	JDBCUtilities.printSQLException(e);
	    }
				
		return rs;
	}
	
	public static ResultSet getAllTags(Connection con) throws SQLException {
		String queryString =
				"SELECT ID, NAME "
				+ "FROM "+ DBNAME +".TAGS ";
						
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = con.createStatement();
	        rs = stmt.executeQuery(queryString);
	    } catch (SQLException e) {
	    	JDBCUtilities.printSQLException(e);
	    }
				
		return rs;
	}
	
	public static ResultSet getUnselectedTags(Connection con, int MediaID) throws SQLException {
		String queryString =
				"SELECT ID, NAME FROM "+ DBNAME +".TAGS " +
				"where ID not in (select TAG_ID from "+ DBNAME +".MEDIA_TAGS " + 
				"where MEDIA_ID = ?)";
										
		java.sql.PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = con.prepareStatement(queryString);
			stmt.setInt(1, MediaID);
	        rs = stmt.executeQuery();
	    } catch (SQLException e) {
	    	JDBCUtilities.printSQLException(e);
	    }
				
		return rs;
	}
	
	public static void removeMediaTagRow(Connection con, int MediaID, int TagID) throws SQLException {
		String queryString = "delete from " + DBNAME +
					".MEDIA_TAGS" +
			        " where MEDIA_TAGS.MEDIA_ID = ? and MEDIA_TAGS.TAG_ID = ?";
				
		java.sql.PreparedStatement deleteRow = null;
		try {
	    	deleteRow = con.prepareStatement(queryString);
	    	deleteRow.setInt(1, MediaID);
		    deleteRow.setInt(2, TagID);
		    deleteRow.executeUpdate();
		} catch (SQLException e) {
	    	JDBCUtilities.printSQLException(e);
	    } finally {
	        if (deleteRow != null) { deleteRow.close(); }
	    }
	}
}
