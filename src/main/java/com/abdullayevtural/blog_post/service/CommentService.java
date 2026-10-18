package com.abdullayevtural.blog_post.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.abdullayevtural.blog_post.dto.CommentRequest;
import com.abdullayevtural.blog_post.dto.CommentResponse;
import com.abdullayevtural.blog_post.entity.Comment;
import com.abdullayevtural.blog_post.entity.Post;
import com.abdullayevtural.blog_post.entity.ReactionType;
import com.abdullayevtural.blog_post.entity.User;
import com.abdullayevtural.blog_post.repository.CommentRepository;
import com.abdullayevtural.blog_post.repository.LikeRepository;
import com.abdullayevtural.blog_post.repository.PostRepository;
import com.abdullayevtural.blog_post.repository.UserRepository;

@Service
public class CommentService {
	private final CommentRepository commentRepository;
	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final LikeRepository likeRepository;

	public CommentService(CommentRepository commentRepository, PostRepository postRepository,
			UserRepository userRepository, LikeRepository likeRepository) {
		this.commentRepository = commentRepository;
		this.postRepository = postRepository;
		this.userRepository = userRepository;
		this.likeRepository = likeRepository;
	}

	public CommentResponse addComment(Long postId, CommentRequest request, String username) {
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new RuntimeException("Komment yazilacaq post tapilmadi, ID: " + postId));
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("Istifadeci tapilmadi: " + username));
		Comment comment = new Comment();
		comment.setContent(request.getContent());
		comment.setCreatedAt(LocalDateTime.now());
		comment.setPost(post);
		comment.setUser(user);
		Comment savedComment = commentRepository.save(comment);
		return mapToResponse(savedComment);
	}

	private CommentResponse mapToResponse(Comment comment) {
		long likeCount = likeRepository.countByCommentIdAndReactionType(comment.getId(), ReactionType.LIKE);
		long dislikeCount = likeRepository.countByCommentIdAndReactionType(comment.getId(), ReactionType.DISLIKE);

		return new CommentResponse(comment.getId(), comment.getContent(), comment.getCreatedAt(),
				comment.getUser().getUsername(), likeCount, dislikeCount);
	}

	public Page<CommentResponse> getCommentsByPostId(Long postId, Pageable pageable) {
		if (!postRepository.existsById(postId)) {
			throw new RuntimeException("Post tapilmadi, ID: " + postId);
		}
		Page<Comment> commentPage = commentRepository.findByPostId(postId, pageable);
		return commentPage.map(this::mapToResponse);
	}
}