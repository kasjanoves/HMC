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

public class TagSelectTagHandler extends SimpleTagSupport{
	
	private int id = -1;
	
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
			if(id == -1)
				rs = DBTables.getAllTags(conn);
			else
				rs = DBTables.getUnselectedTags(conn, id);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return;
		}
    	
    	out.print("<select name='tagSelect'>");
    	try {
			while(rs.next()){
				String tagName = rs.getString("NAME");
				int tagID = rs.getInt("ID");
				out.print(String.format(OPTION_TEMPLATE, tagID, tagName));
			}
			out.print("</select>");
		} catch (SQLException e) {
			JDBCUtilities.printSQLException(e);
		}finally {
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
	
	private static final String OPTION_TEMPLATE = " <option value=%1$d>%2$s</option>";
}
