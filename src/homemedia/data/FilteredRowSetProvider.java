package homemedia.data;

import java.sql.SQLException;

import javax.sql.RowSet;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.FilteredRowSet;
import javax.sql.rowset.Predicate;

import com.sun.rowset.FilteredRowSetImpl;

public abstract class FilteredRowSetProvider extends RowSetProvider {

	protected FilteredRowSet rowSet;

	@Override
	void prepareRowSet() throws SQLException {
		rowSet = new FilteredRowSetImpl();
		rowSet.setUsername(JDBCUtilities.getUserName());
		rowSet.setPassword(JDBCUtilities.getPassword());
		rowSet.setUrl(JDBCUtilities.getUrl());
		rowSet.setCommand(getQuery());
	}

	@Override
	public	RowSet execute() throws SQLException {
		prepareRowSet();
		rowSet.execute();
		rowSet.setFilter(getFilter());
		return rowSet;
	}
	
	abstract Predicate getFilter();

}
