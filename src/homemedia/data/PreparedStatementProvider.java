package homemedia.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class PreparedStatementProvider extends StatementProvider {
	
	protected PreparedStatement statement;
	protected Connection conn;

	@Override
	void prepareStatement() throws SQLException {
		statement = conn.prepareStatement(getQuery());
	}

	@Override
	int execute() throws SQLException {
		try {
			return statement.executeUpdate();
		} finally {
	        if (statement != null) { statement.close(); }
	    }	
	}

}
