package com.algonquin.urlshortener.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
		response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        PrintWriter out = response.getWriter();

        try (InputStream htmlStream = getServletContext().getResourceAsStream("/shorten.html");
             BufferedReader reader = new BufferedReader(new InputStreamReader(htmlStream))) {

            String line;
            while ((line = reader.readLine()) != null) {
            	out.write(line);
            }
        }
        
        List<Map<String, String>> shortenedUrls = ShortenService.getShortenedUrlsForUser(user.getId());
        //Call the dashBoard method to generate the HTML content
        generateDashboardContent(out , user.getEmail(), shortenedUrls);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String originalUrl = request.getParameter("original-url");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        String shortenedSlug = ShortenService.generateShortUrl(originalUrl, user.getId());
        String shortenedUrl = getBaseUrl(request) + "/r"  + "/" + shortenedSlug;
        
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        try (InputStream htmlStream = getServletContext().getResourceAsStream("/result.html");
             BufferedReader reader = new BufferedReader(new InputStreamReader(htmlStream))) {

            StringBuilder htmlContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                htmlContent.append(line);
            }

            htmlContent = new StringBuilder(htmlContent.toString()
                .replace("${originalUrl}", originalUrl)
                .replace("${shortenedUrl}", shortenedUrl));

            response.getWriter().write(htmlContent.toString());
        }
	}
	
    private String getBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();

        return scheme + "://" + serverName + ":" + serverPort + contextPath;
    }

    private void generateDashboardContent(PrintWriter out, String email, List<Map<String, String>> shortenedUrls) {
    	   System.out.println("email: " + email);
   	    
    	    out.print("<div style=\"display: flex; justify-content: center;\">");
    	    out.print("<h2 id=\"email\">" + email + "</h2>");
    	    out.print("<h2 style=\"margin-left: 10px;\">Dashboard</h2>");
    	    out.print("</div>");
    	    out.println("<table border='1' style=\"display: flex; justify-content: center;\">");
    	    out.println("<tr><th>ID</th><th>Slug</th><th>Long URL</th></tr>");

    	    for (Map<String, String> urlData : shortenedUrls) {
    	      out.println("<tr>");
    	      out.println("<td>" + urlData.get("id") + "</td>");
    	      out.println("<td>" + urlData.get("slug") + "</td>");
    	      out.println("<td>" + urlData.get("long_url") + "</td>");
    	      out.println("</tr>");
    	    }

    	    
    	    out.println("</table>");
    	    out.println("</body>");
    	    out.println("</html>");
    }
}
