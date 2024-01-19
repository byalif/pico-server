package com.byalif.App.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.byalif.App.entity.Follower;

@Repository
public interface FollowerRepository extends JpaRepository<Follower, Long> {

	@Query(value = "Select * from follower,user where user.id = follower.user_id and "
			+ "user.id= :userId and follower.email = :email", nativeQuery = true)
	Follower findByIdAndEmail(Long userId, String email);

	@Transactional
	@Modifying
	@Query(value = "Delete follower from follower,user where user.id = follower.user_id and "
			+ "user.id= :userId and follower.email = :email", nativeQuery = true)
	void findByIdAndEmailDelete(Long userId, String email);

}
