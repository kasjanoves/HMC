package homemedia.data;

import java.sql.SQLException;

public class GetUnselectedTagsProvider extends ParamRowSetProvider {
	
	protected final static String query="select ID, NAME from "+
			DBTables.DBNAME +".TAGS " +
			"where ID not in (select TAG_ID from "+ DBTables.DBNAME +".MEDIA_TAGS " + 
			"where MEDIA_ID = ?)";
	
	private int mediaID;
	
	public GetUnselectedTagsProvider(int mediaID) {
		this.mediaID=mediaID;
	}

	@Override
	void setParams() throws SQLException {
		rowSet.setInt(1, mediaID);
	}

	@Override
	String getQuery() {
		return query;
	}

}
