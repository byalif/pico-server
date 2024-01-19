package com.byalif.App.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.byalif.App.customQueries.groupLikeById;
import com.byalif.App.entity.Comments;
import com.byalif.App.entity.Following;
import com.byalif.App.entity.Likes;
import com.byalif.App.entity.Posts;
import com.byalif.App.entity.User;
import com.byalif.App.message.Length;
import com.byalif.App.message.Message;
import com.byalif.App.model.FeedbackModel;
import com.byalif.App.repository.CommentLikeRepository;
import com.byalif.App.repository.CommentRepository;
import com.byalif.App.repository.FollowingRepository;
import com.byalif.App.repository.LikesRepository;
import com.byalif.App.repository.UploadRepository;
import com.byalif.App.repository.UserRepository;
import com.byalif.App.service.CommentService;
import com.byalif.App.service.UserService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PostController {

	@Autowired
	UploadRepository uploadRepository;

	@Autowired
	LikesRepository likesRepository;

	@Autowired
	CommentRepository commentRepository;

	@Autowired
	CommentLikeRepository commentLikeRepository;

	@Autowired
	FollowingRepository followingRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserService userService;

	@Autowired
	CommentService commentService;

	@GetMapping("/posts")
	@CrossOrigin
	public List<Posts> getPosts() {
		return uploadRepository.findAll();
	}

	@GetMapping("/users")
	@CrossOrigin
	public List<User> getUsers() {
		return userRepository.findAll();
	}

	@GetMapping("/userPosts/{email}")
	@CrossOrigin
	public List<Posts> getPostsbyUser(@PathVariable String email) {
		return uploadRepository.findPostsByUserEmail(email);
	}

	@GetMapping("/userByemail/{email}")
	@CrossOrigin
	public User getuser(@PathVariable String email) {
		return userRepository.findByEmail(email);
	}

	@PutMapping("/like/{postId}")
	@CrossOrigin()
	public ResponseEntity<?> addLike(@PathVariable Long postId, @RequestBody FeedbackModel feedback) {
		return userService.addLikes(feedback, postId);

	}

	@PutMapping("/unlike/{postId}")
	@CrossOrigin
	public ResponseEntity<?> unLikeIt(@PathVariable Long postId, @RequestBody FeedbackModel feedback) {
		String status = userService.DeleteLikeByEmail(feedback, postId);
		if (status == "NOT_LIKED") {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message(HttpStatus.BAD_REQUEST, status));
		}
		return ResponseEntity.status(HttpStatus.OK).body(new Message(HttpStatus.OK, status));
	}

	@CrossOrigin
	@DeleteMapping("/delete/{postId}")
	public ResponseEntity<?> deletePost(@PathVariable Long postId) {
		Optional<Posts> post = uploadRepository.findById(postId);
		if (post.isPresent()) {
			Optional<User> user = userRepository.findById(post.get().getUser().getId());
			if (user.isPresent()) {
				post.get().add(null);
				uploadRepository.delete(post.get());
				return ResponseEntity.status(HttpStatus.OK).body(new Message(HttpStatus.OK, "DELETED"));
			}

		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message(HttpStatus.BAD_REQUEST, "NOT_FOUND"));
	}

	@PutMapping("/comment/{postId}")
	@CrossOrigin
	public ResponseEntity<?> addComment(@PathVariable Long postId, @RequestBody FeedbackModel feedback) {
		String status = userService.addComment(feedback, postId);
		if (status.equalsIgnoreCase("VALID")) {
			return ResponseEntity.status(HttpStatus.ACCEPTED)
					.body(new Message(HttpStatus.ACCEPTED, status.toUpperCase()));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new Message(HttpStatus.BAD_REQUEST, status.toUpperCase()));
	}

	@PutMapping("/follow/{userId}/{email}")
	@CrossOrigin
	public ResponseEntity<?> follow(@PathVariable Long userId, @PathVariable String email) {
		String status = userService.followUser(userId, email);
		if (status == "FOLLOWED") {
			return ResponseEntity.status(HttpStatus.OK).body(new Message(HttpStatus.OK, status));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message(HttpStatus.BAD_REQUEST, status));
	}

	@DeleteMapping("/unfollow/{userId}/{email}")
	@CrossOrigin
	public ResponseEntity<?> unfollow(@PathVariable Long userId, @PathVariable String email) {
		String status = userService.unfollowUser(userId, email);
		if (status == "DELETED") {
			return ResponseEntity.status(HttpStatus.OK).body(new Message(HttpStatus.OK, status));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message(HttpStatus.BAD_REQUEST, status));
	}

	@DeleteMapping("/{postId}/deleteComment/{commentId}")
	@CrossOrigin
	public ResponseEntity<?> deleteComment(@PathVariable Long commentId, @PathVariable Long postId) {
		Optional<Comments> com = commentRepository.findById(commentId);
		if (com.isPresent()) {
			Optional<Posts> post = uploadRepository.findById(postId);
			if (post.isPresent()) {
				com.get().add(null);
				commentRepository.delete(com.get());
				return ResponseEntity.status(HttpStatus.OK).body(new Message(HttpStatus.OK, "DELETED"));
			}

		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message(HttpStatus.BAD_REQUEST, "NOT_FOUND"));
	}

	@GetMapping("{postId}/getLikes")
	@CrossOrigin()
	public ResponseEntity<?> numOfLikes(@PathVariable Long postId) {
		int size = userService.sizeOfLikes(postId);
		if (size == -1) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Message(HttpStatus.BAD_REQUEST, "NOT_FOUND"));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Length(size, HttpStatus.OK));
	}

	@CrossOrigin
	@GetMapping("{postId}/getComments")
	public ResponseEntity<?> numOfComments(@PathVariable Long postId) {
		int size = userService.numOfComments(postId);
		if (size == -1) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Message(HttpStatus.BAD_REQUEST, "NOT_FOUND"));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Length(size, HttpStatus.OK));
	}

	@CrossOrigin
	@GetMapping("{postId}/allComments")
	public List<Comments> allComments(@PathVariable Long postId) {
		return commentRepository.findByPostId(postId);
	}

	@CrossOrigin
	@GetMapping("/getnumLikes")
	public List<Object[]> getNum() {
		System.out.println(userService.getLikes().toString());
		return userService.getLikes();
	}

	@CrossOrigin
	@GetMapping("/getnumLikes2")
	public List<groupLikeById> getNum2() {
		return likesRepository.getNumberOfLikes2();

	}

	@CrossOrigin
	@GetMapping("/getUser/{postId}")
	public User getPostUser(@PathVariable Long postId) {
		return userService.findUserByPostId(postId);
	}

	@CrossOrigin
	@GetMapping("/getPost/{postId}")
	public Posts getPost(@PathVariable Long postId) {
		return uploadRepository.findById(postId).get();
	}

	@CrossOrigin
	@GetMapping("/getAllLikes/{postId}")
	public List<Likes> getLikes(@PathVariable Long postId) {
		return userService.findLikesByPostId(postId);
	}

	@CrossOrigin
	@PostMapping("/addComLike/{commentId}/{email}")
	public ResponseEntity<?> addCommentLike(@PathVariable String email, @PathVariable Long commentId) {
		String status = commentService.addCommentLike(email, commentId);
		if (status.equalsIgnoreCase("VALID")) {
			return ResponseEntity.status(HttpStatus.OK).body(new Message(HttpStatus.ACCEPTED, status));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message(HttpStatus.BAD_REQUEST, status));
	}

	@CrossOrigin
	@DeleteMapping("/deleteComLike/{commentId}/{email}")
	public ResponseEntity<?> deleteCommentLike(@PathVariable String email, @PathVariable Long commentId) {
		String status = commentService.deleteCommentLike(email, commentId);
		if (status.equalsIgnoreCase("UNLIKED")) {
			return ResponseEntity.status(HttpStatus.OK).body(new Message(HttpStatus.ACCEPTED, status));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message(HttpStatus.BAD_REQUEST, status));
	}

	@CrossOrigin
	@GetMapping("/getCommentLikes")
	public List<groupLikeById> getCommentLikes() {
		return commentLikeRepository.getCommentLikes();
	}

	@CrossOrigin
	@GetMapping("/following/{userId}")
	public List<Following> getFollowing(@PathVariable Long userId) {
		return followingRepository.findAllByUserId(userId);
	}

}
