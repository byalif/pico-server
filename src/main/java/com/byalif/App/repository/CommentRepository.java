package com.byalif.App.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.byalif.App.entity.Comments;

@Repository
public interface CommentRepository extends JpaRepository<Comments, Long> {

	Comments findByEmail(String email);

	@Query(value = "SELECT * from comments WHERE comments.post_id = :postId", nativeQuery = true)
	List<Comments> findByPostId(Long postId);

	@Query(value = "Delete comments FROM posts,comments WHERE comments.post_id = :postId", nativeQuery = true)
	public void DeletePosts(Long postId);
}
