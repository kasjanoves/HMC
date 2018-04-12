package homemedia.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.RowSet;

import homemedia.data.GetMediaByCriteriaProvider;
import homemedia.data.GetMediaProvider;
import homemedia.data.RowSetProvider;

public class SearchController extends HttpServlet {
	
	private static final long serialVersionUID = 6446669769941915608L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String searchString = request.getParameter("search");
		Set<Integer> tags = (HashSet<Integer>) session.getAttribute("SelectedTags");
		
		RowSet rs = null;
		
    	try {
			if(searchString.isEmpty() && (tags == null || tags.isEmpty())) { 
				RowSetProvider getMedia = new GetMediaProvider();
				rs = getMedia.execute();
			}	
			else {
				RowSetProvider getMediaByCriteria = new GetMediaByCriteriaProvider(searchString, tags);
				rs = getMediaByCriteria.execute();
			}	
					
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
    	
    	request.setAttribute("MediaSet", rs);
		RequestDispatcher view = request.getRequestDispatcher("Home.jsp");
		view.forward(request, response);
		
	}
}
