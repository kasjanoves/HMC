package homemedia.data;

import java.sql.Connection;

public class DeleteUnusedTags extends PreparedStatementProvider {
	
	private static String query = "delete from " + DBTables.DBNAME +
			".TAGS where ID not in " +
			"(select distinct TAG_ID from " + DBTables.DBNAME + ".MEDIA_TAGS)";

	public DeleteUnusedTags(Connection conn) {
		super(conn);
	}

	@Override
	public	String getQuery() {
		return query;
	}

}
