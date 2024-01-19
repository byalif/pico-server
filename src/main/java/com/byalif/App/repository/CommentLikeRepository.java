package com.byalif.App.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.byalif.App.customQueries.groupLikeById;
import com.byalif.App.entity.CommentLike;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

	@Transactional
	@Modifying
	@Query(value = "DELETE comment_like FROM comments, comment_like Where comments.id= comment_like.comment_id AND comment_like.email = :email AND comments.id = :commentId", nativeQuery = true)
	void DeleteByEmailAndComId(String email, Long commentId);

	@Query(value = "SELECT * FROM comments, comment_like WHERE comments.id = comment_like.comment_id AND comment_like.email = :email AND comments.id = :commentId", nativeQuery = true)
	CommentLike findByEmailAndComId(String email, Long commentId);

	@Query(value = "select comments.id, count(comment_like.id) as amount " + "from comment_like, comments "
			+ "where comment_like.comment_id = comments.id " + "group by comments.id "
			+ "order by amount ", nativeQuery = true)
	List<groupLikeById> getCommentLikes();
}
