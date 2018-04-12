package homemedia.data;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class ParamPreparedStatementKeyGenProvider extends PreparedStatementKeyGenProvider {

	public ParamPreparedStatementKeyGenProvider(Connection conn) {
		super(conn);
	}

	@Override
	public	int execute() throws SQLException {
		setParam();
		return super.execute();
	}
	
	abstract void setParam() throws SQLException;

}
