package homemedia.data;

import java.sql.SQLException;

public abstract class StatementProvider {
	
	public abstract String getQuery();
	
	abstract void prepareStatement() throws SQLException;
	
	public abstract int execute() throws SQLException;

}
