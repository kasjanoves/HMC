package homemedia.data;

import java.sql.SQLException;

import javax.sql.RowSet;
import javax.sql.rowset.CachedRowSet;

import com.sun.rowset.CachedRowSetImpl;

public abstract class CachedRowSetProvider extends RowSetProvider {
	
	protected CachedRowSet rowSet;
	
	@Override
	void prepareRowSet() throws SQLException {
		rowSet = new CachedRowSetImpl();
		rowSet.setUsername(JDBCUtilities.getUserName());
		rowSet.setPassword(JDBCUtilities.getPassword());
		rowSet.setUrl(JDBCUtilities.getUrl());
		rowSet.setCommand(getQuery());
	}

	@Override
	RowSet execute() throws SQLException {
		prepareRowSet();
		rowSet.execute();
		return rowSet;
	}

}
