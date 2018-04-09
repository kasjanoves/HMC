package homemedia.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

public class DBTables {
	
	public final static String DBNAME = "HMCATALOG";
		
	private static void createTableTemplate(Connection conn, String query) throws SQLException {
		
		Statement stmt = null;
	    try {
	        stmt = conn.createStatement();
	        stmt.executeUpdate(query);
	    } finally {
	        if (stmt != null) { stmt.close(); }
	    }
	}
	
	private static RowSet getRowsTemplate(String query) throws SQLException {
		
		CachedRowSet crs = new CachedRowSetImpl();
		crs.setUsername(JDBCUtilities.getUserName());
	    crs.setPassword(JDBCUtilities.getPassword());
	    crs.setUrl(JDBCUtilities.getUrl());
	    crs.setCommand(query);
	    crs.execute();
						
		return crs;
	}
	
	public static void createAllTables(Connection conn) throws SQLException {
		createMediaTable(conn);
		createMetadaTypesTable(conn);
		createMetadataTable(conn);
		createTagsTable(conn);
		createMediaTagsTable(conn);
	}
		
	public static void createMediaTable(Connection conn) throws SQLException {
		
	    String query =
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

	    createTableTemplate(conn, query);
	}
	
	public static void createMetadataTable(Connection conn) throws SQLException {
		
		String query =
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
				
		createTableTemplate(conn, query);
		
	}
	
	public static void createMetadaTypesTable(Connection conn) throws SQLException {
		
		String query =
		    	"create table if not exists " + DBNAME +
		        "." + MetadataTag.TABLE_NAME +
		        "(ID integer AUTO_INCREMENT, " +
		        "DESTINATION varchar(15), " +
		        "DIRECTORY varchar(50), " +
		        "TAG varchar(50), " +
		        "TYPE varchar(10), " +
		        "PRIMARY KEY (ID))";

		createTableTemplate(conn, query);
		
	}
	
	public static void createTagsTable(Connection conn) throws SQLException {
		
		String query =
		    	"create table if not exists " + DBNAME +
		        ".TAGS" +
		        "(ID integer AUTO_INCREMENT, " +
		        "NAME varchar(35) not null, " +
		        "PRIMARY KEY (ID))";

		createTableTemplate(conn, query);
	}
	
	public static void createMediaTagsTable(Connection conn) throws SQLException {
		
		String query =
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
				
		createTableTemplate(conn, query);
	}
	
	public static int insertMediaRow(Connection conn, Media media) throws SQLException {
		
		int autoIncKey = -1;
		
		java.sql.PreparedStatement insertRow = null;
		ResultSet rs = null;
		try {
	    	insertRow = conn.prepareStatement(insertMediaQuery, Statement.RETURN_GENERATED_KEYS);
	    	insertRow.setString(1, media.getMediaType());
	    	insertRow.setString(2, media.getDescription());
	    	insertRow.setString(3, media.getPath());
	    	insertRow.setString(4, media.getThumbnailPath());
	    	insertRow.setLong(5, media.getSize());
	    	insertRow.setTimestamp(6, new java.sql.Timestamp(media.getCreationDate().getTime()));
	    	insertRow.executeUpdate();
	    	rs = insertRow.getGeneratedKeys();
	        if (rs.next()) 
	        	autoIncKey = rs.getInt(1);
	        else 
	            throw new SQLException("key generation exception");
	    } finally {
	    	if (rs != null) { rs.close(); }
	        if (insertRow != null) { insertRow.close(); }
	    }
		return autoIncKey;
	}
	
	public static int insertMetadataTagRow(Connection conn, MetadataTag mDataTag) throws SQLException {
		
		int autoIncKey = -1;
		
		CachedRowSet crs = new CachedRowSetImpl();
		crs.setUsername(JDBCUtilities.getUserName());
		crs.setPassword(JDBCUtilities.getPassword());
		crs.setUrl(JDBCUtilities.getUrl());
		crs.setCommand(getMetadataTagQuery);
		crs.setString(1, mDataTag.getDestination());
		crs.setString(2, mDataTag.getDirectory());
		crs.setString(3, mDataTag.getTag());
		crs.execute();
		
		try {
			if(crs.next()) {
	        	autoIncKey = crs.getInt("ID");
	        	return autoIncKey;	//row already exists, returns ID
	        }	
	    } finally {	
	    	if (crs != null) { crs.close(); }
	    }
				
		java.sql.PreparedStatement insertRow = null;
		ResultSet rs = null;
		try {
	    	insertRow = conn.prepareStatement(insertMetadataTagQuery, Statement.RETURN_GENERATED_KEYS);
	    	insertRow.setString(1, mDataTag.getDestination());
	    	insertRow.setString(2, mDataTag.getDirectory());
	    	insertRow.setString(3, mDataTag.getTag());
	    	insertRow.setString(4, mDataTag.getType().name());
	    	insertRow.executeUpdate();
	    	rs = insertRow.getGeneratedKeys();
	        if (rs.next()) 
	        	autoIncKey = rs.getInt(1);
	        else 
	            throw new SQLException("key generation exception");
	       
	    } finally {
	    	if (rs != null) { rs.close(); }
	        if (insertRow != null) { insertRow.close(); }
	    }
		return autoIncKey;
	}
	
	public static void insertMetadataRows(Connection conn, MetadataRows mdataValues) throws SQLException {
		
		//batch update
		conn.setAutoCommit(false);
		java.sql.PreparedStatement insertRows = null;
		try {
	    	insertRows = conn.prepareStatement(insertMetadataRowsQuery);
	    	for(MetadataRow mdataRow : mdataValues.getItems()) {
		    	insertRows.setInt(1, mdataValues.getMediaRowID());
		    	insertRows.setInt(2, mdataRow.getRowID());
		    	insertRows.setString(3, mdataRow.getStringValue());
		    	insertRows.setFloat(4, mdataRow.getNumValue());
		    	insertRows.setTimestamp(5, mdataRow.getDateTimeValue());
		    	insertRows.addBatch();
	    	}
	    	insertRows.executeBatch();
	    	conn.commit();
	    	conn.setAutoCommit(true);
	    } finally {
	        if (insertRows != null) { insertRows.close(); }
	    }
	}
	
	public static int insertTagRow(Connection con, String tag) throws SQLException {
				
		int autoIncKey = -1;
		
		java.sql.PreparedStatement insertRow = null;
		ResultSet rs = null;
		try {
	    	insertRow = con.prepareStatement(insertTagQuery, Statement.RETURN_GENERATED_KEYS);
	    	insertRow.setString(1, tag);
	    	insertRow.executeUpdate();
	    	rs = insertRow.getGeneratedKeys();
	        if (rs.next()) 
	        	autoIncKey = rs.getInt(1);
	        else 
	        	throw new SQLException("key generation exception");
	        
	    } finally {
	    	if (rs != null) { rs.close(); }
	        if (insertRow != null) { insertRow.close(); }
	    }
		
		return autoIncKey;
	}
	
	public static void insertMediaTagRow(Connection con, int MediaID, int TagID) throws SQLException {
						
		java.sql.PreparedStatement insertRow = null;
		try {
	    	insertRow = con.prepareStatement(insertMediaTagQuery);
	    	insertRow.setInt(1, MediaID);
		    insertRow.setInt(2, TagID);
		    insertRow.executeUpdate();
		} finally {
	        if (insertRow != null) { insertRow.close(); }
	    }
	}
	
	/**
	 * Returns all rows of MEDIA table
	 * @return RowSet
	 * @throws SQLException
	 */
	public static RowSet getMedia() throws SQLException {
								
		return getRowsTemplate(getAllMediaQuery);
	}
	
	public static RowSet getMediaById(int mediaID) throws SQLException {
		
		CachedRowSet crs = new CachedRowSetImpl();
		crs.setUsername(JDBCUtilities.getUserName());
	    crs.setPassword(JDBCUtilities.getPassword());
	    crs.setUrl(JDBCUtilities.getUrl());
	    crs.setCommand(getMediaByIdQuery);
	    crs.setInt(1, mediaID);
	    crs.execute();
						
		return crs;
	}
	
	public static RowSet getMediaInfo(int mediaID) throws SQLException {
		
		CachedRowSet crs = new CachedRowSetImpl();
		crs.setUsername(JDBCUtilities.getUserName());
		crs.setPassword(JDBCUtilities.getPassword());
		crs.setUrl(JDBCUtilities.getUrl());
		crs.setCommand(getMediaInfoQuery);
		crs.setInt(1, mediaID);
		crs.execute();
		
		return crs;
	}
		
	public static RowSet getMediaByCriteria(String descr, Set<Integer> tags) throws SQLException {
		
		//using JoinRowSet
		StringJoiner sj = new StringJoiner(",");
		for(Integer tag : tags)
			sj.add(tag.toString());
				
		CachedRowSet mediaCrs = new CachedRowSetImpl();
		mediaCrs.setUsername(JDBCUtilities.getUserName());
		mediaCrs.setPassword(JDBCUtilities.getPassword());
		mediaCrs.setUrl(JDBCUtilities.getUrl());
		mediaCrs.setCommand(getMediaByDescriptionQuery);
		mediaCrs.setString(1, descr);
		mediaCrs.execute();
		
		CachedRowSet mtagsCrs = new CachedRowSetImpl();
		mtagsCrs.setUsername(JDBCUtilities.getUserName());
		mtagsCrs.setPassword(JDBCUtilities.getPassword());
		mtagsCrs.setUrl(JDBCUtilities.getUrl());
		mtagsCrs.setCommand(getMediaTagsByIDsQuery);
		mtagsCrs.setString(1, sj.toString());
		mtagsCrs.execute();
		
		JoinRowSet jrs = new JoinRowSetImpl();
		jrs.addRowSet(mediaCrs, "ID");
		jrs.addRowSet(mtagsCrs, "MEDIA_ID");
				
		return jrs;
	}
	
	public static RowSet getMediaByMetadataAdv(Map<String, String[]> parMap) throws SQLException {
						
		//FilteredRowSet 
		MetadataFilter filter = new MetadataFilter(parMap);
				
		FilteredRowSet frs = new FilteredRowSetImpl();
		frs.setCommand(getMediaByMetadataAdvQuery);
		frs.setUsername(JDBCUtilities.getUserName());
		frs.setPassword(JDBCUtilities.getPassword());
		frs.setUrl(JDBCUtilities.getUrl());
		frs.execute();
		frs.setFilter(filter);
	    		
		return frs;
	}
	
	public static RowSet getMediaTags(int mediaID) throws SQLException {
								
		CachedRowSet crs = new CachedRowSetImpl();
		crs.setUsername(JDBCUtilities.getUserName());
		crs.setPassword(JDBCUtilities.getPassword());
		crs.setUrl(JDBCUtilities.getUrl());
		crs.setCommand(getMediaTagsQuery);
		crs.setInt(1, mediaID);
		crs.execute();
				
		return crs;
	}
	
	public static RowSet getAllTags() throws SQLException {
						
		return getRowsTemplate(getAllTagsQuery);
	}
	
	public static RowSet getMetadataTypes() throws SQLException {
						
		return getRowsTemplate(getMetadataTagsQuery);
				
	}
	
	public static RowSet getUnselectedTags(int mediaID) throws SQLException {
		
		CachedRowSet crs = new CachedRowSetImpl();
		crs.setUsername(JDBCUtilities.getUserName());
		crs.setPassword(JDBCUtilities.getPassword());
		crs.setUrl(JDBCUtilities.getUrl());
		crs.setCommand(getUnselectedTagsQuery);
		crs.setInt(1, mediaID);
		crs.execute();
				
		return crs;
	}
	
	public static RowSet getTagsByIDs(Set<Integer> tags) throws SQLException {
								
		StringJoiner sj = new StringJoiner(",");
		sj.setEmptyValue("null");
		for(Integer tag : tags)
			sj.add(tag.toString());
				
		CachedRowSet crs = new CachedRowSetImpl();
		crs.setUsername(JDBCUtilities.getUserName());
		crs.setPassword(JDBCUtilities.getPassword());
		crs.setUrl(JDBCUtilities.getUrl());
		crs.setCommand(getTagsByIDsQuery);
		crs.setString(1, sj.toString());
		crs.execute();
				
		return crs;
	}
	
	public static void deleteMediaTagRow(Connection conn, int MediaID, int TagID) throws SQLException {
		
		java.sql.PreparedStatement deleteRow = null;
		try {
	    	deleteRow = conn.prepareStatement(deleteMediaTagQuery);
	    	deleteRow.setInt(1, MediaID);
		    deleteRow.setInt(2, TagID);
		    deleteRow.executeUpdate();
		} finally {
	        if (deleteRow != null) { deleteRow.close(); }
	    }
	}
	
	public static void deleteMediaRow(Connection conn, int MediaID) throws SQLException {
		
		java.sql.PreparedStatement deleteRow = null;
		try {
	    	deleteRow = conn.prepareStatement(deleteMediaQuery);
	    	deleteRow.setInt(1, MediaID);
		    deleteRow.executeUpdate();
		} finally {
	        if (deleteRow != null) { deleteRow.close(); }
	    }
	}
	
	public static void deleteTagRow(Connection con, int tagID) throws SQLException {
		
		java.sql.PreparedStatement deleteRow = null;
		try {
	    	deleteRow = con.prepareStatement(deleteTagQuery);
	    	deleteRow.setInt(1, tagID);
		    deleteRow.executeUpdate();
		} finally {
	        if (deleteRow != null) { deleteRow.close(); }
	    }
	}
	
	public static void deleteUnusedTags(Connection conn) throws SQLException {
								
		java.sql.PreparedStatement deleteRow = null;
		try {
	    	deleteRow = conn.prepareStatement(deleteUnusedTagsQuery);
	    	deleteRow.executeUpdate();
		} finally {
	        if (deleteRow != null) { deleteRow.close(); }
	    }
	}
	
	public static void updateMediaDescription(Connection conn, int MediaID, String descr) throws SQLException {
						
		java.sql.PreparedStatement deleteRow = null;
		try {
	    	deleteRow = conn.prepareStatement(updateMediaDescriptionQuery);
	    	deleteRow.setString(1, descr);
	    	deleteRow.setInt(2, MediaID);
		    deleteRow.executeUpdate();
		} catch (SQLException e) {
	    	JDBCUtilities.printSQLException(e);
	    } finally {
	        if (deleteRow != null) { deleteRow.close(); }
	    }
	}

	private static final String insertMediaQuery = "insert into " + DBNAME +
						"." + Media.TABLE_NAME +
						" values(NULL,?,?,?,?,?,?)";
	
	private static final String getMetadataTagQuery = "select ID " +
				        "from " + DBNAME + "." + MetadataTag.TABLE_NAME +
				        " WHERE DESTINATION = ?" +
				        " AND DIRECTORY = ?" +
				        " AND TAG = ?";
	
	private static final String insertMetadataTagQuery = "insert into " + DBNAME +
						"." + MetadataTag.TABLE_NAME +
				        " values(NULL,?,?,?,?)";
	
	private static final String insertMetadataRowsQuery = "insert into " + DBNAME +
						"." + MetadataRow.TABLE_NAME +
				        " values(?,?,?,?,?)";
	
	private static final String insertTagQuery = "insert into " + DBNAME +
						".TAGS" + 
				        " values(NULL,?)";
	
	private static final String insertMediaTagQuery = "insert into " + DBNAME +
						".MEDIA_TAGS" +
				        " values(?,?)";
	
	private static final String getAllMediaQuery = "select * " +
						"from " + DBNAME + "." + Media.TABLE_NAME;
	
	private static final String getMediaByIdQuery = getAllMediaQuery + " where ID = ?";
	
	private static final String getMediaByDescriptionQuery = getAllMediaQuery + 
						" where DESCRIPTION like '%?%'";
	
	private static final String getMediaInfoQuery =
						"select MEDIA.*," + 
						"METADATA_TYPES.DIRECTORY," + 
						"METADATA_TYPES.TAG," + 
						"METADATA.VALUE " + 
						"from "+ DBNAME +".MEDIA " + 
						"left join " + DBNAME + ".METADATA on METADATA.MEDIA_ID=MEDIA.ID " + 
						"left join " + DBNAME + ".METADATA_TYPES on METADATA.MDATA_ID = METADATA_TYPES.ID " + 
						"where MEDIA.ID = ?";
		
	private static final String getMediaTagsByIDsQuery = "select * from " + DBNAME + ".MEDIA_TAGS where " +
						"TAG_ID in (?)";
	
	private static final String getMediaByMetadataAdvQuery =
						"select MEDIA.ID," + 
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
						"and METADATA.MDATA_ID = METADATA_TYPES.ID";
	
	private static final String getMediaTagsQuery =
						"select TAG_ID, NAME, MEDIA_ID " +
						"FROM "+ DBNAME +".MEDIA_TAGS, "+ DBNAME +".TAGS " + 
						"where MEDIA_ID = ?" + 
						" and MEDIA_TAGS.TAG_ID=TAGS.ID";
	
	private static final String getAllTagsQuery = "select ID, NAME from "+ DBNAME +".TAGS ";
	
	private static final String getMetadataTagsQuery =
						"select * "	+
						"from "+ DBNAME +"."+MetadataTag.TABLE_NAME +
						" order by DESTINATION";
	
	private static final String getUnselectedTagsQuery = "select ID, NAME from "+ DBNAME +".TAGS " +
						"where ID not in (select TAG_ID from "+ DBNAME +".MEDIA_TAGS " + 
						"where MEDIA_ID = ?)";
	
	private static final String getTagsByIDsQuery = "select ID, NAME from "+ DBNAME +".TAGS "	+ 
						"where ID in (?)";
	
	private static final String deleteMediaTagQuery = "delete from " + DBNAME +
						".MEDIA_TAGS" +
				        " where MEDIA_TAGS.MEDIA_ID = ? and MEDIA_TAGS.TAG_ID = ?";
	
	private static final String deleteMediaQuery = "delete from " + DBNAME +
						".MEDIA where MEDIA.ID = ?";
	
	private static final String deleteTagQuery = "delete from " + DBNAME +
						".TAGS where TAGS.ID = ?";
	
	private static final String deleteUnusedTagsQuery = "delete from " + DBNAME +
						".TAGS where ID not in " +
						"(select distinct TAG_ID from " + DBNAME + ".MEDIA_TAGS)";
	
	private static final String updateMediaDescriptionQuery = "update " + DBNAME +
						".MEDIA set DESCRIPTION = ? " +
						"where MEDIA.ID = ?";
}
