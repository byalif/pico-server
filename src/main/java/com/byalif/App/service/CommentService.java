package com.byalif.App.service;

public interface CommentService {

	String addCommentLike(String email, Long commentId);

	String deleteCommentLike(String email, Long commentId);

}
