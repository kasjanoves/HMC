package homemedia.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class PreparedStatementKeyGenProvider extends StatementProvider {

	protected PreparedStatement statement;
	protected Connection conn;
	private int autoIncKey = -1;
	
	@Override
	void prepareStatement() throws SQLException {
		statement = conn.prepareStatement(getQuery(), Statement.RETURN_GENERATED_KEYS);
	}

	@Override
	int execute() throws SQLException {
		ResultSet rs = null;
		try {
			statement.executeUpdate();
			rs = statement.getGeneratedKeys();
	        if (rs.next()) 
	        	autoIncKey = rs.getInt(1);
	        else 
	            throw new SQLException("key generation exception");
		} finally {
	    	if (rs != null) { rs.close(); }
	        if (statement != null) { statement.close(); }
	    }
		return autoIncKey;
	}

}
