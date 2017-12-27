package com.example.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.model.DBTables;
import com.example.model.JDBCUtilities;

public class AdvSearch extends HttpServlet {
	
	private static final long serialVersionUID = 4954246757302417042L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Map<String, String[]> pmap = request.getParameterMap();
		for( Entry<String, String[]> entry : pmap.entrySet()) {
			System.out.print(entry.getKey()+":");
			System.out.println(Arrays.deepToString(entry.getValue()));
		}
				
		ResultSet rs = DBTables.getMediaByMeatadataAdv(pmap);
		request.setAttribute("MediaSet", rs);
		RequestDispatcher view = request.getRequestDispatcher("Home.jsp");
		view.forward(request, response);
	}

}
