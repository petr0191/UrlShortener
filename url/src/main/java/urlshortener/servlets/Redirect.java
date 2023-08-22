package urlshortener.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import urlshortener.services.ShortenService;

/**
 * Servlet implementation class Redirect
 */
@WebServlet("/r/*")
public class Redirect extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Redirect() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Get slug from url path
		String shortSlug = request.getPathInfo().substring(1); 
        String originalUrl = ShortenService.getOriginalUrl(shortSlug);

        if (originalUrl != null) {
            response.sendRedirect(originalUrl);
        } else {
            // Did not found url for given slug
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Original URL not found");
        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
