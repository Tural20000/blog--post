package com.abdullayevtural.blog_post.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PostReportResponse {
	private Long postId;
	private String title;
	private String content;
	private LocalDateTime createdAt;
	private long likeCount;
	private long dislikeCount;
	private List<ReactionUserResponse> reactions;
	private String imageUrl;
	private String aiSummary;

}
