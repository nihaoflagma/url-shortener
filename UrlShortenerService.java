package org.example;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UrlShortenerService {
    
    private Map<String, String> urlDatabase = new HashMap<>();

    
    public String generateShortUrl(String longUrl, UUID userId) {
        
        String shortUrlPart = userId.toString() + "/" + encodeBase64(longUrl);
        String shortUrl = "http://short.url/" + shortUrlPart;

        
        urlDatabase.put(shortUrlPart, longUrl);
        return shortUrl;
    }

    
    private String encodeBase64(String longUrl) {
        return Base64.getUrlEncoder().encodeToString(longUrl.getBytes());
    }

    
    public String getOriginalUrl(String shortUrl) {
        
        String shortUrlPart = shortUrl.replace("http://short.url/", "");
        return urlDatabase.get(shortUrlPart); 
    }
}
