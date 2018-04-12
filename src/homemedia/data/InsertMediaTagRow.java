package homemedia.data;

import java.sql.Connection;
import java.sql.SQLException;

public class InsertMediaTagRow extends ParamStatementProvider {
		
	private static String query = "insert into " + DBTables.DBNAME +
			".MEDIA_TAGS" +
	        " values(?,?)";
	
	private int MediaID;
	private int TagID;

	public InsertMediaTagRow(Connection conn, int MediaID, int TagID) {
		super(conn);
		this.MediaID=MediaID;
		this.TagID=TagID;
	}
	
	@Override
	void setParam() throws SQLException {
		statement.setInt(1, MediaID);
		statement.setInt(2, TagID);
	}

	@Override
	public	String getQuery() {
		return query;
	}

}
