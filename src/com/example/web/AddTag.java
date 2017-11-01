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

public class AddTag extends HttpServlet {
	private static final long serialVersionUID = -335418515443847548L;
	private int SelectedTagID =-1;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String tagSelect = request.getParameter("tagSelect");
		if(tagSelect != null)
			SelectedTagID = Integer.parseInt(tagSelect);
		String tagName = request.getParameter("tag");
		int MediaID = Integer.parseInt(request.getParameter("id"));
		JDBCUtilities util = (JDBCUtilities) getServletContext().getAttribute("DBUtils");
		Connection conn = null;
		
		try {
			conn = util.getConnection();
			int TagRowID;
			if(SelectedTagID == -1)
				TagRowID = DBTables.insertTagsRow(conn, tagName);
			else
				TagRowID = SelectedTagID;
			DBTables.insertMediaTagRow(conn, MediaID, TagRowID);
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
	
}
