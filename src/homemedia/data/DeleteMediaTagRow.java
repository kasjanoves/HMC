package homemedia.data;

import java.sql.Connection;
import java.sql.SQLException;

public class DeleteMediaTagRow extends ParamStatementProvider {
	
	private static String query = "delete from " + DBTables.DBNAME +
			".MEDIA_TAGS" +
	        " where MEDIA_TAGS.MEDIA_ID = ? and MEDIA_TAGS.TAG_ID = ?";
	
	private int MediaID;
	private int TagID;

	public DeleteMediaTagRow(Connection conn, int MediaID, int TagID) {
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
