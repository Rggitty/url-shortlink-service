package com.rithvik.urlshortlink.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rithvik.urlshortlink.dto.ShortenUrlRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UrlShortenerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shortenUrlReturnsCreatedResponse() throws Exception {
        ShortenUrlRequest request = new ShortenUrlRequest("https://example.com");

        mockMvc.perform(post("/api/v1/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.shortCode").exists())
                .andExpect(jsonPath("$.originalUrl").value("https://example.com"));
    }

    @Test
    void redirectReturns302AndLocationHeader() throws Exception {
        // create a short link first
        String body = objectMapper.writeValueAsString(new ShortenUrlRequest("https://example.com"));

        String response = mockMvc.perform(post("/api/v1/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String code = objectMapper.readTree(response).get("shortCode").asText();

        // verify redirect endpoint
        mockMvc.perform(get("/api/v1/r/" + code))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "https://example.com"));
    }

    @Test
    void redirectUnknownCodeReturns404() throws Exception {
        mockMvc.perform(get("/api/v1/r/doesNotExist"))
                .andExpect(status().isNotFound());
    }
}
