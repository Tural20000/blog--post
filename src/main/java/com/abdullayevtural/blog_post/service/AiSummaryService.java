package com.abdullayevtural.blog_post.service;

import org.springframework.stereotype.Service;

@Service
public class AiSummaryService {

	public String generateSummary(String content) {
		try {
			if (content == null || content.trim().length() < 20) {
				return content;
			}

			String[] sentences = content.split("(?<=[.!?])\\s+");

			StringBuilder summary = new StringBuilder();

			int limit = Math.min(sentences.length, 2);
			for (int i = 0; i < limit; i++) {
				summary.append(sentences[i]).append(" ");
			}

			String result = summary.toString().trim();

			if (result.isEmpty()) {
				return content.length() > 100 ? content.substring(0, 100) + "..." : content;
			}

			return "[AI Smart Summary]: " + result;

		} catch (Exception e) {
			System.err.println("Xülasə xətası: " + e.getMessage());
			return content;
		}
	}
}