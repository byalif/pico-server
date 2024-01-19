package com.byalif.App.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.byalif.App.entity.Likes;
import com.byalif.App.entity.User;
import com.byalif.App.model.FeedbackModel;
import com.byalif.App.model.Login;
import com.byalif.App.model.Upload;
import com.byalif.App.model.registerModel;

public interface UserService {

	String registerUser(registerModel user);

	String findByEmail(Login user);

	String postPicture(Upload upload);

	String addComment(FeedbackModel feedback, Long postId);

	String addLike(FeedbackModel feedback, Long postId);

	String deleteComment(Long commentId, Long postId);

	int sizeOfLikes(Long postId);

	int numOfComments(Long postId);

	ResponseEntity<?> addLikes(FeedbackModel feedback, Long postId);

	List<Object[]> getLikes();

	User findUserByPostId(Long postId);

	List<Likes> findLikesByPostId(Long postId);

	String DeleteLikeByEmail(FeedbackModel feedback, Long postId);

	String followUser(Long userId, String email);

	String unfollowUser(Long userId, String email);

}
