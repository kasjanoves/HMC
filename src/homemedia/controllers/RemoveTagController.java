package homemedia.controllers;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import homemedia.data.DeleteMediaTagRow;
import homemedia.data.DeleteUnusedTags;
import homemedia.data.JDBCUtilities;
import homemedia.data.StatementProvider;

public class RemoveTagController extends HttpServlet{
	private static final long serialVersionUID = 4865237263889847211L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int MediaID = Integer.parseInt(request.getParameter("id"));
		int TagID = Integer.parseInt(request.getParameter("tag_id"));
		
		JDBCUtilities util = (JDBCUtilities) getServletContext().getAttribute("DBUtils");
		Connection conn = null;
		
		try {
			conn = util.getConnection();
			StatementProvider deleteMediaTagRow = new DeleteMediaTagRow(conn, MediaID, TagID),
					deleteUnusedTags = new DeleteUnusedTags(conn);
			deleteMediaTagRow.execute();
			deleteUnusedTags.execute();
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}finally {
			if(conn != null)
				util.closeConnection(conn);
		}
		
		response.sendRedirect("View.do");
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

}
