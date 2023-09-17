package com.algonquin.urlshortener.services;

import com.algonquin.urlshortener.beans.User;
import com.algonquin.urlshortener.dao.UserDao;

public class UserService {
	public static void insertUser(String newuseremail, String newuserpassword) {
		UserDao dao = new UserDao();
		User signup = new User(newuseremail, newuserpassword);
		dao.insertUser(signup);
	}
	
	public static User loginUser(String useremail, String userpassword) {
		UserDao dao = new UserDao();
		User user = dao.getUserSignIn(useremail, userpassword);
		return user;
	}
}
