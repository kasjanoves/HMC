package com.example.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.model.JDBCUtilities;

public class AdvSearch extends HttpServlet {
	
	private static final long serialVersionUID = 4954246757302417042L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Map<String, String[]> pmap = request.getParameterMap();
		System.out.println(pmap);
		
		JDBCUtilities util = (JDBCUtilities) getServletContext().getAttribute("DBUtils");
		Connection conn = null;
		
		try {
			conn = util.getConnection();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}finally {
			if(conn != null)
				util.closeConnection(conn);
		}
		
	}

}
