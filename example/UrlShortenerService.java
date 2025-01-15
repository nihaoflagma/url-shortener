package org.example;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UrlShortenerService {
    // Хранение сокращенных и оригинальных URL
    private Map<String, LinkData> urlDatabase = new HashMap<>();

    // Параметры по умолчанию
    private final int defaultClickLimit = 5; // Лимит переходов
    private final int defaultExpirationHours = 24; // Срок жизни в часах

    // Класс для хранения данных ссылки
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

    // Метод для генерации короткой ссылки
    public String generateShortUrl(String longUrl, UUID userId) {
        String shortUrlPart = userId.toString() + "/" + encodeBase64(longUrl);
        String shortUrl = "http://short.url/" + shortUrlPart;

        // Устанавливаем параметры ссылки
        LinkData linkData = new LinkData(
                longUrl,
                defaultClickLimit,
                LocalDateTime.now().plusHours(defaultExpirationHours)
        );

        // Сохраняем в базе данных
        urlDatabase.put(shortUrlPart, linkData);
        return shortUrl;
    }

    // Преобразование строки в Base64
    private String encodeBase64(String longUrl) {
        return Base64.getUrlEncoder().encodeToString(longUrl.getBytes());
    }

    // Получение оригинальной ссылки по сокращенной
    public String getOriginalUrl(String shortUrl) {
        String shortUrlPart = shortUrl.replace("http://short.url/", "");
        LinkData linkData = urlDatabase.get(shortUrlPart);

        // Проверяем, существует ли ссылка и не истек ли срок действия
        if (linkData == null) {
            return null;
        }

        if (linkData.expirationTime.isBefore(LocalDateTime.now())) {
            urlDatabase.remove(shortUrlPart); // Удаляем устаревшую ссылку
            return null;
        }

        if (linkData.clickCount >= linkData.clickLimit) {
            return null; // Лимит переходов исчерпан
        }

        linkData.clickCount++;
        return linkData.longUrl;
    }

    // Метод для удаления ссылки
    public boolean deleteShortUrl(String shortUrl) {
        String shortUrlPart = shortUrl.replace("http://short.url/", "");
        return urlDatabase.remove(shortUrlPart) != null;
    }

    // Метод для изменения лимита переходов
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
