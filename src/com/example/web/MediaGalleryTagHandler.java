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

/**
 * @author arendator
 *
 */
public class MediaGalleryTagHandler extends SimpleTagSupport{
	
	private String SearchString = null;
	
	public void setSearch(String SearchString){
		this.SearchString = SearchString;
	}
	
	public void doTag() throws JspException, IOException {
		PageContext pageContext = (PageContext) getJspContext();
		JspWriter out = pageContext.getOut();
		JDBCUtilities util = (JDBCUtilities) pageContext.getServletContext().getAttribute("DBUtils");
		Connection conn = null;
		ResultSet rs = null;
		
		try {
			conn = util.getConnection();
			if(SearchString.isEmpty()) 
				rs = DBTables.getMedia(conn);
			else
				rs = DBTables.getMediaByDescription(conn, SearchString);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return;
		}
		    	
    	int rowSize = 3;
    	int itemsCount = 0;
    	String descrRow = "<tr>";
    	
    	out.print("<table>");
    	out.print("<tr>");
    	try {
			while(rs.next()){
				String path = rs.getString("PATH");
				String descr = rs.getString("DESCRIPTION");
				int id = rs.getInt("ID");
													
				out.print("<td align='center'>");
				if(rs.getString("TYPE").equalsIgnoreCase("image"))
					out.print(String.format(IMG_TEMPLATE, path, descr, id));
				else
					out.print(String.format(VIDEO_TEMPLATE, path));
				out.print("</td>");
				descrRow = descrRow + "<td align='center'>" + descr + "</td>";
				
				if(++itemsCount == rowSize) {
					out.print("</tr>");
					descrRow = descrRow + "</tr>";
					out.print(descrRow);
					descrRow = "<tr>";
					if(!rs.isLast())
						out.print("<tr>");
					itemsCount = 0;
				}
				
			}
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
    	if(itemsCount>0) {
	    	descrRow = descrRow + "</tr>";
			out.print(descrRow);
	    	out.print("</tr>");
    	}
    	out.print("</table>");
    	
    	if(conn != null)
			util.closeConnection(conn);
    	
	}
	
	private static final String IMG_TEMPLATE = "<a href='View.jsp?id=%3$d'>"
			+ "<img src='%1$s' width='250' alt='%2$s'></a>";
	private static final String VIDEO_TEMPLATE = "<video src='%1$s' width='250' controls='controls'>"
			+ "Video not supported..."
			+ "<a href='%1$s'>Download video</a>.</video>";
}
