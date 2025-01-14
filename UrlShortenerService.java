package org.example;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UrlShortenerService {
    // Хранение сокращенных и оригинальных URL
    private Map<String, String> urlDatabase = new HashMap<>();

    // Метод для генерации короткой ссылки
    public String generateShortUrl(String longUrl, UUID userId) {
        // Создаем уникальный идентификатор для каждой ссылки
        String shortUrlPart = userId.toString() + "/" + encodeBase64(longUrl);
        String shortUrl = "http://short.url/" + shortUrlPart;

        // Сохраняем в базе данных (симуляция базы данных)
        urlDatabase.put(shortUrlPart, longUrl);
        return shortUrl;
    }

    // Преобразование строки в Base64
    private String encodeBase64(String longUrl) {
        return Base64.getUrlEncoder().encodeToString(longUrl.getBytes());
    }

    // Получение оригинальной ссылки по сокращенной
    public String getOriginalUrl(String shortUrl) {
        // Извлекаем уникальную часть ссылки
        String shortUrlPart = shortUrl.replace("http://short.url/", "");
        return urlDatabase.get(shortUrlPart); // Возвращаем оригинальную ссылку из базы
    }
}
