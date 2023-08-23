package com.algonquin.urlshortener.services;

import java.security.SecureRandom;

import com.algonquin.urlshortener.beans.ShortenedUrl;
import com.algonquin.urlshortener.dao.ApplicationDao;

public class ShortenService {
	 private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	    private static final int SHORT_URL_LENGTH = 6;

	    public static String generateShortUrl(String originalUrl, int userid) {
	    	ApplicationDao db = new ApplicationDao();
	        SecureRandom random = new SecureRandom();
	        String shortUrl;

	        do {
	            StringBuilder sb = new StringBuilder();
	            for (int i = 0; i < SHORT_URL_LENGTH; i++) {
	                int randomIndex = random.nextInt(CHARACTERS.length());
	                char randomChar = CHARACTERS.charAt(randomIndex);
	                sb.append(randomChar);
	            }
	            shortUrl = sb.toString();
	        } while (db.getShortenedUrlBySlug(shortUrl) != null); 
	        ShortenedUrl s = new ShortenedUrl();
	        s.setSlug(shortUrl);
	        s.setLongUrl(originalUrl);
	        s.setUserId(userid);
	        db.insertShortenedUrl(s);
	        return shortUrl;
	    }

		public static String getOriginalUrl(String shortSlug) {
			ApplicationDao db = new ApplicationDao();
			ShortenedUrl s = db.getShortenedUrlBySlug(shortSlug);
			if (s!=null) {
				db.registerClick(s.getId());
				return s.getLongUrl();
			}
			return null;
		}
}
