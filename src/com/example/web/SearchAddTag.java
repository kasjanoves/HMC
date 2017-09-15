package com.example.web;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SearchAddTag extends HttpServlet{
	private static final long serialVersionUID = 4572240132719784111L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String tag_id = request.getParameter("tag_id");
		if(tag_id != null) {
			HttpSession session = request.getSession();
			Set<Integer> tags = (HashSet<Integer>) session.getAttribute("SelectedTags");
			if(tags == null)
				tags = new HashSet<Integer>();
			tags.add(Integer.parseInt(tag_id));
			session.setAttribute("SelectedTags", tags);
			
			RequestDispatcher view = request.getRequestDispatcher("Home.jsp");
			view.forward(request, response);
		}
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

}
