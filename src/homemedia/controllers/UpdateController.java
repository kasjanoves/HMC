package homemedia.controllers;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import homemedia.data.JDBCUtilities;
import homemedia.data.StatementProvider;
import homemedia.data.UpdateMediaDescription;

public class UpdateController extends HttpServlet{
	
	private static final long serialVersionUID = 4817843422617513883L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int MediaID = Integer.parseInt(request.getParameter("id"));
		String descr = request.getParameter("descr");
		JDBCUtilities util = (JDBCUtilities) getServletContext().getAttribute("DBUtils");
		Connection conn = null;
		
		try {
			conn = util.getConnection();
			StatementProvider updateMediaDescription = 
					new UpdateMediaDescription(conn, MediaID, descr);
			updateMediaDescription.execute();
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}finally {
			if(conn != null)
				util.closeConnection(conn);
		}
		
		response.sendRedirect("/View.do");
	}

}
