package homemedia.data;

import java.util.Map;

import javax.sql.rowset.Predicate;

public class GetMediaByMetadataAdvProvider extends FilteredRowSetProvider {
	
	protected final static String query = "select MEDIA.ID," + 
			"MEDIA.TYPE," + 
			"MEDIA.DESCRIPTION," + 
			"MEDIA.PATH," + 
			"MEDIA.THUMB_PATH," + 
			"METADATA.MDATA_ID," + 
			"METADATA.VALUE," + 
			"METADATA.NUM_VALUE," + 
			"METADATA.DATA_VALUE," +
			"METADATA_TYPES.TYPE as MDATA_TYPE from "+ DBTables.DBNAME +".METADATA, " +
			DBTables.DBNAME +".MEDIA, " +
			DBTables.DBNAME +".METADATA_TYPES " +
			"where METADATA.MEDIA_ID = MEDIA.ID " +
			"and METADATA.MDATA_ID = METADATA_TYPES.ID";
	
	private MetadataFilter filter;
	
	public GetMediaByMetadataAdvProvider(Map<String, String[]> params) {
		this.filter = new MetadataFilter(params);
	}

	@Override
	Predicate getFilter() {
		return filter;
	}

	@Override
	public	String getQuery() {
		return query;
	}

}
