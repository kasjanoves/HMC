package homemedia.data;

import java.sql.SQLException;

public class GetMediaInfoProvider extends ParamRowSetProvider {
	
	protected final static String query="select MEDIA.*," + 
			"METADATA_TYPES.DIRECTORY," + 
			"METADATA_TYPES.TAG," + 
			"METADATA.VALUE " + 
			"from "+ DBTables.DBNAME +".MEDIA " + 
			"left join " + DBTables.DBNAME + ".METADATA on METADATA.MEDIA_ID=MEDIA.ID " + 
			"left join " + DBTables.DBNAME + ".METADATA_TYPES on METADATA.MDATA_ID = METADATA_TYPES.ID " + 
			"where MEDIA.ID = ?";
	
	private int mediaID;
	
	public GetMediaInfoProvider(int mediaID) {
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
