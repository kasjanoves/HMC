package homemedia.controllers;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.RowSet;

import homemedia.data.DeleteMediaRow;
import homemedia.data.DeleteUnusedTags;
import homemedia.data.GetMediaByIdProvider;
import homemedia.data.JDBCUtilities;
import homemedia.data.RowSetProvider;
import homemedia.data.StatementProvider;

public class DeleteController extends HttpServlet{
	
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
			RowSetProvider getMediaById = new GetMediaByIdProvider(MediaID);
			RowSet rs = getMediaById.execute();
			if(rs.next()) {
				String relPath = rs.getString("PATH");
				String tmbPath = rs.getString("THUMB_PATH");
				String filePath = getServletContext().getRealPath("/"+relPath);
				File file = new File(filePath);
				if(file.exists()) {
					file.delete();
				}
				if(tmbPath != null && !tmbPath.isEmpty()) {
					filePath = getServletContext().getRealPath("/"+tmbPath);
					file = new File(filePath);
					if(file.exists()) {
						file.delete();
					}
				}
				StatementProvider deleteMediaRow = new DeleteMediaRow(conn, MediaID);
				deleteMediaRow.execute();
				StatementProvider deleteUnusedTags = new DeleteUnusedTags(conn);
				deleteUnusedTags.execute();
			}
			
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}finally {
			if(conn != null)
				util.closeConnection(conn);
		}
		
		RequestDispatcher view = request.getRequestDispatcher("Home.jsp");
		view.forward(request, response);
	}
	
}
