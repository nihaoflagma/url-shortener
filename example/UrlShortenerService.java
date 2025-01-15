package org.example;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UrlShortenerService {
    
    private Map<String, LinkData> urlDatabase = new HashMap<>();

    
    private final int defaultClickLimit = 5; 
    private final int defaultExpirationHours = 24; 

    
    private static class LinkData {
        String longUrl;
        int clickCount;
        int clickLimit;
        LocalDateTime expirationTime;

        LinkData(String longUrl, int clickLimit, LocalDateTime expirationTime) {
            this.longUrl = longUrl;
            this.clickCount = 0;
            this.clickLimit = clickLimit;
            this.expirationTime = expirationTime;
        }
    }

    
    public String generateShortUrl(String longUrl, UUID userId) {
        String shortUrlPart = userId.toString() + "/" + encodeBase64(longUrl);
        String shortUrl = "http://short.url/" + shortUrlPart;

        
        LinkData linkData = new LinkData(
                longUrl,
                defaultClickLimit,
                LocalDateTime.now().plusHours(defaultExpirationHours)
        );

        
        urlDatabase.put(shortUrlPart, linkData);
        return shortUrl;
    }

    
    private String encodeBase64(String longUrl) {
        return Base64.getUrlEncoder().encodeToString(longUrl.getBytes());
    }

    
    public String getOriginalUrl(String shortUrl) {
        String shortUrlPart = shortUrl.replace("http://short.url/", "");
        LinkData linkData = urlDatabase.get(shortUrlPart);

        
        if (linkData == null) {
            return null;
        }

        if (linkData.expirationTime.isBefore(LocalDateTime.now())) {
            urlDatabase.remove(shortUrlPart); 
            return null;
        }

        if (linkData.clickCount >= linkData.clickLimit) {
            return null; 
        }

        linkData.clickCount++;
        return linkData.longUrl;
    }

    
    public boolean deleteShortUrl(String shortUrl) {
        String shortUrlPart = shortUrl.replace("http://short.url/", "");
        return urlDatabase.remove(shortUrlPart) != null;
    }

    
    public boolean updateClickLimit(String shortUrl, int newLimit) {
        String shortUrlPart = shortUrl.replace("http://short.url/", "");
        LinkData linkData = urlDatabase.get(shortUrlPart);

        if (linkData != null) {
            linkData.clickLimit = newLimit;
            return true;
        }
        return false;
    }
}
