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

        while (true) {
            System.out.println("Select an option:");
            System.out.println("1. Shorten a URL");
            System.out.println("2. Visit a shortened URL");
            System.out.println("3. Delete a shortened URL");
            System.out.println("4. Update click limit for a URL");
            System.out.println("5. Exit");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    shortenUrl(service, scanner);
                    break;
                case 2:
                    visitUrl(service, scanner);
                    break;
                case 3:
                    deleteUrl(service, scanner);
                    break;
                case 4:
                    updateClickLimit(service, scanner);
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void shortenUrl(UrlShortenerService service, Scanner scanner) {
        UUID userId = UUID.randomUUID();
        System.out.print("Enter URL to shorten: ");
        String longUrl = scanner.nextLine();
        String shortUrl = service.generateShortUrl(longUrl, userId);
        System.out.println("Shortened URL: " + shortUrl);
    }

    private static void visitUrl(UrlShortenerService service, Scanner scanner) {
        System.out.print("Enter shortened URL to visit: ");
        String userShortUrl = scanner.nextLine();

        if (!userShortUrl.startsWith("http://short.url/")) {
            userShortUrl = "http://short.url/" + userShortUrl;
        }

        try {
            String longUrl = service.getOriginalUrl(userShortUrl);

            if (longUrl != null) {
                URI uri = new URI(longUrl);
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().browse(uri);
                } else {
                    System.out.println("Desktop is not supported on this system.");
                }
            } else {
                System.out.println("Error: Short URL not found or limit exceeded.");
            }
        } catch (IOException | URISyntaxException e) {
            System.out.println("Error opening URL: " + e.getMessage());
        }
    }

    private static void deleteUrl(UrlShortenerService service, Scanner scanner) {
        System.out.print("Enter shortened URL to delete: ");
        String shortUrl = scanner.nextLine();

        if (service.deleteShortUrl(shortUrl)) {
            System.out.println("URL deleted successfully.");
        } else {
            System.out.println("Error: URL not found.");
        }
    }

    private static void updateClickLimit(UrlShortenerService service, Scanner scanner) {
        System.out.print("Enter shortened URL to update: ");
        String shortUrl = scanner.nextLine();
        System.out.print("Enter new click limit: ");
        int newLimit = Integer.parseInt(scanner.nextLine());

        if (service.updateClickLimit(shortUrl, newLimit)) {
            System.out.println("Click limit updated successfully.");
        } else {
            System.out.println("Error: URL not found.");
        }
    }
}