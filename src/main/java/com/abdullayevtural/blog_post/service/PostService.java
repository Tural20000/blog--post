package com.abdullayevtural.blog_post.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.abdullayevtural.blog_post.dto.PostRequest;
import com.abdullayevtural.blog_post.dto.PostResponse;
import com.abdullayevtural.blog_post.entity.Post;
import com.abdullayevtural.blog_post.entity.ReactionType;
import com.abdullayevtural.blog_post.entity.User;
import com.abdullayevtural.blog_post.repository.LikeRepository;
import com.abdullayevtural.blog_post.repository.PostRepository;
import com.abdullayevtural.blog_post.repository.UserRepository;

@Service
public class PostService {
	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final LikeRepository likeRepository;

	public PostService(PostRepository postRepository, UserRepository userRepository, LikeRepository likeRepository) {
		this.postRepository = postRepository;
		this.userRepository = userRepository;
		this.likeRepository = likeRepository;
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
				post.getUser().getUsername(), likeCount, dislikeCount);
	}

	public void deletePost(Long id, String username) {
		Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post tapilmadi, ID: " + id));

		if (!post.getUser().getUsername().equals(username)) {
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
}