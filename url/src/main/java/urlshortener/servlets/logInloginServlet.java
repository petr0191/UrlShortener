package urlshortener.servlets;

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

import urlshortener.dao.ApplicationDao;

@WebServlet("/loginServlet")
public class logInloginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // get the username and passowrd from the form post
        String originalEmail = request.getParameter("email");
        String originalPassword = request.getParameter("password");

        // request.getRequestDispatcher("login.html").include(request, response);

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        // check if the email and password are in the database
        ApplicationDao db = new ApplicationDao();

        // if retrun is null then the email is not in the database
        if (db.getUserByEmail(originalEmail) == null) {
            // send error message
            request.getRequestDispatcher("login.html").include(request, response);
            out.println("Email not found");
            System.out.println("Email not found");
        } else {
            // check if the password is correct
            if (db.getUserByEmail(originalEmail).getPassword().equals(originalPassword)) {

                // read the /result.html file and replace the placeholders with the original and
                // shortened
                try (InputStream htmlStream = getServletContext().getResourceAsStream("/afterlogin.html");
                        BufferedReader reader = new BufferedReader(new InputStreamReader(htmlStream))) {

                    StringBuilder htmlContent = new StringBuilder();
                    String line;
                    // go through the result.html line by line and replace the originalUrl and
                    // shortenedurl
                    while ((line = reader.readLine()) != null) {
                        htmlContent.append(line);
                    }

                    htmlContent = new StringBuilder(htmlContent.toString()
                            .replace("${email}", originalEmail));

                    // write back to the /result.html
                    response.getWriter().write(htmlContent.toString());
                }

                // send to the user page
                response.sendRedirect("afterlogin.html");
                System.out.println("Password correct");
            } else {
                // send error message
                request.getRequestDispatcher("login.html").include(request, response);
                out.println("Incorrect password");
                System.out.println("Incorrect password");
            }
        }
    }

}
