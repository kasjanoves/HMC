package com.example.web;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.model.DBTables;
import com.example.model.JDBCUtilities;

public class RemoveTag extends HttpServlet{
	private static final long serialVersionUID = 4865237263889847211L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int MediaID = Integer.parseInt(request.getParameter("id"));
		int TagID = Integer.parseInt(request.getParameter("tag_id"));
		
		JDBCUtilities util = (JDBCUtilities) getServletContext().getAttribute("DBUtils");
		Connection conn = null;
		
		try {
			conn = util.getConnection();
			DBTables.deleteMediaTagRow(conn, MediaID, TagID);
			DBTables.deleteUnusedTags(conn);
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}finally {
			if(conn != null)
				util.closeConnection(conn);
		}
		
		RequestDispatcher view = request.getRequestDispatcher("View.jsp");
		view.forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

}
