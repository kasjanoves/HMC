package homemedia.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;

import javax.sql.RowSet;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.FilteredRowSet;
import javax.sql.rowset.JoinRowSet;

import com.sun.rowset.CachedRowSetImpl;
import com.sun.rowset.FilteredRowSetImpl;
import com.sun.rowset.JoinRowSetImpl;

import homemedia.model.Media;
import homemedia.model.MetadataRow;
import homemedia.model.MetadataRows;
import homemedia.model.MetadataTag;

import java.util.Set;
import java.util.StringJoiner;


//so much boilerplate code here
public class DBTables {
	
	public final static String DBNAME = "HMCATALOG";
	
	public static void createAllTables(Connection conn) throws SQLException {
		createMediaTable(conn);
		createMetadaTypesTable(conn);
		createMetadataTable(conn);
		createTagsTable(conn);
		createMediaTagsTable(conn);
	}
		
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
		        "." + MetadataTag.TABLE_NAME +
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
		    	"create table if not exists " + DBNAME + ".MEDIA_TAGS" +
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
	
	public static int insertMediaRow(Connection con, Media media) throws SQLException {
		
		String queryString = "insert into " + DBNAME +
					"." + Media.TABLE_NAME +
			        " values(NULL,?,?,?,?,?,?)";
		int autoIncKey = -1;
		
		java.sql.PreparedStatement insertRow = null;
		ResultSet rs = null;
		try {
	    	insertRow = con.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS);
	    	insertRow.setString(1, media.getMediaType());
	    	insertRow.setString(2, media.getDescription());
	    	insertRow.setString(3, media.getPath());
	    	insertRow.setString(4, media.getThumbnailPath());
	    	insertRow.setLong(5, media.getSize());
	    	insertRow.setTimestamp(6, new java.sql.Timestamp(media.getCreationDate().getTime()));
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
	    	insertRow.setString(4, mDataTag.getType().name());
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
		    	insertRow.setString(3, mdataRow.getStringValue());
		    	insertRow.setFloat(4, mdataRow.getNumValue());
		    	insertRow.setTimestamp(5, mdataRow.getDateTimeValue());
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
	 * @return ResultSet
	 * @throws SQLException
	 */
	public static RowSet getMedia() throws SQLException {
		
		String queryString =
		        "select * " +
		        "from " + DBNAME + ".MEDIA";
		
		CachedRowSet crs = new CachedRowSetImpl();
		crs.setUsername(JDBCUtilities.getUserName());
	    crs.setPassword(JDBCUtilities.getPassword());
	    crs.setUrl(JDBCUtilities.getUrl());
	    crs.setCommand(queryString);
	    crs.execute();
						
		return crs;
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
	
	public static RowSet getMediaInfo(int mediaID) throws SQLException {
		
		String queryString =
				"select MEDIA.*," + 
				"METADATA_TYPES.DIRECTORY," + 
				"METADATA_TYPES.TAG," + 
				"METADATA.VALUE " + 
				"from "+ DBNAME +".MEDIA " + 
				"left join " + DBNAME + ".METADATA on METADATA.MEDIA_ID=MEDIA.ID " + 
				"left join " + DBNAME + ".METADATA_TYPES on METADATA.MDATA_ID = METADATA_TYPES.ID " + 
				"where MEDIA.ID=" + mediaID;
		
		CachedRowSet crs = new CachedRowSetImpl();
		crs.setUsername(JDBCUtilities.getUserName());
		crs.setPassword(JDBCUtilities.getPassword());
		crs.setUrl(JDBCUtilities.getUrl());
		crs.setCommand(queryString);
		crs.execute();
		
		return crs;
	}
		
	public static RowSet getMediaByCriteria(String descr, Set<Integer> tags) throws SQLException {
		
		//instead using JoinRowSet here
		
		StringJoiner sj = new StringJoiner(",");
		for(Integer tag : tags)
			sj.add(tag.toString());
				
		String mediaQueryString =
		        "select * from " + DBNAME + ".media where DESCRIPTION like '%" + descr.trim() +"%'";
		
		String mtagsQueryString = "select * from " + DBNAME + ".media_tags where " +
				"TAG_ID in ("+sj.toString()+")";
		
		CachedRowSet mediaCrs = new CachedRowSetImpl();
		mediaCrs.setUsername(JDBCUtilities.getUserName());
		mediaCrs.setPassword(JDBCUtilities.getPassword());
		mediaCrs.setUrl(JDBCUtilities.getUrl());
		mediaCrs.setCommand(mediaQueryString);
		mediaCrs.execute();
		
		CachedRowSet mtagsCrs = new CachedRowSetImpl();
		mtagsCrs.setUsername(JDBCUtilities.getUserName());
		mtagsCrs.setPassword(JDBCUtilities.getPassword());
		mtagsCrs.setUrl(JDBCUtilities.getUrl());
		mtagsCrs.setCommand(mtagsQueryString);
		mtagsCrs.execute();
		
		JoinRowSet jrs = new JoinRowSetImpl();
		jrs.addRowSet(mtagsCrs, "ID");
		jrs.addRowSet(mtagsCrs, "MEDIA_ID");
				
		return jrs;
	}
	
	public static RowSet getMediaByMetadataAdv(Map<String, String[]> parMap) throws SQLException {
						
		//use FilteredRowSet here 
		FilteredRowSet frs = null;
		MetadataFilter filter = new MetadataFilter(parMap);
				
		frs = new FilteredRowSetImpl();
		frs.setCommand("select MEDIA.ID," + 
				"MEDIA.TYPE," + 
				"MEDIA.DESCRIPTION," + 
				"MEDIA.PATH," + 
				"MEDIA.THUMB_PATH," + 
				"METADATA.MDATA_ID," + 
				"METADATA.VALUE," + 
				"METADATA.NUM_VALUE," + 
				"METADATA.DATA_VALUE," +
				"METADATA_TYPES.TYPE as MDATA_TYPE from "+ DBNAME +".METADATA, " +
				DBNAME +".MEDIA, " +
				DBNAME +".METADATA_TYPES " +
				"where METADATA.MEDIA_ID = MEDIA.ID " +
				"and METADATA.MDATA_ID = METADATA_TYPES.ID");
		frs.setUsername(JDBCUtilities.getUserName());
		frs.setPassword(JDBCUtilities.getPassword());
		frs.setUrl(JDBCUtilities.getUrl());
		frs.execute();
		frs.setFilter(filter);
	    		
		return frs;
	}
	
	public static RowSet getMediaTags(int mediaID) throws SQLException {
		String queryString =
				"select TAG_ID, NAME, MEDIA_ID "
				+ "FROM "+ DBNAME +".MEDIA_TAGS, "+ DBNAME +".TAGS " + 
				"where MEDIA_ID = "+ mediaID
				+ " and MEDIA_TAGS.TAG_ID=TAGS.ID";
						
		CachedRowSet crs = new CachedRowSetImpl();
		crs.setUsername(JDBCUtilities.getUserName());
		crs.setPassword(JDBCUtilities.getPassword());
		crs.setUrl(JDBCUtilities.getUrl());
		crs.setCommand(queryString);
		crs.execute();
				
		return crs;
	}
	
	public static RowSet getAllTags() throws SQLException {
		
		String queryString = "select ID, NAME from "+ DBNAME +".TAGS ";
						
		CachedRowSet crs = new CachedRowSetImpl();
		crs.setUsername(JDBCUtilities.getUserName());
		crs.setPassword(JDBCUtilities.getPassword());
		crs.setUrl(JDBCUtilities.getUrl());
		crs.setCommand(queryString);
		crs.execute();
				
		return crs;
	}
	
	public static RowSet getMetadataTypes() throws SQLException {
		
		String queryString =
				"select * "	+
				"from "+ DBNAME +"."+MetadataTag.TABLE_NAME +
				" order by DESTINATION";
						
		Statement stmt = null;
		ResultSet rs = null;
		
		CachedRowSet crs = new CachedRowSetImpl();
		crs.setUsername(JDBCUtilities.getUserName());
		crs.setPassword(JDBCUtilities.getPassword());
		crs.setUrl(JDBCUtilities.getUrl());
		crs.setCommand(queryString);
		crs.execute();
				
		return crs;
				
	}
	
	public static RowSet getUnselectedTags(int mediaID) throws SQLException {
		
		String queryString = "select ID, NAME from "+ DBNAME +".TAGS " +
				"where ID not in (select TAG_ID from "+ DBNAME +".MEDIA_TAGS " + 
				"where MEDIA_ID = ?)";
				
		CachedRowSet crs = new CachedRowSetImpl();
		crs.setUsername(JDBCUtilities.getUserName());
		crs.setPassword(JDBCUtilities.getPassword());
		crs.setUrl(JDBCUtilities.getUrl());
		crs.setCommand(queryString);
		crs.setInt(1, mediaID);
		crs.execute();
				
		return crs;
	}
	
	public static RowSet getTagsByIDs(Set<Integer> tags) throws SQLException {
								
		StringJoiner sj = new StringJoiner(",");
		sj.setEmptyValue("null");
		for(Integer tag : tags)
			sj.add(tag.toString());
		
		String queryString = "select ID, NAME from "+ DBNAME +".TAGS "
				+ "where ID in ("+sj.toString()+")";
		
		CachedRowSet crs = new CachedRowSetImpl();
		crs.setUsername(JDBCUtilities.getUserName());
		crs.setPassword(JDBCUtilities.getPassword());
		crs.setUrl(JDBCUtilities.getUrl());
		crs.setCommand(queryString);
		crs.execute();
				
		return crs;
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
	
	public static void deleteUnusedTags(Connection conn) throws SQLException {
		String queryString = "delete from " + DBNAME +
					".TAGS where ID not in "
					+ "(select distinct TAG_ID from " + DBNAME + ".MEDIA_TAGS)";
				
		java.sql.PreparedStatement deleteRow = null;
		try {
	    	deleteRow = conn.prepareStatement(queryString);
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
