package homemedia.data;

import java.sql.SQLException;

public abstract class ParamStatementProvider extends PreparedStatementProvider {
	
	@Override
	int execute() throws SQLException {
		setParam();
		return statement.executeUpdate();
	}
	
	abstract void setParam();
}
