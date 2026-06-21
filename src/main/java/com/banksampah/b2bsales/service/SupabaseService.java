package com.banksampah.b2bsales.service;

import com.banksampah.b2bsales.config.EnvConfig;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SupabaseService {
    private final String baseUrl;
    private final String apiKey;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public SupabaseService() {
        this.baseUrl = EnvConfig.get("SUPABASE_URL") + "/rest/v1";
        this.apiKey = EnvConfig.get("SUPABASE_KEY");
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    // Generic Fetch Data dari tabel Supabase via REST API (GET)
    public List<Map<String, Object>> fetchData(String tableName, String selectParams) throws Exception {
        String urlStr = baseUrl + "/" + tableName + "?" + selectParams;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlStr))
                .header("apikey", apiKey)
                .header("Authorization", "Bearer " + apiKey)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            return objectMapper.readValue(response.body(), new TypeReference<List<Map<String, Object>>>() {});
        } else {
            throw new RuntimeException("Gagal mengambil data: " + response.body());
        }
    }

    // Generic Insert Data ke Supabase (POST)
    public void insertData(String tableName, Map<String, Object> dataPayload) throws Exception {
        String jsonPayload = objectMapper.writeValueAsString(dataPayload);
        String urlStr = baseUrl + "/" + tableName;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlStr))
                .header("apikey", apiKey)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .header("Prefer", "return=minimal")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new RuntimeException("Gagal menyimpan data ke Supabase: " + response.body());
        }
    }
}
