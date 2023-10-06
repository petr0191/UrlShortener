<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>URL Shortener</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
        }
	    .container {
	        background-color: #fff;
	        border-radius: 5px;
	        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
	        text-align: center;
	        min-width: 600px; 
	        max-width: 1000px;
	        min-height: 120px; 
	        margin: 0 auto;
	        display: flex;
            justify-content: center;
	    }
        h1 {
			padding-top: 50px;
            margin-top: 0;
        }
        label {
            font-weight: bold;
            display: block;
            margin-bottom: 5px;
        }
        #original-url {
            width: 400px;
            padding: 8px;
            border: 1px solid #ccc;
            border-radius: 3px;
            display: block;
        }
        button {
            display: block;
            width: 85px;
            padding: 8px;
            background-color: #007bff;
            color: #fff;
            border: none;
            border-radius: 3px;
            cursor: pointer;
            margin: 10px auto 0;
        }
        button:hover {
            background-color: #0056b3;
        }
        p {
            margin-top: 10px;
            font-size: 14px;
        }
        
    </style>
</head>
<body>
    <div class="container">
        <h1>URL Shortener</h1>
    </div>
    <div class="container">
        <form method="post" action="shorten">
            <label for="original-url">Enter a long URL:</label>
            <input type="url" id="original-url" name="original-url" placeholder="https://www.example.com" required>
            <button type="submit" formaction="" name="shorten">Shorten</button>
        </form>
        <c:if test="${shortenedUrl != null}">
            <p>Original URL: ${originalUrl}</p>
            <p>Shortened URL: <a href="${shortenedUrl}">${shortenedUrl}</a></p>
        </c:if>
        <table border="1" style="display: flex; justify-content: center;">
        <thead>
            <tr>
                <th>Slug</th>
                <th>Long URL</th>
                <th>Times Used</th>
                <th></th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="urlMap" items="${shortenedUrls}">
                <tr>
                    <td>${urlMap.slug}</td>
                    <td>${urlMap.long_url}</td>
                    <td>${urlMap.count}</td>
                    <td><a href="Details">More..</a></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    </div>
</body>
</html>