package homemedia.model;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;

import javax.sql.RowSet;

import homemedia.data.Operator;

public enum MetadataTypes {
	
	NUM(new String[] {"=", "&lt", "&gt", "&lt=", "&gt=", "between"},
			"number",
			"NUM_VALUE"){
		public boolean Test(RowSet rs, String token, String[] values) throws SQLException {
			Float rowValue = rs.getFloat("NUM_VALUE");
			return Operator.Test(token, rowValue, values);
		}
	},
	DATETIME(new String[] {"=", "&lt", "&gt", "&lt=", "&gt=", "between"},
			"date",
			"DATA_VALUE"){
		public boolean Test(RowSet rs, String token, String[] values) throws SQLException {
			Date rowValue = rs.getDate("DATA_VALUE");
			return Operator.Test(token, rowValue, values);
		}
	},
	STRING(new String[] {"=", "contains"},
			"text",
			"VALUE"){
		public boolean Test(RowSet rs, String token, String[] values) throws SQLException {
			String rowValue = rs.getString("VALUE");
			return Operator.Test(token, rowValue, values[0]);
		}
	};
	
	private String[] Comparators;
	private String InputType;
	private String SQLColName;
	private MetadataTypes(String[] comp, String inputType, String sqlColName) {
		Comparators = comp;
		InputType = inputType;
		SQLColName = sqlColName;
	}
	public boolean Test(RowSet rs, String token, String[] values) throws SQLException {
		return false;
	}
			
	public Iterable<String> Comparators() {
		return Arrays.asList(Comparators);
	}

	public String getInputType() {
		return InputType;
	}

	public String getSQLColName() {
		return SQLColName;
	}
			
}
