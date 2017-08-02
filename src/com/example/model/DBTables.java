package com.example.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBTables {
	/*private Connection con = null;	
	private String dbName;
		
	public DBTables(Connection con, String dbName) {
		this.con = con;
		this.dbName = dbName;
	}*/
	private static int MediaTableCount = 0;
	
	public static void createMediaTable(Connection con, String dbName) throws SQLException {
	    String createString =
	    	"create table if not exists " + dbName +
	        ".MEDIA" +
	        "(ID integer AUTO_INCREMENT, " +
	        "DESCRIPTION varchar(100), " +
	        "PATH varchar(150) NOT NULL, " +
	        "SIZE integer, " +
	        "DATE datetime, " +
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
	
	public static void insertMediaRow(Connection con, String dbName, 
			String description, String path) throws SQLException {
		String query = "insert into " + dbName +
			            ".MEDIA " +
			            "values(NULL, '"+ description +
			            "', '" + path +"',0,'1000-01-01 00:00:00')";
		System.out.println(query);
		
	    Statement stmt = null;
	    try {
	        stmt = con.createStatement();
	        stmt.executeUpdate(query);
	        MediaTableCount++;        
	    } catch (SQLException e) {
	    	JDBCUtilities.printSQLException(e);
	    } finally {
	        if (stmt != null) { stmt.close(); }
	    }
	}
	
}
