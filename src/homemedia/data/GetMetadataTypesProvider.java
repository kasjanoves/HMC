package homemedia.data;

import homemedia.model.MetadataTag;

public class GetMetadataTypesProvider extends CachedRowSetProvider {
	
	protected final static String query="select * "	+
			"from "+ DBTables.DBNAME +"."+MetadataTag.TABLE_NAME +
			" order by DESTINATION";

	@Override
	String getQuery() {
		return query;
	}

}
