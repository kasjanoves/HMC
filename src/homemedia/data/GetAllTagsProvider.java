package homemedia.data;

public class GetAllTagsProvider extends CachedRowSetProvider {
	
	protected final static String query="select ID, NAME from "+ DBTables.DBNAME +".TAGS ";

	@Override
	public	String getQuery() {
		return query;
	}

}
