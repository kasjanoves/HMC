package homemedia.data;

import java.sql.SQLException;

import homemedia.model.Media;

public class GetMediaByDescriptionProvider extends ParamRowSetProvider {
	
	protected final static String query="select * " +
			"from " + DBTables.DBNAME + "." + Media.TABLE_NAME + " DESCRIPTION like '%?%'";
	
	private String description;
	
	public GetMediaByDescriptionProvider(String description) {
		this.description = description;
	}

	@Override
	void setParams() throws SQLException {
		rowSet.setString(1, description);
	}

	@Override
	public	String getQuery() {
		return query;
	}

}
