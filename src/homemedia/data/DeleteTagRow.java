package homemedia.data;

import java.sql.Connection;
import java.sql.SQLException;

public class DeleteTagRow extends ParamStatementProvider {
	
	private static String query = "delete from " + DBTables.DBNAME +
			".TAGS where TAGS.ID = ?";
	
	private int tagID;

	public DeleteTagRow(Connection conn, int tagID) {
		super(conn);
		this.tagID=tagID;
	}

	@Override
	void setParam() throws SQLException {
		statement.setInt(1, tagID);
	}

	@Override
	public	String getQuery() {
		return query;
	}

}
