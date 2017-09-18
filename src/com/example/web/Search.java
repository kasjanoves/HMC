package com.example.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.example.model.DBTables;
import com.example.model.JDBCUtilities;

public class Search extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String searchString = request.getParameter("search");
		Set<Integer> tags = (HashSet<Integer>) session.getAttribute("SelectedTags");
		//System.out.println(searchString);
		JDBCUtilities util = (JDBCUtilities) getServletContext().getAttribute("DBUtils");
    	try {
			Connection conn = util.getConnection();
			ResultSet rs = DBTables.getMediaByCriteria(conn, searchString, tags);
			request.setAttribute("MediaSet", rs);
			RequestDispatcher view = request.getRequestDispatcher("Home.jsp");
			view.forward(request, response);
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
    	
		
	}
}
