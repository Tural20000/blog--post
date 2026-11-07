package com.abdullayevtural.blog_post.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.abdullayevtural.blog_post.dto.PostReportResponse;
import com.abdullayevtural.blog_post.dto.PostRequest;
import com.abdullayevtural.blog_post.dto.PostResponse;
import com.abdullayevtural.blog_post.dto.ReactionUserResponse;
import com.abdullayevtural.blog_post.entity.Like;
import com.abdullayevtural.blog_post.entity.Post;
import com.abdullayevtural.blog_post.entity.ReactionType;
import com.abdullayevtural.blog_post.entity.Role;
import com.abdullayevtural.blog_post.entity.User;
import com.abdullayevtural.blog_post.repository.LikeRepository;
import com.abdullayevtural.blog_post.repository.PostRepository;
import com.abdullayevtural.blog_post.repository.UserRepository;

@Service
public class PostService {
	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final LikeRepository likeRepository;
	private final FileStorageService fileStorageService; // YENİ ƏLAVƏ OLUNDU

	public PostService(PostRepository postRepository, UserRepository userRepository, LikeRepository likeRepository,
			FileStorageService fileStorageService) {
		this.postRepository = postRepository;
		this.userRepository = userRepository;
		this.likeRepository = likeRepository;
		this.fileStorageService = fileStorageService;
	}

	public PostResponse createPost(PostRequest request, String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("Istifadeci tapilmadi: " + username));
		Post post = new Post();
		post.setTitle(request.getTitle());
		post.setContent(request.getContent());
		post.setCreatedAt(LocalDateTime.now());
		post.setUser(user);

		Post savedPost = postRepository.save(post);
		return mapToResponse(savedPost);
	}

	public PostResponse createPostWithImage(String title, String content, MultipartFile file, String username) {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("Istifadeci tapilmadi: " + username));

		Post post = new Post();
		post.setTitle(title);
		post.setContent(content);
		post.setCreatedAt(LocalDateTime.now());
		post.setUser(user);

		if (file != null && !file.isEmpty()) {
			String fileName = fileStorageService.storeFile(file);
			post.setImageUrl("/uploads/" + fileName);
		}

		Post savedPost = postRepository.save(post);
		return mapToResponse(savedPost);
	}

	public List<PostResponse> getAllPost() {
		return postRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
	}

	public PostResponse getPostById(Long id) {
		Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post tapilmadi, ID: " + id));
		return mapToResponse(post);
	}

	private PostResponse mapToResponse(Post post) {
		long likeCount = likeRepository.countByPostIdAndReactionType(post.getId(), ReactionType.LIKE);
		long dislikeCount = likeRepository.countByPostIdAndReactionType(post.getId(), ReactionType.DISLIKE);

		return new PostResponse(post.getId(), post.getTitle(), post.getContent(), post.getCreatedAt(),
				post.getUser().getUsername(), likeCount, dislikeCount, post.getImageUrl());
	}

	public void deletePost(Long id, String username) {
		Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post tapilmadi, ID: " + id));

		User currentUser = userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("Istifadeci tapilmadi: " + username));

		boolean isOwner = post.getUser().getUsername().equals(username);
		boolean isAdmin = currentUser.getRole() == Role.ADMIN;

		if (!isOwner && !isAdmin) {
			throw new RuntimeException("Sizin bu postu silmek selahiyetiniz yoxdur!");
		}

		postRepository.delete(post);
	}

	public PostResponse updatePost(Long id, PostRequest request, String username) {
		Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post tapilmadi, ID: " + id));
		if (!post.getUser().getUsername().equals(username)) {
			throw new RuntimeException("Sizin bu postu yenilemek selahiyetiniz yoxdur!");
		}
		post.setTitle(request.getTitle());
		post.setContent(request.getContent());

		Post updatedPost = postRepository.save(post);
		return mapToResponse(updatedPost);
	}

	public List<PostReportResponse> getPostReportsByDateRange(String username, LocalDateTime startDate,
			LocalDateTime endDate) {

		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("İstifadəçi tapılmadı: " + username));

		List<Post> posts = postRepository.findByUserIdAndCreatedAtBetween(user.getId(), startDate, endDate);

		return posts.stream().map(post -> {

			List<Like> likes = likeRepository.findByPostId(post.getId());

			long likeCount = likes.stream().filter(l -> l.getReactionType() == ReactionType.LIKE).count();
			long dislikeCount = likes.stream().filter(l -> l.getReactionType() == ReactionType.DISLIKE).count();

			List<ReactionUserResponse> reactionUsers = likes.stream()
					.map(l -> new ReactionUserResponse(l.getUser().getUsername(), l.getReactionType()))
					.collect(Collectors.toList());

			return new PostReportResponse(post.getId(), post.getTitle(), post.getContent(), post.getCreatedAt(),
					likeCount, dislikeCount, reactionUsers, post.getImageUrl());
		}).collect(Collectors.toList());
	}

	public List<PostResponse> searchPosts(String keyword) {
		List<Post> posts = postRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword,
				keyword);
		return posts.stream().map(this::mapToResponse).collect(Collectors.toList());
	}
}