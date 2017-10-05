package com.example.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import javax.servlet.http.HttpSession;
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
	private Set<Integer> tags;
	
	public void setSearch(String SearchString){
		this.SearchString = SearchString;
	}
	
	public void setTags(Set<Integer> tags){
		this.tags = tags;
	}
	
	public void doTag() throws JspException, IOException {
		PageContext pageContext = (PageContext) getJspContext();
		JspWriter out = pageContext.getOut();
		JDBCUtilities util = (JDBCUtilities) pageContext.getServletContext().getAttribute("DBUtils");
		Connection conn = null;
		ResultSet rs = null;
		
		try {
			conn = util.getConnection();
			if(SearchString.isEmpty() && tags == null) 
				rs = DBTables.getMedia(conn);
			else
				rs = DBTables.getMediaByCriteria(conn, SearchString, tags);
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
				String tmbPath = rs.getString("THUMB_PATH");
				String descr = rs.getString("DESCRIPTION");
				String type = rs.getString("TYPE");
				int id = rs.getInt("ID");
													
				out.print("<td align='center'>");
				if(type.equals("image"))
					out.print(String.format(IMG_TEMPLATE, path, descr, id));
				else if(type.equals("video"))
					out.print(String.format(IMG_TEMPLATE, tmbPath, descr, id));
				out.print("</td>");
				if(type.equals("video"))
					descrRow = descrRow + "<td align='center'>" + descr + " [video]</td>";
				else
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
