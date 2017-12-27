package com.example.model;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.sql.RowSet;
import javax.sql.rowset.Predicate;

public class MetadataFilter implements Predicate {
	
	private Map<String, String[]> filters;
	private Set<Integer> alreadySelected = new HashSet<Integer>();
	
	public MetadataFilter(Map<String, String[]> filters) {
		this.filters = filters;
	}

	@Override
	public boolean evaluate(RowSet rs) {
		
		if (rs == null) return false;
		
		try {
			int mediaID = rs.getInt("ID");
			int mdataID = rs.getInt("MDATA_ID");
			String mdataType = rs.getString("TYPE");
			
			if(alreadySelected.contains(mediaID)) return false;
						
			String[] cond = filters.get("Select"+mdataID);
			if(cond == null) return true;
			String[] values = filters.get("Input"+mdataID);
			if(values == null) return true;
			
			Object rowValue = null;
			MetadataTypes type = MetadataTypes.valueOf(mdataType.toUpperCase());
			rowValue = rs.getObject(type.getSQLColName());
			
			//Straightforward dumb implementation
			if(mdataType.equals("Num")) {
				if(cond[0].equals("="))
					return rowValue.equals(values[0]);
				if(cond[0].equals(">="))
					return (float)rowValue >= Float.parseFloat(values[0]); 
				if(cond[0].equals("<="))
					return (float)rowValue <= Float.parseFloat(values[0]);	
			}else if(mdataType.equals("DateTime")) {
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public boolean evaluate(Object value, int column) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean evaluate(Object value, String columnName) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	
	
}
