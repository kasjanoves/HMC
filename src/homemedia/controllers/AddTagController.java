package homemedia.controllers;

import java.io.IOException;
import java.sql.Connection;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import homemedia.data.DBTables;
import homemedia.data.InsertMediaTagRow;
import homemedia.data.InsertTagRow;
import homemedia.data.JDBCUtilities;
import homemedia.data.StatementProvider;

public class AddTagController extends HttpServlet {
	
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
			if(SelectedTagID == -1) {
				StatementProvider insertTagRow = new InsertTagRow(conn, tagName);
				TagRowID = insertTagRow.execute();
			}
			else
				TagRowID = SelectedTagID;
			StatementProvider insertMediaTagRow = new InsertMediaTagRow(conn, MediaID, TagRowID);
			insertMediaTagRow.execute();
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}finally {
			if(conn != null)
				util.closeConnection(conn);
		}
		
		response.sendRedirect("/Home");
    	
	}
	
}
