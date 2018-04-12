package homemedia.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class PreparedStatementProvider extends StatementProvider {
	
	protected PreparedStatement statement;
	protected Connection conn;
	
	public PreparedStatementProvider(Connection conn) {
		this.conn=conn;
	}

	@Override
	void prepareStatement() throws SQLException {
		statement = conn.prepareStatement(getQuery());
	}

	@Override
	public	int execute() throws SQLException {
		return statement.executeUpdate();
	}

}
