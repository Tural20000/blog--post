package com.abdullayevtural.blog_post.dto;

import com.abdullayevtural.blog_post.entity.ReactionType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ReactionUserResponse {
	private String username;
	private ReactionType reactionType;

}
