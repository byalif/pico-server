package com.byalif.App.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.byalif.App.entity.CommentLike;
import com.byalif.App.entity.Comments;
import com.byalif.App.entity.User;
import com.byalif.App.repository.CommentLikeRepository;
import com.byalif.App.repository.CommentRepository;
import com.byalif.App.repository.LikesRepository;
import com.byalif.App.repository.UploadRepository;
import com.byalif.App.repository.UserRepository;

@Service
public class commentServiceImpl implements CommentService {
	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	UploadRepository uploadRepository;

	@Autowired
	CommentRepository commentRepository;

	@Autowired
	LikesRepository likesRepository;

	@Autowired
	CommentLikeRepository commentLikeRepository;

	@Override
	public String addCommentLike(String email, Long commentId) {
		User user = userRepository.findByEmail(email);
		if (user == null) {
			return "LOGIN";
		}
		Optional<Comments> comment = commentRepository.findById(commentId);
		if (!comment.isPresent()) {
			return "NOT_FOUND";
		}
		CommentLike comLike = commentLikeRepository.findByEmailAndComId(email, commentId);
		if (comLike != null) {
			return "LIKED_ALREADY";
		}
		CommentLike newLike = CommentLike.builder().email(email).theComment(comment.get()).build();
		comment.get().addComLike(newLike);
		commentLikeRepository.save(newLike);
		return "VALID";
	}

	@Override
	public String deleteCommentLike(String email, Long commentId) {
		User user = userRepository.findByEmail(email);
		if (user == null) {
			return "LOGIN";
		}
		Optional<Comments> comment = commentRepository.findById(commentId);
		if (!comment.isPresent()) {
			return "NOT_FOUND";
		}
		CommentLike comLike = commentLikeRepository.findByEmailAndComId(email, commentId);
		if (comLike == null) {
			return "UNLIKED_ALREADY";
		}
		comment.get().removeComLike(comLike);
		commentLikeRepository.DeleteByEmailAndComId(email, commentId);
		return "UNLIKED";
	}

}
