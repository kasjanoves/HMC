package homemedia.data;

import java.sql.SQLException;

import homemedia.model.MetadataTag;

public class GetMetadataTagExp extends ParamRowSetProvider {
	
	private static String query = "select ID " +
	        "from " + DBTables.DBNAME + "." + MetadataTag.TABLE_NAME +
	        " WHERE DESTINATION = ?" +
	        " AND DIRECTORY = ?" +
	        " AND TAG = ?";
	
	private MetadataTag mDataTag;
	
	public GetMetadataTagExp(MetadataTag mDataTag) {
		this.mDataTag=mDataTag;
	}

	@Override
	void setParams() throws SQLException {
		rowSet.setString(1, mDataTag.getDestination());
		rowSet.setString(2, mDataTag.getDirectory());
		rowSet.setString(3, mDataTag.getTag());
	}

	@Override
	String getQuery() {
		return query;
	}

}
