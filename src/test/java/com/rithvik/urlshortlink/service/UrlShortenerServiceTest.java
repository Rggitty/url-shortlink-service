package com.rithvik.urlshortlink.service;

import com.rithvik.urlshortlink.dto.ShortenUrlResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UrlShortenerServiceTest {

    private final UrlShortenerService service = new UrlShortenerService();

    @Test
    void shortenValidUrl() {
        ShortenUrlResponse response = service.shorten("https://example.com");

        assertNotNull(response.shortCode());
        assertEquals("https://example.com", response.originalUrl());
    }

    @Test
    void generatesUniqueCodes() {
        String first = service.shorten("https://a.com").shortCode();
        String second = service.shorten("https://b.com").shortCode();

        assertNotEquals(first, second);
    }

    @Test
    void invalidUrlThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                service.shorten("not-a-url")
        );
    }
}
