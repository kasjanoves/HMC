package homemedia.data;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class ParamStatementProvider extends PreparedStatementProvider {
	
	public ParamStatementProvider(Connection conn) {
		super(conn);
	}

	@Override
	public	int execute() throws SQLException {
		setParam();
		return statement.executeUpdate();
	}
	
	abstract void setParam() throws SQLException;
}
