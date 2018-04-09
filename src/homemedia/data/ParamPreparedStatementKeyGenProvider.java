package homemedia.data;

import java.sql.SQLException;

public abstract class ParamPreparedStatementKeyGenProvider extends PreparedStatementKeyGenProvider {

	@Override
	int execute() throws SQLException {
		setParam();
		return super.execute();
	}
	
	abstract void setParam() throws SQLException;

}
