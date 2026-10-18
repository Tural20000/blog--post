package com.abdullayevtural.blog_post.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abdullayevtural.blog_post.entity.Comment;
import com.abdullayevtural.blog_post.entity.Like;
import com.abdullayevtural.blog_post.entity.Post;
import com.abdullayevtural.blog_post.entity.ReactionType;
import com.abdullayevtural.blog_post.entity.User;
import com.abdullayevtural.blog_post.repository.CommentRepository;
import com.abdullayevtural.blog_post.repository.LikeRepository;
import com.abdullayevtural.blog_post.repository.PostRepository;
import com.abdullayevtural.blog_post.repository.UserRepository;

@Service
public class LikeService {

	private final LikeRepository likeRepository;
	private final PostRepository postRepository;
	private final CommentRepository commentRepository;
	private final UserRepository userRepository;

	public LikeService(LikeRepository likeRepository, PostRepository postRepository,
			CommentRepository commentRepository, UserRepository userRepository) {
		this.likeRepository = likeRepository;
		this.postRepository = postRepository;
		this.commentRepository = commentRepository;
		this.userRepository = userRepository;
	}

	@Transactional
	public String reactToPost(Long postId, ReactionType reactionType, String username) {

		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new RuntimeException("Post tapılmadı, ID: " + postId));

		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("İstifadəçi tapılmadı: " + username));

		var existingLike = likeRepository.findByUserIdAndPostId(user.getId(), postId);

		if (existingLike.isPresent()) {
			Like like = existingLike.get();
			if (like.getReactionType() == reactionType) {
				likeRepository.delete(like);
				return "Reaksiya uğurla silindi (geri götürüldü).";
			} else {
				like.setReactionType(reactionType);
				likeRepository.save(like);
				return "Reaksiya yeniləndi: " + reactionType;
			}
		} else {
			Like like = new Like();
			like.setReactionType(reactionType);
			like.setUser(user);
			like.setPost(post);
			likeRepository.save(like);
			return "Uğurla əlavə olundu: " + reactionType;
		}
	}

	@Transactional
	public String reactToComment(Long commentId, ReactionType reactionType, String username) {

		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new RuntimeException("Komment tapılmadı, ID: " + commentId));

		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("İstifadəçi tapılmadı: " + username));

		var existingLike = likeRepository.findByUserIdAndCommentId(user.getId(), commentId);

		if (existingLike.isPresent()) {
			Like like = existingLike.get();
			if (like.getReactionType() == reactionType) {
				likeRepository.delete(like);
				return "Komment reaksiyası silindi.";
			} else {
				like.setReactionType(reactionType);
				likeRepository.save(like);
				return "Komment reaksiyası dəyişdirildi: " + reactionType;
			}
		} else {
			Like like = new Like();
			like.setReactionType(reactionType);
			like.setUser(user);
			like.setComment(comment);
			likeRepository.save(like);
			return "Kommentə reaksiya əlavə olundu: " + reactionType;
		}
	}
}