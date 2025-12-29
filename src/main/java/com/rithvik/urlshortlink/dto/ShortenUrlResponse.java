package com.rithvik.urlshortlink.dto;

public record ShortenUrlResponse(
        String shortCode,
        String originalUrl
) {
}
