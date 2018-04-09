package homemedia.data;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.RowSet;

import homemedia.model.MetadataTag;

public class InsertMetadataTagRowProvider extends ParamPreparedStatementKeyGenProvider {
	
	private static String query ="insert into " + DBTables.DBNAME +
			"." + MetadataTag.TABLE_NAME +
	        " values(NULL,?,?,?,?)";
	
	private MetadataTag mDataTag;
	
	public InsertMetadataTagRowProvider(Connection conn, MetadataTag mDataTag) {
		this.conn=conn;
		this.mDataTag=mDataTag;
	}

	@Override
	void setParam() throws SQLException {
		statement.setString(1, mDataTag.getDestination());
		statement.setString(2, mDataTag.getDirectory());
		statement.setString(3, mDataTag.getTag());
		statement.setString(4, mDataTag.getType().name());
	}

	@Override
	String getQuery() {
		return query;
	}

	@Override
	int execute() throws SQLException {
		RowSetProvider rsProv = new GetMetadataTagExp(mDataTag);
		RowSet rs = rsProv.execute();
		if(rs.next()) return rs.getInt("ID");	//row already exists, returns ID
        return super.execute();
	}
	
	

}
