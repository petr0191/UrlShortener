package com.algonquin.urlshortener.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class ApplicationDao {
	private static final String DB_URL = "jdbc:mysql://localhost:3306/";
	private static final String DB_NAME = "shorturl";
	private static final String DB_USERNAME = "root";
	private static final String DB_PASSWORD = "password";
	private static final String DB_USE_STATEMENT = "USE " + DB_NAME;
	
	private static Connection connection = null;
	
	
	//Singleton pattern which returns the connection instead of the object
	public static Connection getConnectionToDatabase() {
        if (connection == null) {
            synchronized (ApplicationDao.class) {
                if (connection == null) {
                    try {
                        // Load the driver class
                        Class.forName("com.mysql.cj.jdbc.Driver");

                        // Get hold of the DriverManager
                        connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
                        
                        //Create the database if needed
                        createDbIfRequired();

                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return connection;
    }

	
	private ApplicationDao() {}

	

	private static void createDbIfRequired() {
		try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
			// Create the database if it doesn't exist
			String createDatabaseQuery = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;
			Statement statement = connection.createStatement();
			statement.executeUpdate(createDatabaseQuery);

			// Use the newly created database
			String useDatabaseQuery = DB_USE_STATEMENT;
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

	public static String getDbUseStatement() {
		return DB_USE_STATEMENT;
	}

}
