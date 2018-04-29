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
import homemedia.data.RowSetProvider;

public class SearchRemoveTagController extends HttpServlet{
	
	private static final long serialVersionUID = -3028867310158620526L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String tag_id = request.getParameter("tag_id");
		if(tag_id != null) {
			HttpSession session = request.getSession();
			Set<Integer> tags = (HashSet<Integer>) session.getAttribute("SelectedTags");
			if(tags != null)
				tags.remove(Integer.parseInt(tag_id));
			session.setAttribute("SelectedTags", tags);
			
			RowSet rs = null;
			try {
				RowSetProvider getMediaByCriteria = new GetMediaByCriteriaProvider(null, tags);
				rs = getMediaByCriteria.execute();
						
			} catch (SQLException e) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
	    	
	    	request.setAttribute("MediaSet", rs);
			RequestDispatcher view = request.getRequestDispatcher("Home.jsp");
			view.forward(request, response);
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
}
