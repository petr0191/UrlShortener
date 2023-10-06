package com.algonquin.urlshortener.servlets;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.algonquin.urlshortener.beans.User;
import com.algonquin.urlshortener.services.ShortenService;

/**
 * Servlet implementation class ShortenServlet
 */
@WebServlet("/shorten")
public class ShortenServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    HttpSession session = request.getSession();
	    User user = (User) session.getAttribute("user");
	    if (user != null) {
	    	List<Map<String, String>> shortenedUrls = ShortenService.getShortenedUrlsForUser(user.getId());

		    request.setAttribute("userEmail", user.getEmail());
		    request.setAttribute("shortenedUrls", shortenedUrls);
		    request.setAttribute("baseUrl", this.getBaseUrl(request));
	    }
	    
	    RequestDispatcher dispatcher = request.getRequestDispatcher("/shorten.jsp");
	    dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		   HttpSession session = request.getSession();
		    User user = (User) session.getAttribute("user");

		    String originalUrl = request.getParameter("original-url");
		    String shortenedSlug = ShortenService.generateShortUrl(originalUrl, user.getId());
		    String shortenedUrl = getBaseUrl(request) + "/r" + "/" + shortenedSlug;

		    List<Map<String, String>> shortenedUrls = ShortenService.getShortenedUrlsForUser(user.getId());

		    // Set attributes to pass them to the JSP
		    request.setAttribute("originalUrl", originalUrl);
		    request.setAttribute("shortenedUrl", shortenedUrl);
		    request.setAttribute("shortenedUrls", shortenedUrls);
		    request.setAttribute("baseUrl", this.getBaseUrl(request));

		    // Forward the request to shorten.jsp
		    RequestDispatcher dispatcher = request.getRequestDispatcher("/shorten.jsp");
		    dispatcher.forward(request, response);
	}
	
    private String getBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();

        return scheme + "://" + serverName + ":" + serverPort + contextPath;
    }
}
