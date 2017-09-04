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

public class MediaViewTagHandler extends SimpleTagSupport {
	
	private int id;
	
	public void setId(int id){
		this.id = id;
	}

	public void doTag() throws JspException, IOException {
		PageContext pageContext = (PageContext) getJspContext();
		JspWriter out = pageContext.getOut();
		ResultSet rs = null;
		
		JDBCUtilities util = (JDBCUtilities) pageContext.getServletContext().getAttribute("DBUtils");
		Connection conn = null;
    	try {
			conn = util.getConnection();
			rs = DBTables.getMediaAndMetadata(conn, id);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return;
		}
    	
    	String path = "";
    	String descr = "";
    	String table = "<table>";
    	try {
			while(rs.next()){
				path = rs.getString("PATH");
				descr = rs.getString("DESCRIPTION");
				String directory = rs.getString("DIRECTORY");
				String tag = rs.getString("TAG");
				String value = rs.getString("VALUE");
				table = table + String.format(ROW_TEMPLATE, directory, tag, value);
			}
			table = table + "</table>";
			if(rs.getString("TYPE").equalsIgnoreCase("image")) 
				out.print(String.format(IMG_TEMPLATE, path, descr));
			else
				out.print(String.format(VIDEO_TEMPLATE, path));
			out.print(descr+"<br>");
			out.print(table);
			
					
		} catch (SQLException e) {
			JDBCUtilities.printSQLException(e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					JDBCUtilities.printSQLException(e);
				}
		}
    	
    	if(conn != null)
			util.closeConnection(conn);
    	
	}
	
	private static final String IMG_TEMPLATE = "<img src='%1$s' alt='%2$s'>";
	private static final String VIDEO_TEMPLATE = "<video src='%1$s' controls='controls'>"
			+ "Video not supported..."
			+ "<a href='%1$s'>Download video</a>.</video>";
	private static final String ROW_TEMPLATE = "<tr><td>'%1$s'</td><td>'%2$s'</td><td>'%3$s'</td></tr>";
	
}
