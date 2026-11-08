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

	public boolean isContentClean(String content) {
		if (content == null || content.trim().isEmpty()) {
			return false;
		}

		String lowerContent = content.toLowerCase();
		String[] bannedWords = { "spam", "təhqir", "pis söz", "hack", "badword" };
		for (String word : bannedWords) {
			if (lowerContent.contains(word)) {
				return false;
			}
		}
		return true;
	}

	public String generateSeoDescription(String content, String title) {
		if (content == null || content.trim().isEmpty()) {
			return title;
		}

		String cleanContent = content.replaceAll("\\s+", " ").trim();
		if (cleanContent.length() > 140) {
			cleanContent = cleanContent.substring(0, 140) + "...";
		}

		return "Read about " + title + " - " + cleanContent;
	}

	public String generateTags(String content, String title) {
		if (content == null) {
			return "General";
		}

		String combined = (title + " " + content).toLowerCase();
		StringBuilder tags = new StringBuilder();

		if (combined.contains("java") || combined.contains("spring") || combined.contains("backend")) {
			tags.append("#Java #SpringBoot ");
		}
		if (combined.contains("history") || combined.contains("boston") || combined.contains("revolution")) {
			tags.append("#History #Travel ");
		}
		if (combined.contains("database") || combined.contains("sql") || combined.contains("mysql")) {
			tags.append("#Database #SQL ");
		}
		if (combined.contains("ai") || combined.contains("artificial") || combined.contains("smart")) {
			tags.append("#AI #Technology ");
		}

		if (tags.length() == 0) {
			tags.append("#Blog #General");
		}

		return tags.toString().trim();
	}
}