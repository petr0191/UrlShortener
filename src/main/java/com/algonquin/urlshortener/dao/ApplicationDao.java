package com.algonquin.urlshortener.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.algonquin.urlshortener.beans.ShortenedUrl;

public class ApplicationDao {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "shorturl";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "password";

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
        
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            Statement statement = connection.createStatement();
            String useDatabaseQuery = "USE " + DB_NAME;
            statement.executeUpdate(useDatabaseQuery);
        	String query = "SELECT * FROM short_urls WHERE slug = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, slug);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                shortenedUrl = new ShortenedUrl();
                shortenedUrl.setId(resultSet.getInt("id"));
                shortenedUrl.setSlug(resultSet.getString("slug"));
                shortenedUrl.setLongUrl(resultSet.getString("long_url"));
                shortenedUrl.setUserId(resultSet.getInt("user_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return shortenedUrl;
    }
    
    public List<ShortenedUrl> getShortenedUrlsByUserId(int userId) {
    	createDbIfRequired();
        List<ShortenedUrl> shortenedUrls = new ArrayList<>();
        
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            Statement statement = connection.createStatement();
            String useDatabaseQuery = "USE " + DB_NAME;
            statement.executeUpdate(useDatabaseQuery);
        	String query = "SELECT * FROM short_urls WHERE user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ShortenedUrl shortenedUrl = new ShortenedUrl();
                shortenedUrl.setId(resultSet.getInt("id"));
                shortenedUrl.setSlug(resultSet.getString("slug"));
                shortenedUrl.setLongUrl(resultSet.getString("long_url"));
                shortenedUrl.setUserId(resultSet.getInt("user_id"));
                shortenedUrls.add(shortenedUrl);
            }
        } catch (SQLException e) {
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
    
    private void createDbIfRequired() {
    	try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            // Create the database if it doesn't exist
            String createDatabaseQuery = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;
            Statement statement = connection.createStatement();
            statement.executeUpdate(createDatabaseQuery);

            // Use the newly created database
            String useDatabaseQuery = "USE " + DB_NAME;
            statement.executeUpdate(useDatabaseQuery);

            // Create the table for short URLs if it doesn't exist
            String createShortUrlsTableQuery = "CREATE TABLE IF NOT EXISTS short_urls ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "slug VARCHAR(255) NOT NULL,"
                    + "long_url VARCHAR(255) NOT NULL,"
                    + "user_id INT,"
                    + "INDEX idx_short_url (slug)"
                    + ")";
            statement.executeUpdate(createShortUrlsTableQuery);

            // Create the table for click information if it doesn't exist
            String createClicksTableQuery = "CREATE TABLE IF NOT EXISTS short_url_clicks ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "short_url_id INT,"
                    + "click_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                    + ")";
            statement.executeUpdate(createClicksTableQuery);

            System.out.println("Database and tables created successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
