package com.byalif.App.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.byalif.App.entity.Following;

@Repository
public interface FollowingRepository extends JpaRepository<Following, Long> {

	@Query(value = "Select * from following,user where user.id = following.user_id and "
			+ "user.id= :userId and following.email = :email", nativeQuery = true)
	Following findByIdAndEmail(Long userId, String email);

	@Transactional
	@Modifying
	@Query(value = "DELETE following from following,user where user.id = following.user_id and "
			+ "user.id= :userId and following.email = :email", nativeQuery = true)
	void findByIdAndEmailDelete(Long userId, String email);

	@Query(value = "select * from following where following.user_id = :userId", nativeQuery = true)
	List<Following> findAllByUserId(Long userId);
}
