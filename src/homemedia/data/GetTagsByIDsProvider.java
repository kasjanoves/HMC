package homemedia.data;

import java.sql.SQLException;
import java.util.Set;
import java.util.StringJoiner;

public class GetTagsByIDsProvider extends ParamRowSetProvider {

	protected final static String query="select ID, NAME from "+ DBTables.DBNAME +".TAGS "	+ 
			"where ID in (?)";
			
	private Set<Integer> tags;	
	
	public GetTagsByIDsProvider(Set<Integer> tags) {
		this.tags=tags;
	}
	
	@Override
	void setParams() throws SQLException {
		StringJoiner sj = new StringJoiner(",");
		sj.setEmptyValue("null");
		for(Integer tag : tags)
			sj.add(tag.toString());
		rowSet.setString(1, sj.toString());
	}

	@Override
	public	String getQuery() {
		return query;
	}

}
