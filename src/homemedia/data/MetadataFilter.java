package homemedia.data;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.sql.RowSet;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.Predicate;

import homemedia.model.MetadataTypes;

public class MetadataFilter implements Predicate {
	
	private Map<String, String[]> filters;
	private Set<Integer> alreadySelected = new HashSet<Integer>();
	
	public MetadataFilter(Map<String, String[]> filters) {
		this.filters = filters;
	}

	@Override
	public boolean evaluate(RowSet rs) {
		
		CachedRowSet frs = (CachedRowSet)rs;
		boolean evaluation = false;
		int mediaID = -1;
						
		try {
			mediaID = frs.getInt("ID");
			if(alreadySelected.contains(mediaID)) return false;
			int mdataID = frs.getInt("MDATA_ID");
			String mdataType = frs.getString(getColIndexByLabel(rs, "MDATA_TYPE"));
									
			String[] cond = filters.get("Select"+mdataID);
			if(cond == null) return false;
			String[] bounds = filters.get("Input"+mdataID);
			if(bounds == null || bounds.length == 0) return false;
						
			MetadataTypes type = MetadataTypes.valueOf(mdataType.toUpperCase());
									
			evaluation = type.Test(frs, cond[0], bounds);
					
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		//System.out.println(mediaID+": "+evaluation);
		if(evaluation) alreadySelected.add(mediaID); 
		return evaluation;
	}

	@Override
	public boolean evaluate(Object arg0, int arg1) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean evaluate(Object arg0, String arg1) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	
	//because of Oracle's bug
	//Most JDBC ResultSet implementations follow this new pattern but 
	//there are exceptions such as the com.sun.rowset.CachedRowSetImpl class
	//which only uses the column name, ignoring any column labels.
	//https://stackoverflow.com/questions/15184709/cachedrowsetimpl-getstring-based-on-column-label-throws-invalid-column-name
	private static int getColIndexByLabel(RowSet rs, String colLabel) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		for(int i=1; i<=rsmd.getColumnCount(); i++)
			if(rsmd.getColumnLabel(i).equals(colLabel))
				return i;
		return 0;
	}
	
}
