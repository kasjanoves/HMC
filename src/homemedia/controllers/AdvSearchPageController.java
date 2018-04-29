package homemedia.controllers;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.RowSet;

import homemedia.data.GetMetadataTypesProvider;
import homemedia.data.RowSetProvider;

public class AdvSearchPageController extends HttpServlet{

	private static final long serialVersionUID = -3420617873591578787L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		RowSet metadataTypes = null;
		try {
			RowSetProvider getMetadataTypes = new GetMetadataTypesProvider();
			metadataTypes = getMetadataTypes.execute();
		} catch (SQLException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		request.setAttribute("metadataTypes", metadataTypes);
		RequestDispatcher view = request.getRequestDispatcher("AdvSearch.jsp");
		view.forward(request, response);
		
	}

}
