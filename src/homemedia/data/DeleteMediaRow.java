package homemedia.data;

import java.sql.Connection;
import java.sql.SQLException;

public class DeleteMediaRow extends ParamStatementProvider {
	
	private static String query = "delete from " + DBTables.DBNAME +
			".MEDIA where MEDIA.ID = ?";
	
	private int MediaID;

	public DeleteMediaRow(Connection conn, int MediaID) {
		super(conn);
		this.MediaID=MediaID;
	}

	@Override
	void setParam() throws SQLException {
		statement.setInt(1, MediaID);
	}

	@Override
	public	String getQuery() {
		return query;
	}

}
