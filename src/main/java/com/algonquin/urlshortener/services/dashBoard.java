package com.algonquin.urlshortener.services;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.algonquin.urlshortener.dao.ApplicationDao;


public class dashBoard {
	public static void generateContent(PrintWriter out, String email, int userid) {

    System.out.println("email: " + email);

    // retrieve the data from the database according to the email
    // create a database object
    ApplicationDao db = new ApplicationDao();

    List<Map<String, String>> shortenedUrls = new ArrayList<>();

    shortenedUrls = db.getShortenedUrlsByUserId(userid);

    
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
