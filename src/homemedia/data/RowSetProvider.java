package homemedia.data;

import java.sql.SQLException;

import javax.sql.RowSet;

public abstract class RowSetProvider {
	
	abstract String getQuery();
	
	abstract void prepareRowSet() throws SQLException;
	
	abstract RowSet execute() throws SQLException;

}
