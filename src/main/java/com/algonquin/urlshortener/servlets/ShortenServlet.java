package com.algonquin.urlshortener.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.algonquin.urlshortener.beans.User;
import com.algonquin.urlshortener.services.ShortenService;
import com.algonquin.urlshortener.services.dashBoard;

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
        
        //Call the dashBoard method to generate the HTML content
        dashBoard.generateContent(out , user.getEmail(), user.getId());
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

}
