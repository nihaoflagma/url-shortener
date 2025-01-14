package org.example;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        // Пример работы с сервисом сокращения URL
        UrlShortenerService service = new UrlShortenerService();
        Scanner scanner = new Scanner(System.in);

        // Генерация уникального идентификатора для пользователя
        UUID userId = UUID.randomUUID();

        // Ввод длинной ссылки для сокращения
        System.out.print("Enter URL to shorten: ");
        String longUrl = scanner.nextLine();

        // Генерация сокращенной ссылки с UUID
        String shortUrl = service.generateShortUrl(longUrl, userId);
        System.out.println("Shortened URL: " + shortUrl);

        // Ожидание ввода сокращенной ссылки для перехода
        System.out.print("Enter shortened URL to visit: ");
        String userShortUrl = scanner.nextLine();

        // Если сокращенная ссылка не содержит "http://short.url/", добавляем его
        if (!userShortUrl.startsWith("http://short.url/")) {
            userShortUrl = "http://short.url/" + userShortUrl;
        }

        // Переход на оригинальный URL, используя Desktop API
        openInBrowser(userShortUrl, service);
    }

    // Метод для открытия ссылки в браузере
    public static void openInBrowser(String shortUrl, UrlShortenerService service) {
        try {
            // Разворачиваем короткую ссылку в оригинальную
            String longUrl = service.getOriginalUrl(shortUrl);

            // Проверяем, нашли ли мы оригинальный URL
            if (longUrl != null) {
                // Создаем URI из оригинальной ссылки
                URI uri = new URI(longUrl);

                // Проверка, поддерживает ли система использование Desktop
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    desktop.browse(uri);  // Открытие браузера с заданной ссылкой
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
