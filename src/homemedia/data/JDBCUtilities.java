package homemedia.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCUtilities {
	
	private static String userName= "root";
	private static String password= "root";
	private static String serverName = "localhost";
	private static String portNumber = "3306";
	private static String url = "jdbc:mysql://"+serverName+":"+portNumber;
	
	public JDBCUtilities(String serverName, String portNumber,
						String userName, String password) throws ClassNotFoundException {
		this(userName, password);
		JDBCUtilities.serverName = serverName;
		JDBCUtilities.portNumber = portNumber;
	}
	
	public JDBCUtilities(String userName, String password) throws ClassNotFoundException {
		JDBCUtilities.userName = userName;
		JDBCUtilities.password = password;
		Class.forName("com.mysql.jdbc.Driver");
	}
	
	public static String getUserName() {
		return userName;
	}

	public static String getPassword() {
		return password;
	}

	public static String getServerName() {
		return serverName;
	}

	public static String getPortNumber() {
		return portNumber;
	}

	public static String getUrl() {
		return url;
	}

	public Connection getConnection() throws SQLException {

	    Connection conn = null;
	    Properties connectionProps = new Properties();
	    connectionProps.put("user", JDBCUtilities.userName);
	    connectionProps.put("password", JDBCUtilities.password);
	    
	    conn = DriverManager.getConnection(url, connectionProps);
	    
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
