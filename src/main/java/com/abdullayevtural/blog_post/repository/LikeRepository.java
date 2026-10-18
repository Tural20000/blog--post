package com.abdullayevtural.blog_post.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.abdullayevtural.blog_post.entity.Like;
import com.abdullayevtural.blog_post.entity.ReactionType;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
	Optional<Like> findByUserIdAndPostId(Long userId, Long postId);

	Optional<Like> findByUserIdAndCommentId(Long userId, Long commentId);

	long countByPostIdAndReactionType(Long postId, ReactionType reactionType);

	long countByCommentIdAndReactionType(Long commentId, ReactionType reactionType);

}
