package com.algonquin.urlshortener.servlets;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.algonquin.urlshortener.beans.User;
import com.algonquin.urlshortener.services.UserService;

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
	    RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
	    dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.print("Login posted!");
		String loginButton = request.getParameter("login");
		String signupButton = request.getParameter("signup");

		if (loginButton != null) {
			String useremail = request.getParameter("signinemail");
			String userpassword = request.getParameter("signinpassword");
			
			User signin = UserService.loginUser(useremail, userpassword);

			if (signin != null) {
				// Create session to pass on user
				HttpSession session = request.getSession();
		        session.setAttribute("user", signin);

		        // Redirect to Servlet
		        response.sendRedirect("shorten");
			} else {
				// Unsuccessful login, display an error message
	            // Unsuccessful login, display an error message
	            request.setAttribute("errorMessage", "Invalid email or password.");
	            RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
	            dispatcher.forward(request, response);
			}
		} else if (signupButton != null) {
			String newuseremail = request.getParameter("signupemail");
			String newuserpassword = request.getParameter("signuppassword");
			UserService.insertUser(newuseremail, newuserpassword);	
			doGet(request, response);
		}
	}
}
