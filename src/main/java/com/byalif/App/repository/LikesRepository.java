package com.byalif.App.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.byalif.App.customQueries.groupLikeById;
import com.byalif.App.entity.Likes;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {

	public Likes findByEmail(String email);

	@Query(value = "SELECT * from likes WHERE likes.email = :email AND likes.post_id = :postId ", nativeQuery = true)
	public Likes findByEmailAndPostId(String email, Long postId);

	@Query(value = "SELECT posts.id, count(likes.id) AS amount FROM likes, posts " + "WHERE posts.id = likes.post_id "
			+ "GROUP BY posts.id", nativeQuery = true)
	public List<Object[]> getNumberOfLikes();

	@Query(value = "select posts.id as id, count(likes.id) as amount  FROM likes, posts "
			+ "WHERE posts.id = likes.post_id " + "GROUP BY posts.id", nativeQuery = true)
	public List<groupLikeById> getNumberOfLikes2();

	@Query(value = "SELECT * FROM posts, likes WHERE posts.id = likes.post_id AND posts.id = :postId", nativeQuery = true)
	public List<Likes> findLikesByPostId(Long postId);

	@Transactional
	@Modifying
	@Query(value = "Delete likes FROM likes, posts WHERE likes.post_id = posts.id AND likes.email = :email AND posts.id= :postId", nativeQuery = true)
	public void DeleteByEmail(String email, Long postId);

	@Query(value = "Delete likes FROM likes, posts WHERE likes.post_id = :postId", nativeQuery = true)
	public void DeletePosts(Long postId);

}
