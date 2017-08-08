/**
 * 
 */
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
	
	public void doTag() throws JspException, IOException {
		PageContext pageContext = (PageContext) getJspContext();
		JspWriter out = pageContext.getOut();
		ResultSet rs = null;
		
		JDBCUtilities util = new JDBCUtilities("root","root");
    	try {
			Connection conn = util.getConnection();
			rs = DBTables.getMedia(conn, "hmcatalog");
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
													
				out.print("<td align='center'>");
				out.print(String.format(IMG_TEMPLATE, path, descr));
				//out.print(descr);
				out.print("</td>");
				descrRow = descrRow + "<td align='center'>" + descr + "</td>";
				
				if(++itemsCount == rowSize) {
					out.print("</tr>");
					out.print("<tr>");
					descrRow = descrRow + "</tr>";
					out.print(descrRow);
					descrRow = "<tr>";
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
    	descrRow = descrRow + "</tr>";
		out.print(descrRow);
    	out.print("</tr>");
    	out.print("</table>");    	
    	
	}
	
	private static final String IMG_TEMPLATE = "<img src='%1$s' width='250' alt='%2$s'>";
}
