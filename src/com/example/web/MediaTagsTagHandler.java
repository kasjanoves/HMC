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

public class MediaTagsTagHandler extends SimpleTagSupport{

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
			rs = DBTables.getMediaTags(conn, id);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return;
		}
    	
    	try {
			while(rs.next()){
				String tagName = rs.getString("NAME");
				int tagID = rs.getInt("TAG_ID");
				out.print(String.format(TAG_TEMPLATE, tagName, id, tagID));
			}
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
	
	private static final String TAG_TEMPLATE = "[%1$s <a href='RemoveTag.do?id=%2$d&tag_id=%3$d'>X</a>] ";
}
