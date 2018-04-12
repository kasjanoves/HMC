package homemedia.data;

import java.sql.SQLException;

import javax.sql.RowSet;

public abstract class RowSetProvider {
	
	public abstract String getQuery();
	
	abstract void prepareRowSet() throws SQLException;
	
	public abstract RowSet execute() throws SQLException;

}
