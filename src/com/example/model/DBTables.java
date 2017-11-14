package com.example.model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;

//so much boilerplate code here
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
	        "THUMB_PATH varchar(250), " +
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
		        "NUM_VALUE numeric(10,2), " +
		        "DATA_VALUE datetime, " +
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
		        "TYPE varchar(10), " +
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
			        " values(NULL,?,?,?,?,?,?)";
		int autoIncKey = -1;
		
		java.sql.PreparedStatement insertRow = null;
		ResultSet rs = null;
		try {
	    	insertRow = con.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS);
	    	insertRow.setString(1, mediaRow.getMediaType());
	    	insertRow.setString(2, mediaRow.getDescription());
	    	insertRow.setString(3, mediaRow.getRelativePath());
	    	insertRow.setString(4, mediaRow.getThumbnailPath());
	    	insertRow.setLong(5, mediaRow.getSize());
	    	insertRow.setTimestamp(6, new java.sql.Timestamp(mediaRow.getCreationDate().getTime()));
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
	
	public static int insertMetadataTagRow(Connection con, MetadataTag mDataTag) throws SQLException {
		String queryString = "select ID " +
		        "from " + DBNAME + "." + MetadataTag.TABLE_NAME +
		        " WHERE DESTINATION = '" + mDataTag.getDestination() + "'" +
		        " AND DIRECTORY = '" + mDataTag.getDirectory() + "'" +
		        " AND TAG = '" + mDataTag.getTag() + "'";
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
				"." + MetadataTag.TABLE_NAME +
		        " values(NULL,?,?,?,?)";
		
		java.sql.PreparedStatement insertRow = null;
		
		try {
	    	insertRow = con.prepareStatement(insertString, Statement.RETURN_GENERATED_KEYS);
	    	insertRow.setString(1, mDataTag.getDestination());
	    	insertRow.setString(2, mDataTag.getDirectory());
	    	insertRow.setString(3, mDataTag.getTag());
	    	insertRow.setString(4, mDataTag.getType());
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
			        " values(?,?,?,?,?)";
				
		java.sql.PreparedStatement insertRow = null;
		try {
	    	insertRow = con.prepareStatement(queryString);
	    	for(MetadataRow mdataRow : mdataValues.getItems()) {
		    	insertRow.setInt(1, mdataValues.getMediaRowID());
		    	insertRow.setInt(2, mdataRow.getRowID());
		    	insertRow.setString(3, "");
		    	insertRow.setFloat(4, 0.0f);
		    	insertRow.setDate(5, null);
		    	if(mdataRow.getTag().getType().equals("string"))
		    		insertRow.setString(3, mdataRow.getValue());
		    	else if(mdataRow.getTag().getType().equals("Num"))
		    		insertRow.setFloat(4, Float.parseFloat(mdataRow.getValue()));
		    	else if(mdataRow.getTag().getType().equals("DateTime")) {
		    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
					java.util.Date date = sdf.parse(mdataRow.getValue(), new ParsePosition(0));
		    		insertRow.setDate(5, date);
		    	}
		    		
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
	 * Returns all rows in MEDIA table
	 * @param con
	 * @return ResultSet
	 * @throws SQLException
	 */
	public static ResultSet getMedia(Connection con) throws SQLException {
		String queryString =
		        "select * " +
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
	
	public static ResultSet getMediaById(Connection con, int id) throws SQLException {
		String queryString =
		        "select * " +
		        "from " + DBNAME + ".MEDIA " +
		        "where ID = " + String.valueOf(id);
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
				"select MEDIA.*," + 
				"METADATA_TYPES.DIRECTORY," + 
				"METADATA_TYPES.TAG," + 
				"METADATA.VALUE " + 
				"from "+ DBNAME +".MEDIA " + 
				"left join " + DBNAME + ".METADATA on METADATA.MEDIA_ID=MEDIA.ID " + 
				"left join " + DBNAME + ".METADATA_TYPES on METADATA.MDATA_ID = METADATA_TYPES.ID " + 
				"where MEDIA.ID=" + String.valueOf(id);
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
		
	public static ResultSet getMediaByCriteria(Connection con, String descr, Set<Integer> tags) throws SQLException {
		
		StringBuilder sb = new StringBuilder();
		if(tags != null) {
			Iterator<Integer> it = tags.iterator();
			while(it.hasNext())
				sb.append(Integer.toString(it.next()) + (it.hasNext() ? "," : ""));
		}
				
		String queryString =
		        "select * " +
				"from " + DBNAME + ".media ";
		if(sb.length()>0)
			queryString = queryString +
				"," + DBNAME + ".media_tags ";
		queryString = queryString +
				"where " +
				"DESCRIPTION like '%" + descr.trim() +"%'";
		if(sb.length()>0)
			queryString = queryString +
				" and media_tags.TAG_ID in ("+sb.toString()+")" +
				" and media.ID = media_tags.MEDIA_ID";
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
	
	public static ResultSet getMediaTags(Connection con, int mediaID) throws SQLException {
		String queryString =
				"select TAG_ID as ID, NAME "
				+ "FROM "+ DBNAME +".MEDIA_TAGS, "+ DBNAME +".TAGS " + 
				"where MEDIA_ID = "+ String.valueOf(mediaID)
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
				"select ID, NAME "
				+ "from "+ DBNAME +".TAGS ";
						
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
				"select ID, NAME from "+ DBNAME +".TAGS " +
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
	
	public static ResultSet getTagsByIDs(Connection con, Set<Integer> tags) throws SQLException {
								
		Statement stmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder();
		Iterator<Integer> it = tags.iterator();
		while(it.hasNext())
			sb.append(Integer.toString(it.next()) + (it.hasNext() ? "," : ""));
		if(sb.length()==0)
			sb.append("null");
		
		String queryString =
				"select ID, NAME "
				+ "from "+ DBNAME +".TAGS "
				+ "where ID in ("+sb.toString()+")";
		
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(queryString);
		} catch (SQLException e) {
	    	JDBCUtilities.printSQLException(e);
	    }
				
		return rs;
	}
	
	public static void deleteMediaTagRow(Connection con, int MediaID, int TagID) throws SQLException {
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
	
	public static void deleteMediaRow(Connection con, int MediaID) throws SQLException {
		String queryString = "delete from " + DBNAME +
					".MEDIA where MEDIA.ID = ?";
				
		java.sql.PreparedStatement deleteRow = null;
		try {
	    	deleteRow = con.prepareStatement(queryString);
	    	deleteRow.setInt(1, MediaID);
		    deleteRow.executeUpdate();
		} catch (SQLException e) {
	    	JDBCUtilities.printSQLException(e);
	    } finally {
	        if (deleteRow != null) { deleteRow.close(); }
	    }
	}
	
	public static void deleteTagRow(Connection con, int tagID) throws SQLException {
		String queryString = "delete from " + DBNAME +
					".TAGS where TAGS.ID = ?";
				
		java.sql.PreparedStatement deleteRow = null;
		try {
	    	deleteRow = con.prepareStatement(queryString);
	    	deleteRow.setInt(1, tagID);
		    deleteRow.executeUpdate();
		} catch (SQLException e) {
	    	JDBCUtilities.printSQLException(e);
	    } finally {
	        if (deleteRow != null) { deleteRow.close(); }
	    }
	}
	
	public static void deleteUnusedTags(Connection con) throws SQLException {
		String queryString = "delete from " + DBNAME +
					".TAGS where ID not in "
					+ "(select distinct TAG_ID from " + DBNAME + ".MEDIA_TAGS)";
				
		java.sql.PreparedStatement deleteRow = null;
		try {
	    	deleteRow = con.prepareStatement(queryString);
	    	deleteRow.executeUpdate();
		} catch (SQLException e) {
	    	JDBCUtilities.printSQLException(e);
	    } finally {
	        if (deleteRow != null) { deleteRow.close(); }
	    }
	}
	
	public static void updateMediaDescription(Connection con, int MediaID, String descr) throws SQLException {
		String queryString = "update " + DBNAME +
					".MEDIA set DESCRIPTION = ? "
					+ "where MEDIA.ID = ?";
				
		java.sql.PreparedStatement deleteRow = null;
		try {
	    	deleteRow = con.prepareStatement(queryString);
	    	deleteRow.setString(1, descr);
	    	deleteRow.setInt(2, MediaID);
		    deleteRow.executeUpdate();
		} catch (SQLException e) {
	    	JDBCUtilities.printSQLException(e);
	    } finally {
	        if (deleteRow != null) { deleteRow.close(); }
	    }
	}
}
