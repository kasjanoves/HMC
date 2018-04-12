package homemedia.data;

import java.sql.Connection;
import java.sql.SQLException;

import homemedia.model.Media;

public class InsertMediaRowProvider extends ParamPreparedStatementKeyGenProvider {
	
	private static String query ="insert into " + DBTables.DBNAME +
			"." + Media.TABLE_NAME +
			" values(NULL,?,?,?,?,?,?)";
	
	private Media media;

	public InsertMediaRowProvider(Connection conn, Media media) {
		super(conn);
		this.media=media;
	}
	
	@Override
	void setParam() throws SQLException {
		statement.setString(1, media.getMediaType());
		statement.setString(2, media.getDescription());
		statement.setString(3, media.getPath());
		statement.setString(4, media.getThumbnailPath());
		statement.setLong(5, media.getSize());
		statement.setTimestamp(6, new java.sql.Timestamp(media.getCreationDate().getTime()));
	}

	@Override
	public	String getQuery() {
		return query;
	}

}
