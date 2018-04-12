package homemedia.data;

import java.sql.Connection;
import java.sql.SQLException;

public class UpdateMediaDescription extends ParamStatementProvider {
	
	private static String query = "update " + DBTables.DBNAME +
			".MEDIA set DESCRIPTION = ? " +
			"where MEDIA.ID = ?";
	
	private int MediaID;
	private String description;

	public UpdateMediaDescription(Connection conn, int MediaID, String description) {
		super(conn);
		this.MediaID=MediaID;
		this.description=description;
	}

	@Override
	void setParam() throws SQLException {
		statement.setString(1, description);
		statement.setInt(2, MediaID);
	}

	@Override
	public	String getQuery() {
		return query;
	}

}
