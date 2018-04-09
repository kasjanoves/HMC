package homemedia.data;

import java.sql.SQLException;

public abstract class StatementProvider {
	
	abstract String getQuery();
	
	abstract void prepareStatement() throws SQLException;
	
	abstract int execute() throws SQLException;

}
