package homemedia.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.RowSet;

import homemedia.data.GetMediaByMetadataAdvProvider;
import homemedia.data.RowSetProvider;

public class AdvSearchController extends HttpServlet {
	
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
				
		RowSet rs = null;
		try {
			RowSetProvider getMediaByMetadataAdv = new GetMediaByMetadataAdvProvider(pmap);
			rs = getMediaByMetadataAdv.execute();
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		request.setAttribute("MediaSet", rs);
		RequestDispatcher view = request.getRequestDispatcher("Home.jsp");
		view.forward(request, response);
	}

}
