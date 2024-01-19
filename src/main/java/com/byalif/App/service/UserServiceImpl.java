package com.byalif.App.service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.byalif.App.entity.Comments;
import com.byalif.App.entity.Follower;
import com.byalif.App.entity.Following;
import com.byalif.App.entity.Likes;
import com.byalif.App.entity.Posts;
import com.byalif.App.entity.User;
import com.byalif.App.message.Like;
import com.byalif.App.message.Message;
import com.byalif.App.model.FeedbackModel;
import com.byalif.App.model.Login;
import com.byalif.App.model.Upload;
import com.byalif.App.model.registerModel;
import com.byalif.App.repository.CommentRepository;
import com.byalif.App.repository.FollowerRepository;
import com.byalif.App.repository.FollowingRepository;
import com.byalif.App.repository.LikesRepository;
import com.byalif.App.repository.UploadRepository;
import com.byalif.App.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

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
	FollowerRepository followerRepository;

	@Autowired
	FollowingRepository followingRepository;

	@Override
	public String registerUser(registerModel user) {
		if (user.getFirstName() != "") {
			if (user.getLastName() != "") {
				if (user.getEmail() != "") {
					if (userRepository.findByEmail(user.getEmail()) == null) {
						String regex = "^(.+)@(.+)$";
						Pattern pattern = Pattern.compile(regex);
						Matcher matcher = pattern.matcher(user.getEmail());
						if (matcher.matches()) {
							if (user.getPassword() != "") {
								if (user.getPassword().length() >= 4) {
									user.setPassword(passwordEncoder.encode(user.getPassword()));
									User ne = User.builder().email(user.getEmail()).firstName(user.getFirstName())
											.lastName(user.getLastName()).password(user.getPassword()).build();
									userRepository.save(ne);
									return "valid";
								}
								return "p_length";
							}
							return "p_blank";
						}
						return "e_invalid";
					}
					return "e_exists";
				}
				return "e_blank";

			}
			return "l_blank";
		}
		return "f_blank";
	}

	@Override
	public String findByEmail(Login user) {
		User userFound = userRepository.findByEmail(user.getEmail());
		if (userFound == null)
			return "NOT_FOUND";
		if (passwordEncoder.matches(user.getPassword(), userFound.getPassword())) {
			return "LOGGED_IN";
		}
		return "WRONG_PASS";

	}

	@Override
	public String postPicture(Upload upload) {
		User user = userRepository.findByEmail(upload.getEmail());
		if (user == null) {
			return "NOT_FOUND";
		}
		Posts post = Posts.builder().description(upload.getDescription()).title(upload.getTitle())
				.image(upload.getImage()).build();

		user.addPost(post);
		post.add(user);
		uploadRepository.save(post);
		return "UPLOADED";
	}

	@Override
	public String addComment(FeedbackModel feedback, Long postId) {
		Posts post = uploadRepository.findById(postId).get();
		if (feedback.getEmail() == null) {
			return "LOGIN";
		}
		Comments come = Comments.builder().comment(feedback.getComment()).email(feedback.getEmail()).build();
		post.addComment(come);
		come.add(post);
		commentRepository.save(come);
		return "VALID";

	}

	@Override
	public String deleteComment(Long postId, Long commentId) {
		Posts post = uploadRepository.findById(postId).get();
		Comments come = commentRepository.findById(commentId).get();
		if (come == null) {
			return "NOT_FOUND";
		}
		String status = post.deleteComment(come);
		if (status == "ALREADY_DELETED") {
			return "ALREADY_DELETED";
		}
		come.add(null);
		commentRepository.delete(come);
		return "DELETED";
	}

	@Override
	public String addLike(FeedbackModel feedback, Long postId) {
		Posts post = uploadRepository.findById(postId).get();
		Likes lik = Likes.builder().email(feedback.getEmail()).build();
		String status = post.addLike(lik);
		if (status == "LIKED") {
			return "LIKED_ALREADY";
		}
		lik.add(post);
		likesRepository.save(lik);
		return "VALID";
	}

	@Override
	public ResponseEntity<?> addLikes(FeedbackModel feedback, Long postId) {
		Posts post = uploadRepository.findById(postId).get();
		if (feedback.getEmail() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message(HttpStatus.BAD_REQUEST, "LOG_IN"));
		}
		Likes lik = Likes.builder().email(feedback.getEmail()).build();
		String status = post.addLike(lik);
		if (status == "LIKED_ALREADY") {
			Likes findlike = likesRepository.findByEmailAndPostId(feedback.getEmail(), postId);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Like(findlike, HttpStatus.BAD_REQUEST));
		}
		lik.add(post);
		likesRepository.save(lik);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Like(lik, HttpStatus.OK));
	}

	@Override
	public String DeleteLikeByEmail(FeedbackModel feedback, Long postId) {
		Posts post = uploadRepository.findById(postId).get();
		if (feedback.getEmail() == null) {
			return "UNLIKED_ALREADY";
		}
		Likes lik = likesRepository.findByEmailAndPostId(feedback.getEmail(), postId);
		if (lik == null) {
			return "UNLIKED";
		}
		likesRepository.DeleteByEmail(feedback.getEmail(), postId);
		String status = post.removeLike(lik);
		if (status == "UNLIKED_ALREADY")
			return status;
		return "UNLIKED";
	}

	@Override
	public int sizeOfLikes(Long postId) {
		Posts post = uploadRepository.findById(postId).get();
		if (post == null) {
			return -1;
		}
		return post.getLikes().size();
	}

	@Override
	public int numOfComments(Long postId) {
		Posts post = uploadRepository.findById(postId).get();
		if (post == null) {
			return -1;
		}
		return post.getComments().size();
	}

	@Override
	public List<Object[]> getLikes() {
		return likesRepository.getNumberOfLikes();
	}

	@Override
	public User findUserByPostId(Long postId) {
		return userRepository.findUserByPostId(postId);
	}

	@Override
	public List<Likes> findLikesByPostId(Long postId) {
		return likesRepository.findLikesByPostId(postId);
	}

	@Override
	public String followUser(Long userId, String email) {
		Optional<User> user = userRepository.findById(userId);
		User person = userRepository.findByEmail(email);
		String status = "FOLLOWED";
		if (person == null) {
			return "LOGIN";
		}
		if (!user.isPresent()) {
			return "NOT_FOUND";
		}
		Follower test = followerRepository.findByIdAndEmail(userId, email);
		Following test2 = followingRepository.findByIdAndEmail(person.getId(), user.get().getEmail());
		if (test == null) {
			Follower personB = Follower.builder().user(user.get()).email(email).build();
			user.get().gainFollow(personB);
			followerRepository.save(personB);
		}
		if (test2 == null) {
			Following personA = Following.builder().email(user.get().getEmail()).user(person).build();
			person.follow(personA);
			followingRepository.save(personA);
		}

		if (test != null || test2 != null) {
			status = "ALREADY_FOLLOWED";
		}
		return status;

	}

	@Override
	public String unfollowUser(Long userId, String email) {
		Optional<User> user = userRepository.findById(userId);
		User person = userRepository.findByEmail(email);
		if (person == null) {
			return "LOGIN";
		}
		if (!user.isPresent()) {
			return "NOT_FOUND";
		}

		followerRepository.findByIdAndEmailDelete(userId, email);

		followingRepository.findByIdAndEmailDelete(person.getId(), user.get().getEmail());

		return "DELETED";

	}

}