package com.example.demo.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TranslationUtil {

    // 번역 요청 데이터 클래스
    static class TranslationRequest {
        public String original_text;
        public String target_language;

        public TranslationRequest(String originalText, String targetLanguage) {
            this.original_text = originalText;
            this.target_language = targetLanguage;
        }
    }

    // 번역 응답 데이터 클래스
    static class TranslationResponse {
        public String translated_text;
    }

    public static String sendTranslateHttp(String originalText, String targetLang) {
        ObjectMapper objectMapper = new ObjectMapper();
        HttpClient client = HttpClient.newHttpClient();

        try {
            // 요청 데이터 생성
            TranslationRequest requestData = new TranslationRequest(originalText, targetLang);
            String requestBody = objectMapper.writeValueAsString(requestData);

            // HTTP POST 요청 생성
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8000/api/translate"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            // 요청 보내기
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // 응답 처리
            if (response.statusCode() == 200) {
                JsonNode responseBody = objectMapper.readTree(response.body());
                return responseBody.get("translated_text").asText();
            } else {
                System.err.println("번역 요청 실패. 상태 코드: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return "";
    }
}
