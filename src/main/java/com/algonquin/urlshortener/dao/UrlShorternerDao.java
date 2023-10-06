package com.algonquin.urlshortener.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.algonquin.urlshortener.beans.ShortenedUrl;

public class UrlShorternerDao {
	private static Connection connection = ApplicationDao.getConnectionToDatabase();
	private static final String DB_USE_STATEMENT = ApplicationDao.getDbUseStatement();

	// SQL statements, at top for easier access
	private static final String insertQuery = "INSERT INTO short_urls (slug, long_url, user_id) VALUES (?, ?, ?)";
	private static final String slugSelectQuery = "SELECT * FROM short_urls WHERE slug = ?";
	private static final String shortUrlSelectQuery = "SELECT short_urls.id, short_urls.slug, short_urls.long_url, count(short_url_clicks.ID) FROM short_urls "
			+ "LEFT JOIN short_url_clicks ON short_url_clicks.short_url_id = short_urls.id "
			+ "WHERE user_id = ? GROUP BY short_urls.id ORDER BY short_urls.id";
	private static final String clickInsertQuery = "INSERT INTO short_url_clicks (short_url_id) VALUES (?)";

	public void insertShortenedUrl(ShortenedUrl shortenedUrl) {
		try {
			Statement statement = connection.createStatement();
			String useDatabaseQuery = DB_USE_STATEMENT;
			statement.executeUpdate(useDatabaseQuery);

			PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
			preparedStatement.setString(1, shortenedUrl.getSlug());
			preparedStatement.setString(2, shortenedUrl.getLongUrl());
			preparedStatement.setInt(3, shortenedUrl.getUserId());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ShortenedUrl getShortenedUrlBySlug(String slug) {
		ShortenedUrl shortenedUrl = null;

		try {
			Statement statement = connection.createStatement();
			String useDatabaseQuery = DB_USE_STATEMENT;
			statement.executeUpdate(useDatabaseQuery);

			// create the query and run it
			PreparedStatement preparedStatement = connection.prepareStatement(slugSelectQuery);
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
		// List<ShortenedUrl> shortenedUrls = new ArrayList<>();

		// create a map list to store the data
		List<Map<String, String>> shortenedUrls = new ArrayList<>();

		try {
			Statement statement = connection.createStatement();
			String useDatabaseQuery = DB_USE_STATEMENT;
			statement.executeUpdate(useDatabaseQuery);

			PreparedStatement preparedStatement = connection.prepareStatement(shortUrlSelectQuery);
			preparedStatement.setInt(1, userId);

			System.out.println(shortUrlSelectQuery);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {

				Map<String, String> idData = new HashMap<>();
				idData.put("id", resultSet.getString("id"));
				idData.put("slug", resultSet.getString("slug"));
				idData.put("long_url", resultSet.getString("long_url"));
				idData.put("count", resultSet.getString("count(short_url_clicks.ID)"));

				shortenedUrls.add(idData);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return shortenedUrls;
	}

	public void registerClick(int shortUrlId) {
		try {
			Statement statement = connection.createStatement();
			String useDatabaseQuery = DB_USE_STATEMENT;
			statement.executeUpdate(useDatabaseQuery);

			PreparedStatement preparedStatement = connection.prepareStatement(clickInsertQuery);
			preparedStatement.setInt(1, shortUrlId);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
