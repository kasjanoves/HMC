package homemedia.controllers;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.RowSet;

import homemedia.data.DBTables;
import homemedia.data.GetMediaInfoProvider;
import homemedia.data.GetMediaTagsProvider;
import homemedia.data.GetUnselectedTagsProvider;
import homemedia.data.RowSetProvider;

public class MediaViewController extends HttpServlet{

	private static final long serialVersionUID = -4253654296132262981L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String id = request.getParameter("id");
		
		RowSet mediaInfo = null, mediaTags = null, unselectedTags = null;
		
		try {
			int mediaID = Integer.parseInt(id);
			RowSetProvider getMediaInfo = new GetMediaInfoProvider(mediaID),
					getMediaTags = new GetMediaTagsProvider(mediaID),
					getUnselectedTags = new GetUnselectedTagsProvider(mediaID);
			mediaInfo = getMediaInfo.execute();
			mediaTags = getMediaTags.execute();
			unselectedTags = getUnselectedTags.execute();
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		
		request.setAttribute("mediaInfo", mediaInfo);
		request.setAttribute("mediaTags", mediaTags);
		request.setAttribute("unselectedTags", unselectedTags);
		RequestDispatcher view = request.getRequestDispatcher("View.jsp");
		view.forward(request, response);
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

}
