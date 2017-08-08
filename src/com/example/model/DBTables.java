package com.example.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

public class DBTables {
		
	public static void createMediaTable(Connection con, String dbName) throws SQLException {
	    String createString =
	    	"create table if not exists " + dbName +
	        ".MEDIA" +
	        "(ID integer AUTO_INCREMENT, " +
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
	
	public static void insertMediaRow(Connection con, String dbName, 
			String description, String path) throws SQLException {
		String queryString = "insert into " + dbName +
			            ".MEDIA " +
			            "values(NULL,?,?,?,?)";
		System.out.println(queryString);
		
		java.sql.PreparedStatement insertRow = null;
		Calendar cal = Calendar.getInstance();
	    try {
	    	insertRow = con.prepareStatement(queryString);
	    	insertRow.setString(1, description);
	    	insertRow.setString(2, path);
	    	insertRow.setInt(3, 0);
	    	//insertRow.setDate(4, new java.sql.Date(cal.getTime().getTime()));
	    	insertRow.setTimestamp(4, new java.sql.Timestamp(cal.getTime().getTime()));
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
	
	public static String ProcessSQlString(String str) {
		return str.replace('\\', '\\');
	}
	
}
