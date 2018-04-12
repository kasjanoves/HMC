package homemedia.data;

import java.sql.SQLException;

public class GetMediaTagsProvider extends ParamRowSetProvider {
	
	protected final static String query="select TAG_ID, NAME, MEDIA_ID " +
			"FROM "+ DBTables.DBNAME +".MEDIA_TAGS, "+ DBTables.DBNAME +".TAGS " + 
			"where MEDIA_ID = ?" + 
			" and MEDIA_TAGS.TAG_ID=TAGS.ID";
	
	private int mediaID;
	
	public GetMediaTagsProvider(int mediaID) {
		this.mediaID=mediaID;
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
