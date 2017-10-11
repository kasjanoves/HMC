package com.example.web;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.model.DBTables;
import com.example.model.JDBCUtilities;

public class Delete extends HttpServlet{
	private static final long serialVersionUID = 127228186509183610L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int MediaID = Integer.parseInt(request.getParameter("id"));
		JDBCUtilities util = (JDBCUtilities) getServletContext().getAttribute("DBUtils");
		Connection conn = null;
		
		try {
			conn = util.getConnection();
			ResultSet rs = DBTables.getMediaById(conn, MediaID);
			if(rs.next()) {
				String relPath = rs.getString("PATH");
				String tmbPath = rs.getString("THUMB_PATH");
				String filePath = getServletContext().getRealPath("/"+relPath);
				File file = new File(filePath);
				if(file.exists()) {
					file.delete();
				}
				if(!tmbPath.isEmpty()) {
					filePath = getServletContext().getRealPath("/"+tmbPath);
					file = new File(filePath);
					if(file.exists()) {
						file.delete();
					}
				}
				DBTables.deleteMediaRow(conn, MediaID);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}finally {
			if(conn != null)
				util.closeConnection(conn);
		}
		
		RequestDispatcher view = request.getRequestDispatcher("Home.jsp");
		view.forward(request, response);
	}
	
}
