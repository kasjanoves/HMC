package homemedia.data;

import java.sql.SQLException;

import homemedia.model.Media;

public class GetMediaByIdProvider extends ParamRowSetProvider {

	protected final static String query="select * " +
			"from " + DBTables.DBNAME + "." + Media.TABLE_NAME + " where ID = ?";
	
	private int mediaID;
	
	public GetMediaByIdProvider(int mediaID) {
		this.mediaID = mediaID;
	}
	
	@Override 
	void setParams() throws SQLException {
		rowSet.setInt(1, mediaID);
	}

	@Override
	public	String getQuery() {
		return query;
	}

}
