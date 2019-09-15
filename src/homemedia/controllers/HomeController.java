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

import homemedia.data.GetAllTagsProvider;
import homemedia.data.GetMediaProvider;
import homemedia.data.GetTagsByIDsProvider;
import homemedia.data.RowSetProvider;

public class HomeController extends HttpServlet{

	private static final long serialVersionUID = 2216661634812496242L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		Set<Integer> sessionTags = (HashSet<Integer>) session.getAttribute("SelectedTags");
		if(sessionTags==null) {
			sessionTags = new HashSet<Integer>();
			session.setAttribute("SelectedTags", sessionTags);
		}	
		RowSet media = null,
		       allTags = null,
		       selectedTags = null;
				
		try {
			RowSetProvider getMedia = new GetMediaProvider(),
					getAllTags = new GetAllTagsProvider(),
					getTagsByIDs = new GetTagsByIDsProvider(sessionTags);
			media = getMedia.execute();
			allTags = getAllTags.execute();
			selectedTags = getTagsByIDs.execute();
									
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
    	
    	request.setAttribute("MediaSet", media);
    	request.setAttribute("AllTags", allTags);
    	request.setAttribute("SelectedTags", selectedTags);
    	RequestDispatcher view = request.getRequestDispatcher("Home.jsp");
		view.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
}
