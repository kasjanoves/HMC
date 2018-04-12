package homemedia.data;

import java.sql.Connection;
import java.sql.SQLException;

import homemedia.model.MetadataRow;
import homemedia.model.MetadataRows;

public class InsertMetadataRows extends ParamStatementProvider {
	
	private static String query = "insert into " + DBTables.DBNAME +
			"." + MetadataRow.TABLE_NAME +
	        " values(?,?,?,?,?)";
	
	private MetadataRows mdataRows;
	
	public InsertMetadataRows(Connection conn, MetadataRows mdataRows) {
		super(conn);
		this.mdataRows=mdataRows;
	}

		
	@Override
	public	int execute() throws SQLException {
		try {
			int[] result = statement.executeBatch();
	    	conn.commit();
	    	conn.setAutoCommit(true);
	    	return result.length;
		} finally {
	        if (statement != null) { statement.close(); }
	    }
	}

	@Override
	void prepareStatement() throws SQLException {
		conn.setAutoCommit(false);
		super.prepareStatement();
	}

	@Override
	void setParam() throws SQLException {
		for(MetadataRow mdataRow : mdataRows.getItems()) {
	    	statement.setInt(1, mdataRows.getMediaRowID());
	    	statement.setInt(2, mdataRow.getRowID());
	    	statement.setString(3, mdataRow.getStringValue());
	    	statement.setFloat(4, mdataRow.getNumValue());
	    	statement.setTimestamp(5, mdataRow.getDateTimeValue());
	    	statement.addBatch();
    	}
	}

	@Override
	public	String getQuery() {
		return query;
	}

}
