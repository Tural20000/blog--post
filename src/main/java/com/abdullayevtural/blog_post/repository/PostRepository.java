package com.abdullayevtural.blog_post.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.abdullayevtural.blog_post.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
	List<Post> findByUserId(Long id);

}
