package urlshortener.services;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import urlshortener.beans.ShortenedUrl;
import urlshortener.dao.ApplicationDao;

public class ShortenService {
	private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private static final int SHORT_URL_LENGTH = 6;

	public static String generateShortUrl(String originalUrl) {
		// db class
		ApplicationDao db = new ApplicationDao();
		// generate random string
		SecureRandom random = new SecureRandom();
		String shortUrl;

		do {
			StringBuilder sb = new StringBuilder();
			// randomly generate a string of length SHORT_URL_LENGTH
			for (int i = 0; i < SHORT_URL_LENGTH; i++) {
				int randomIndex = random.nextInt(CHARACTERS.length());
				char randomChar = CHARACTERS.charAt(randomIndex);
				sb.append(randomChar);
			}
			shortUrl = sb.toString();
			// db function to check if exists in the database
		} while (db.getShortenedUrlBySlug(shortUrl) != null);
		ShortenedUrl s = new ShortenedUrl();
		s.setSlug(shortUrl);
		s.setLongUrl(originalUrl);
		s.setUserId(-1);
		db.insertShortenedUrl(s);
		return shortUrl;
	}

	public static String getOriginalUrl(String shortSlug) {
		ApplicationDao db = new ApplicationDao();
		ShortenedUrl s = db.getShortenedUrlBySlug(shortSlug);
		if (s != null) {
			db.registerClick(s.getId());
			return s.getLongUrl();
		}
		return null;
	}
}
