package com.example.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBTables {
		
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
	    } catch (SQLException e) {
	    	JDBCUtilities.printSQLException(e);
	    } finally {
	        if (stmt != null) { stmt.close(); }
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
		        "select DESCRIPTION, PATH " +
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
