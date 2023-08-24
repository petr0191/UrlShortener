package com.algonquin.urlshortener.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.algonquin.urlshortener.beans.ShortenedUrl;
import com.algonquin.urlshortener.beans.User;

public class ApplicationDao {
	private static final String DB_URL = "jdbc:mysql://localhost:3306/";
	private static final String DB_NAME = "shorturl";
	private static final String DB_USERNAME = "root";
	private static final String DB_PASSWORD = "password";
	
	static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


	public ApplicationDao() {

	}

	public void insertShortenedUrl(ShortenedUrl shortenedUrl) {
		createDbIfRequired();
		try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
			Statement statement = connection.createStatement();
			String useDatabaseQuery = "USE " + DB_NAME;
			statement.executeUpdate(useDatabaseQuery);

			String query = "INSERT INTO short_urls (slug, long_url, user_id) VALUES (?, ?, ?)";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, shortenedUrl.getSlug());
			preparedStatement.setString(2, shortenedUrl.getLongUrl());
			preparedStatement.setInt(3, shortenedUrl.getUserId());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	 public ShortenedUrl getShortenedUrlBySlug(String slug) {
	        createDbIfRequired();
	        ShortenedUrl shortenedUrl = null;

	        // connect to the database
	        try {
	            Class.forName("com.mysql.jdbc.Driver");
	            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
	            Statement statement = connection.createStatement();
	            String useDatabaseQuery = "USE " + DB_NAME;
	            statement.executeUpdate(useDatabaseQuery);

	            // create the query and run it
	            String query = "SELECT * FROM short_urls WHERE slug = ?";
	            PreparedStatement preparedStatement = connection.prepareStatement(query);
	            preparedStatement.setString(1, slug);
	            ResultSet resultSet = preparedStatement.executeQuery();
	            if (resultSet.next()) {
	                // create a new ShortenedUrl object
	                shortenedUrl = new ShortenedUrl();
	                shortenedUrl.setId(resultSet.getInt("id"));
	                shortenedUrl.setSlug(resultSet.getString("slug"));
	                shortenedUrl.setLongUrl(resultSet.getString("long_url"));
	                shortenedUrl.setUserId(resultSet.getInt("user_id"));
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return shortenedUrl;
	    }

	public List<Map<String, String>> getShortenedUrlsByUserId(int userId) {
        createDbIfRequired();
        // List<ShortenedUrl> shortenedUrls = new ArrayList<>();

        // create a map list to store the data
        List<Map<String, String>> shortenedUrls = new ArrayList<>();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            Statement statement = connection.createStatement();
            String useDatabaseQuery = "USE " + DB_NAME;
            statement.executeUpdate(useDatabaseQuery);

            String query = "SELECT * FROM short_urls WHERE user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                // ShortenedUrl shortenedUrl = new ShortenedUrl();
                // shortenedUrl.setId(resultSet.getInt("id"));
                // shortenedUrl.setSlug(resultSet.getString("slug"));
                // shortenedUrl.setLongUrl(resultSet.getString("long_url"));
                // shortenedUrl.setUserId(resultSet.getInt("user_id"));
                // shortenedUrls.add(shortenedUrl);

                Map<String, String> idData = new HashMap<>();
                idData.put("id", resultSet.getString("id"));
                idData.put("slug", resultSet.getString("slug"));
                idData.put("long_url", resultSet.getString("long_url"));
                idData.put("user_id", resultSet.getString("user_id"));

                shortenedUrls.add(idData);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return shortenedUrls;
    }

	public void registerClick(int shortUrlId) {
		createDbIfRequired();
		try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
			Statement statement = connection.createStatement();
			String useDatabaseQuery = "USE " + DB_NAME;
			statement.executeUpdate(useDatabaseQuery);
			String query = "INSERT INTO short_url_clicks (short_url_id) VALUES (?)";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, shortUrlId);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insertUser(User user) {
		createDbIfRequired();
		try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
			Statement statement = connection.createStatement();
			String useDatabaseQuery = "USE " + DB_NAME;
			statement.executeUpdate(useDatabaseQuery);
			String query = "INSERT INTO users (email, password) VALUES (?, ?)";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, user.getEmail());
			preparedStatement.setString(2, user.getPassword());

			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public User getUserByEmail(String email) {
		createDbIfRequired();
		User user = null;

		try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
			Statement statement = connection.createStatement();
			String useDatabaseQuery = "USE " + DB_NAME;
			statement.executeUpdate(useDatabaseQuery);

			String query = "SELECT * FROM users WHERE email = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, email);

			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				int id = resultSet.getInt("id");
				String fetchedEmail = resultSet.getString("email");
				String password = resultSet.getString("password");

				user = new User(id, fetchedEmail, password);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return user;
	}

	public User getUserSignIn(String email, String password) {
		createDbIfRequired();
		User user = null;

		try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
			Statement statement = connection.createStatement();
			String useDatabaseQuery = "USE " + DB_NAME;
			statement.executeUpdate(useDatabaseQuery);

			String query = "SELECT * FROM users WHERE email = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, email);

			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				int id = resultSet.getInt("id");
				String fetchedEmail = resultSet.getString("email");
				String fetchedpassword = resultSet.getString("password");

				if (password.equals(fetchedpassword)) {
					user = new User(id, fetchedEmail, fetchedpassword);
					}
			}
			} catch (SQLException e) {
			e.printStackTrace();
		
		}
		return user;
	}

	private void createDbIfRequired() {
		try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
			// Create the database if it doesn't exist
			String createDatabaseQuery = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;
			Statement statement = connection.createStatement();
			statement.executeUpdate(createDatabaseQuery);

			// Use the newly created database
			String useDatabaseQuery = "USE " + DB_NAME;
			statement.executeUpdate(useDatabaseQuery);

			// User table
			String createUsersTableQuery = "CREATE TABLE IF NOT EXISTS users (" + "id INT AUTO_INCREMENT PRIMARY KEY,"
					+ "email VARCHAR(255) NOT NULL UNIQUE," + "password VARCHAR(255) NOT NULL" + ")";
			statement.executeUpdate(createUsersTableQuery);

			// Short urls table
			String createShortUrlsTableQuery = "CREATE TABLE IF NOT EXISTS short_urls ("
					+ "id INT AUTO_INCREMENT PRIMARY KEY," + "slug VARCHAR(255) NOT NULL,"
					+ "long_url VARCHAR(255) NOT NULL," + "user_id INT," + "INDEX idx_short_url (slug)" + ")";
			statement.executeUpdate(createShortUrlsTableQuery);

			// Click info
			String createClicksTableQuery = "CREATE TABLE IF NOT EXISTS short_url_clicks ("
					+ "id INT AUTO_INCREMENT PRIMARY KEY," + "short_url_id INT,"
					+ "click_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP" + ")";
			statement.executeUpdate(createClicksTableQuery);

			System.out.println("Database and tables created successfully");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
