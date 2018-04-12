package homemedia.data;

import homemedia.model.Media;

public class GetMediaProvider extends CachedRowSetProvider {
	
	protected final static String query="select * " +
			"from " + DBTables.DBNAME + "." + Media.TABLE_NAME;

	@Override
	public	String getQuery() {
		return query;
	}
	
}
