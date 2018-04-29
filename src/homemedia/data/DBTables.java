package homemedia.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import homemedia.model.MetadataTag;

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
	
}	