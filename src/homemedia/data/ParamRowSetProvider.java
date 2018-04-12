package homemedia.data;

import java.sql.SQLException;

import javax.sql.RowSet;

public abstract class ParamRowSetProvider extends CachedRowSetProvider {

	@Override
	public RowSet execute() throws SQLException {
		prepareRowSet();
		setParams();
		rowSet.execute();
		return rowSet;
	}

	abstract void setParams() throws SQLException;
}
