package com.algonquin.urlshortener.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.algonquin.urlshortener.beans.User;

public class UserDao {
	private static Connection connection = ApplicationDao.getConnectionToDatabase();
	private static final String DB_USE_STATEMENT = ApplicationDao.getDbUseStatement();
	
	//SQL statements, at top for easier access
	private static final String insertQuery = "INSERT INTO users (email, password) VALUES (?, ?)";
	private static final String selectQuery = "SELECT * FROM users WHERE email = ?";
	
	public void insertUser(User user) {
		try {
			Statement statement = connection.createStatement();
			String useDatabaseQuery = DB_USE_STATEMENT;
			statement.executeUpdate(useDatabaseQuery);
			
			PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
			preparedStatement.setString(1, user.getEmail());
			preparedStatement.setString(2, user.getPassword());

			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public User getUserByEmail(String email) {
		User user = null;

		try {
			Statement statement = connection.createStatement();
			String useDatabaseQuery = DB_USE_STATEMENT;
			statement.executeUpdate(useDatabaseQuery);

			
			PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
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
		User user = null;

		try {
			Statement statement = connection.createStatement();
			String useDatabaseQuery = DB_USE_STATEMENT;
			statement.executeUpdate(useDatabaseQuery);

			PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
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

}
