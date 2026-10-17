package com.abdullayevtural.blog_post.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CommentResponse {
	private Long id;

	private String content;

	private LocalDateTime createdAt;

	private String authorUsername;

}
