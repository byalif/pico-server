package com.byalif.App.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.byalif.App.entity.Posts;

@Repository
public interface UploadRepository extends JpaRepository<Posts, Long> {

	@Transactional
	@Modifying
	@Query(value = "Delete likes FROM likes, posts WHERE likes.post_id = posts.id AND likes.email = :email AND posts.id= :postId", nativeQuery = true)
	public void DeleteByEmail(String email, Long postId);

	@Query(value = "Select * from posts,user where user.id = posts.user_id and "
			+ "user.email = :email", nativeQuery = true)
	public List<Posts> findPostsByUserEmail(String email);
}
