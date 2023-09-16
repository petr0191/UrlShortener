package com.algonquin.urlshortener.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.algonquin.urlshortener.beans.User;
import com.algonquin.urlshortener.dao.UserDao;

/**
 * Servlet implementation class ShortenServlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");

		try (InputStream htmlStream = getServletContext().getResourceAsStream("/login.html");
				BufferedReader reader = new BufferedReader(new InputStreamReader(htmlStream))) {

			String line;
			while ((line = reader.readLine()) != null) {
				response.getWriter().write(line);
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		UserDao dao = new UserDao();

		String loginButton = request.getParameter("login");
		String signupButton = request.getParameter("signup");

		if (loginButton != null) {
			String useremail = request.getParameter("signinemail");
			String userpassword = request.getParameter("signinpassword");
			
			User signin = dao.getUserSignIn(useremail, userpassword);

			if (signin != null) {
				// Create session to pass on user
				HttpSession session = request.getSession();
		        session.setAttribute("user", signin);

		        // Redirect to Servlet
		        response.sendRedirect("shorten");
			} else {
				// Unsuccessful login, display an error message
			    response.setContentType("text/html");
			    response.setCharacterEncoding("UTF-8");
			    
			    try (InputStream htmlStream = getServletContext().getResourceAsStream("/login.html");
			         BufferedReader reader = new BufferedReader(new InputStreamReader(htmlStream))) {

			        String line;
			        while ((line = reader.readLine()) != null) {
			            // Append an error message to the login form
			            if (line.contains("<form method=\"post\" action=\"signin\">")) {
			                response.getWriter().write(line);
			                response.getWriter().write("<p style=\"color: red;\">Invalid email or password.</p>");
			            } else {
			                response.getWriter().write(line);
			            }
			        }
			    }
			}
		} else if (signupButton != null) {

			String newuseremail = request.getParameter("signupemail");
			String newuserpassword = request.getParameter("signuppassword");
			User signup = new User(newuseremail, newuserpassword);
			dao.insertUser(signup);
			
			doGet(request, response);
		}
	}
}
