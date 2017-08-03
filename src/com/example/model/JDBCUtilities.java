package com.example.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCUtilities {
	private String userName;
	private String password;
	private String serverName = "localhost";
	private String portNumber = "3306";
	
	public JDBCUtilities(String serverName, String portNumber,
						String userName, String password) {
		this.serverName = serverName;
		this.portNumber = portNumber;
		this.userName = userName;
		this.password = password;
	}
	
	public JDBCUtilities(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}

	public Connection getConnection() throws SQLException, ClassNotFoundException {

	    Connection conn = null;
	    Properties connectionProps = new Properties();
	    connectionProps.put("user", this.userName);
	    connectionProps.put("password", this.password);
	    
	    Class.forName("com.mysql.jdbc.Driver");

	    conn = DriverManager.getConnection(
	                   "jdbc:mysql://" +
	                   this.serverName +
	                   ":" + this.portNumber + "/",
	                   connectionProps);
	    
	    System.out.println("Connected to database");
	    return conn;
	}
	
	public static void printSQLException(SQLException ex) {

	    for (Throwable e : ex) {
	        if (e instanceof SQLException) {
	            if (ignoreSQLException(
	                ((SQLException)e).
	                getSQLState()) == false) {

	                e.printStackTrace(System.err);
	                System.err.println("SQLState: " +
	                    ((SQLException)e).getSQLState());

	                System.err.println("Error Code: " +
	                    ((SQLException)e).getErrorCode());

	                System.err.println("Message: " + e.getMessage());

	                Throwable t = ex.getCause();
	                while(t != null) {
	                    System.out.println("Cause: " + t);
	                    t = t.getCause();
	                }
	            }
	        }
	    }
	}

	private static boolean ignoreSQLException(String sqlState) {
		if (sqlState == null) {
	        System.out.println("The SQL state is not defined!");
	        return false;
	    }

	    // X0Y32: Jar file already exists in schema
	    if (sqlState.equalsIgnoreCase("X0Y32"))
	        return true;

	    // 42Y55: Table already exists in schema
	    if (sqlState.equalsIgnoreCase("42Y55"))
	        return true;
		return false;
	}
	
	public void closeConnection(Connection conn){
		try {
			conn.close();
			conn = null;
		} catch (SQLException e) {
			printSQLException(e);
		}
	}
}
