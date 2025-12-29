package com.rithvik.urlshortlink.service;

import com.rithvik.urlshortlink.dto.ShortenUrlResponse;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UrlShortenerService {

    private final AtomicLong counter = new AtomicLong(1);
    private final Map<String, String> store = new ConcurrentHashMap<>();

    public ShortenUrlResponse shorten(String originalUrl) {
        validateUrl(originalUrl);

        long id = counter.getAndIncrement();
        String code = Base62Encoder.encode(id);

        store.put(code, originalUrl);
        return new ShortenUrlResponse(code, originalUrl);
    }

    public String resolve(String code) {
        String url = store.get(code);
        if (url == null) {
            throw new IllegalArgumentException("Short code not found");
        }
        return url;
    }

    private void validateUrl(String url) {
        try {
            URI uri = URI.create(url);
            if (uri.getScheme() == null || uri.getHost() == null) {
                throw new IllegalArgumentException("Invalid URL format");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid URL format");
        }
    }
}
