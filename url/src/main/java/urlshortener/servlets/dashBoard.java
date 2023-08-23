package urlshortener.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import urlshortener.dao.ApplicationDao;

@WebServlet("/dashBoard")
public class dashBoard extends HttpServlet {

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    // get eamil from afterlogin.html
    String email = request.getParameter("email");

    System.out.println("email: " + email);

    // retrieve the data from the database according to the email
    // create a database object
    ApplicationDao db = new ApplicationDao();

    List<Map<String, String>> shortenedUrls = new ArrayList<>();

    shortenedUrls = db.getShortenedUrlsByUserId(-1);

    response.setContentType("text/html");
    response.setCharacterEncoding("UTF-8");

    PrintWriter out = response.getWriter();

    out.println("<!DOCTYPE html>");
    out.println("<html>");
    out.println("<head>");
    out.println("<title id=\"email\">" + email + "Dashboard </title>");
    out.println("</head>");
    out.println("<body>");
    out.print("<div style=\"display: flex; align-items: center;\">");
    out.print("<h2 id=\"email\">" + email + "</h2>");
    out.print("<h2 style=\"margin-left: 10px;\">Dashboard</h2>");
    out.print("</div>");
    out.println("<table border='1'>");
    out.println("<tr><th>ID</th><th>Slug</th><th>Long URL</th><th>User ID</th></tr>");

    for (Map<String, String> urlData : shortenedUrls) {
      out.println("<tr>");
      out.println("<td>" + urlData.get("id") + "</td>");
      out.println("<td>" + urlData.get("slug") + "</td>");
      out.println("<td>" + urlData.get("long_url") + "</td>");
      out.println("<td>" + urlData.get("user_id") + "</td>");
      out.println("</tr>");
    }

    out.println("</table>");
    out.println("</body>");
    out.println("</html>");
  }

}
