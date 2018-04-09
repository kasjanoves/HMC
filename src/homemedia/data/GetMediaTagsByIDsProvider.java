package homemedia.data;

import java.sql.SQLException;
import java.util.Set;
import java.util.StringJoiner;

public class GetMediaTagsByIDsProvider extends ParamRowSetProvider {
	
	protected final static String query="select * from " + DBTables.DBNAME + ".MEDIA_TAGS where " +
			"TAG_ID in (?)";
	
	private Set<Integer> tags;
	
	public GetMediaTagsByIDsProvider(Set<Integer> tags) {
		this.tags = tags;
	}

	@Override
	void setParams() throws SQLException {
		StringJoiner sj = new StringJoiner(",");
		for(Integer tag : tags)
			sj.add(tag.toString());
		rowSet.setString(1, sj.toString());
	}

	@Override
	String getQuery() {
		return query;
	}

}
