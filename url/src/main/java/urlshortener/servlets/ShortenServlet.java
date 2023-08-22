package urlshortener.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import urlshortener.services.ShortenService;

/**
 * Servlet implementation class ShortenServlet
 */
@WebServlet("/shorten")
public class ShortenServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShortenServlet() {
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

        try (InputStream htmlStream = getServletContext().getResourceAsStream("/shorten.html");
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
        // get the original url from the form post
        String originalUrl = request.getParameter("original-url");

        // call the class that generates the short url
        String shortenedSlug = ShortenService.generateShortUrl(originalUrl);
        String shortenedUrl = getBaseUrl(request) + "/r" + "/" + shortenedSlug;

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        // read the /result.html file and replace the placeholders with the original and shortened
        try (InputStream htmlStream = getServletContext().getResourceAsStream("/result.html");
                BufferedReader reader = new BufferedReader(new InputStreamReader(htmlStream))) {

            StringBuilder htmlContent = new StringBuilder();
            String line;
            // go through the file line by line and replace the placeholders
            while ((line = reader.readLine()) != null) {
                htmlContent.append(line);
            }

            htmlContent = new StringBuilder(htmlContent.toString()
                    .replace("${originalUrl}", originalUrl)
                    .replace("${shortenedUrl}", shortenedUrl));

            // write back to the /result.html
            response.getWriter().write(htmlContent.toString());
        }
    }

    private String getBaseUrl(HttpServletRequest request) {
        // return https
        String scheme = request.getScheme();
        // return server name
        String serverName = request.getServerName();
        // return server port like 443 because https
        int serverPort = request.getServerPort();
        // return string that starts with /
        String contextPath = request.getContextPath();

        return scheme + "://" + serverName + ":" + serverPort + contextPath;
    }

}
