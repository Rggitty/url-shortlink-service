package com.rithvik.urlshortlink.service;

import com.rithvik.urlshortlink.model.ShortLink;
import com.rithvik.urlshortlink.repo.ShortLinkRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UrlShortenerServiceTest {

    @Test
    void shortenReturnsCodeAndPersists() {
        ShortLinkRepository repo = mock(ShortLinkRepository.class);

        // When saving the first time, pretend DB assigned id=1
        when(repo.save(any(ShortLink.class))).thenAnswer(inv -> {
            ShortLink s = inv.getArgument(0);
            // simulate DB generated id only if not set
            try {
                var idField = ShortLink.class.getDeclaredField("id");
                idField.setAccessible(true);
                if (idField.get(s) == null) idField.set(s, 1L);
            } catch (Exception ignored) {}
            return s;
        });

        when(repo.existsByShortCode(anyString())).thenReturn(false);

        UrlShortenerService service = new UrlShortenerService(repo);

        var res = service.shorten("https://example.com");

        assertNotNull(res);
        assertNotNull(res.shortCode());
        assertEquals("https://example.com", res.originalUrl());

        verify(repo, atLeastOnce()).save(any(ShortLink.class));
    }

    @Test
    void shortenRejectsBadUrl() {
        ShortLinkRepository repo = mock(ShortLinkRepository.class);
        UrlShortenerService service = new UrlShortenerService(repo);

        assertThrows(IllegalArgumentException.class, () -> service.shorten("not-a-url"));
        verifyNoInteractions(repo);
    }
}
