package com.rithvik.urlshortlink.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rithvik.urlshortlink.dto.ShortenUrlRequest;
import com.rithvik.urlshortlink.dto.ShortenUrlResponse;
import com.rithvik.urlshortlink.service.UrlShortenerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UrlShortenerController.class)
class UrlShortenerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UrlShortenerService service;

    @Test
    void shortenUrlReturnsCreatedResponse() throws Exception {
        when(service.shorten("https://example.com"))
                .thenReturn(new ShortenUrlResponse("1", "https://example.com"));

        ShortenUrlRequest request = new ShortenUrlRequest("https://example.com");

        mockMvc.perform(post("/api/v1/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.shortCode").value("1"))
                .andExpect(jsonPath("$.originalUrl").value("https://example.com"));
    }

    @Test
    void redirectReturns302AndLocationHeader() throws Exception {
        when(service.resolve("abc")).thenReturn("https://example.com");

        mockMvc.perform(get("/api/v1/r/abc"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "https://example.com"));
    }

    @Test
    void redirectUnknownCodeReturns404() throws Exception {
        when(service.resolve("doesNotExist"))
                .thenThrow(new IllegalArgumentException("Short code not found"));

        mockMvc.perform(get("/api/v1/r/doesNotExist"))
                .andExpect(status().isNotFound());
    }
}
