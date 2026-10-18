package com.abdullayevtural.blog_post.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abdullayevtural.blog_post.entity.ReactionType;
import com.abdullayevtural.blog_post.service.LikeService;

@RestController
@RequestMapping("/api/reactions")
public class LikeController {
	private final LikeService likeService;

	public LikeController(LikeService likeService) {
		this.likeService = likeService;
	}

	@PostMapping("/posts/{postId}")
	public ResponseEntity<String> reactToPost(@PathVariable Long postId, @RequestParam ReactionType type,
			Authentication authentication) {
		String username = authentication.getName();
		String result = likeService.reactToPost(postId, type, username);
		return ResponseEntity.ok(result);
	}

	@PostMapping("/comments/{commentId}")
	public ResponseEntity<String> reactToComment(@PathVariable Long commentId, @RequestParam ReactionType type,
			Authentication authentication) {
		String username = authentication.getName();
		String result = likeService.reactToComment(commentId, type, username);
		return ResponseEntity.ok(result);
	}

}
