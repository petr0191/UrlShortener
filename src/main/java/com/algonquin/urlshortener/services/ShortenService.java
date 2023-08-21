package com.algonquin.urlshortener.services;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ShortenService {
	 private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	    private static final int SHORT_URL_LENGTH = 6;

	    private static Set<String> usedUrls = new HashSet<>();
	    private static Map<String, String> urlMap = new HashMap<>();

	    public static String generateShortUrl(String originalUrl) {
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
	        } while (usedUrls.contains(shortUrl)); 

	        usedUrls.add(shortUrl);
	        urlMap.put(shortUrl, originalUrl);
	        return shortUrl;
	    }

		public static String getOriginalUrl(String shortSlug) {
			return urlMap.getOrDefault(shortSlug, null);
		}
}
