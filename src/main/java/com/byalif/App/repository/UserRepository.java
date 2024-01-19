package com.byalif.App.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.byalif.App.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);

	@Query(value = "Select * from user, posts WHERE user.id = posts.user_id AND posts.id = :postId", nativeQuery = true)
	User findUserByPostId(Long postId);

}
