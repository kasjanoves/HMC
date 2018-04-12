package homemedia.data;

import java.sql.Connection;
import java.sql.SQLException;

public class InsertTagRow extends ParamPreparedStatementKeyGenProvider {
	
	private static String query = "insert into " + DBTables.DBNAME +
			".TAGS" + 
	        " values(NULL,?)";
	
	private String tag;
	
	public InsertTagRow(Connection conn, String tag) {
		super(conn);
		this.tag=tag;
	}

	@Override
	void setParam() throws SQLException {
		statement.setString(1, tag);
	}

	@Override
	public	String getQuery() {
		return query;
	}

}
