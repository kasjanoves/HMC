package com.example.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.example.model.DBTables;
import com.example.model.JDBCUtilities;
import com.example.model.MetadataTypes;

public class MetadataFiltersTagHandler extends SimpleTagSupport{

	public void doTag() throws JspException, IOException {
		PageContext pageContext = (PageContext) getJspContext();
		JspWriter out = pageContext.getOut();
		
		JDBCUtilities util = (JDBCUtilities) pageContext.getServletContext().getAttribute("DBUtils");
		Connection conn = null;
		ResultSet rs = null;
		
    	try {
			conn = util.getConnection();
			rs = DBTables.getMetadataTypes(conn);
			
			String CurrentDest = "";
			while(rs.next()) {
				String Dest = rs.getString("DESTINATION");
				String Tag = rs.getString("TAG");
				String TagID = rs.getString("ID");
				String Type = rs.getString("TYPE");
				if(!Dest.equals(CurrentDest)) {
					CurrentDest = new String(Dest);
					out.print("<br>"+CurrentDest+"<br>");
				}
				//out.print(Tag);
				StringBuilder sb = new StringBuilder();
				MetadataTypes type = MetadataTypes.valueOf(Type.toUpperCase());
				for(String comp : type.Comparators())
					sb.append(String.format(OPTION_TEMPLATE, comp));
				out.print(String.format(FILTER_TEMPLATE, Tag, "Select"+TagID,
						sb.toString(), "Input"+TagID, type.getInputType()));
				out.print("<br>");
				
			}
			
    	} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return;
		}
    	
    	if(conn != null)
			util.closeConnection(conn);
	}
	
	private static final String FILTER_TEMPLATE = "%1$s <select name='%2$s'>%3$s</select> "
			+ "<input name='%4$s' type='%5$s'>";
	private static final String OPTION_TEMPLATE = " <option value=%1$s>%1$s</option>";
}

