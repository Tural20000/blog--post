package com.abdullayevtural.blog_post.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AiSummaryService {

	@Value("${gemini.api.key}")
	private String apiKey;

	@Value("${gemini.api.url}")
	private String apiUrl;

	private final ObjectMapper objectMapper = new ObjectMapper();

	public String generateSummary(String content) {
		try {
			if (content == null || content.trim().length() < 20) {
				return content;
			}

			RestTemplate restTemplate = new RestTemplate();
			String url = apiUrl + apiKey;

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			String promptText = "Aşağıdakı bloq postunun mətnini oxu və qısa, peşəkar 1-2 cümləlik xülasəsini yaz: \n\n "
					+ content;

			String jsonRequestBody = String.format("{\"contents\":[{\"parts\":[{\"text\":%s}]}]}",
					objectMapper.writeValueAsString(promptText));

			HttpEntity<String> entity = new HttpEntity<>(jsonRequestBody, headers);

			ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

			if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
				JsonNode rootNode = objectMapper.readTree(response.getBody());
				JsonNode candidatesNode = rootNode.path("candidates");

				if (candidatesNode.isArray() && !candidatesNode.isEmpty()) {
					JsonNode textNode = candidatesNode.get(0).path("content").path("parts").get(0).path("text");

					if (!textNode.isMissingNode()) {
						return textNode.asText();
					}
				}
			}
			return "Xülasə yaradıla bilmədi.";

		} catch (Exception e) {
			System.err.println("AI Xülasə xətası: " + e.getMessage());
			return "Süni intellekt xidməti müvəqqəti əlçatan deyil.";
		}
	}
}