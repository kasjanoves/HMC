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
    	
    	try {
			while(rs.next()){
				String path = rs.getString("PATH");
				String descr = rs.getString("DESCRIPTION");
				
				out.print(String.format(IMG_TEMPLATE, path, descr));
			}
		} catch (SQLException e) {
			JDBCUtilities.printSQLException(e);
		}
    	    	
    	
	}
	
	private static final String IMG_TEMPLATE = "<img src='%1$s' width='250'> %2$s";
}
