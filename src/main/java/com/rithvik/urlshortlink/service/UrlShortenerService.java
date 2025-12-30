package com.rithvik.urlshortlink.service;

import com.rithvik.urlshortlink.dto.ShortenUrlResponse;
import com.rithvik.urlshortlink.model.ShortLink;
import com.rithvik.urlshortlink.repo.ShortLinkRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;

@Service
public class UrlShortenerService {

    private final ShortLinkRepository repo;

    public UrlShortenerService(ShortLinkRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public ShortenUrlResponse shorten(String originalUrl) {
        validateUrl(originalUrl);

        // 1) Save to get an ID
        ShortLink row = new ShortLink(originalUrl);
        row = repo.save(row);

        // 2) Create a stable short code from the ID
        String code = Base62Encoder.encode(row.getId());

        // Optional collision check (rare, but safe)
        if (repo.existsByShortCode(code)) {
            throw new IllegalStateException("Short code collision (unexpected). Try again.");
        }

        // 3) Update row with short code
        row.setShortCode(code);
        repo.save(row);

        return new ShortenUrlResponse(code, originalUrl);
    }

    @Transactional(readOnly = true)
    public String resolve(String code) {
        return repo.findByShortCode(code)
                .map(ShortLink::getOriginalUrl)
                .orElseThrow(() -> new IllegalArgumentException("Short code not found"));
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
