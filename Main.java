package org.example;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        
        UrlShortenerService service = new UrlShortenerService();
        Scanner scanner = new Scanner(System.in);

        
        UUID userId = UUID.randomUUID();

        
        System.out.print("Enter URL to shorten: ");
        String longUrl = scanner.nextLine();

        
        String shortUrl = service.generateShortUrl(longUrl, userId);
        System.out.println("Shortened URL: " + shortUrl);

        
        System.out.print("Enter shortened URL to visit: ");
        String userShortUrl = scanner.nextLine();

        
        if (!userShortUrl.startsWith("http://short.url/")) {
            userShortUrl = "http://short.url/" + userShortUrl;
        }

        
        openInBrowser(userShortUrl, service);
    }

    
    public static void openInBrowser(String shortUrl, UrlShortenerService service) {
        try {
            
            String longUrl = service.getOriginalUrl(shortUrl);

            
            if (longUrl != null) {
                
                URI uri = new URI(longUrl);

                
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    desktop.browse(uri);  
                } else {
                    System.out.println("Desktop is not supported on this system.");
                }
            } else {
                System.out.println("Error: Short URL not found.");
            }
        } catch (IOException | URISyntaxException e) {
            System.out.println("Error opening URL in browser: " + e.getMessage());
        }
    }
}
