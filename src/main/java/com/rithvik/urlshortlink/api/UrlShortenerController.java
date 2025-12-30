package com.rithvik.urlshortlink.api;

import com.rithvik.urlshortlink.dto.ShortenUrlRequest;
import com.rithvik.urlshortlink.dto.ShortenUrlResponse;
import com.rithvik.urlshortlink.service.UrlShortenerService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class UrlShortenerController {

    private final UrlShortenerService service;

    public UrlShortenerController(UrlShortenerService service) {
        this.service = service;
    }

    @PostMapping("/shorten")
    @ResponseStatus(HttpStatus.CREATED)
    public ShortenUrlResponse shorten(@RequestBody ShortenUrlRequest request) {
        return service.shorten(request.originalUrl());
    }

    @GetMapping("/r/{code}")
    public ResponseEntity<Void> redirect(@PathVariable String code) {
        try {
            String target = service.resolve(code);
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header(HttpHeaders.LOCATION, target)
                    .build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
