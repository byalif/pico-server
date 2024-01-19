package com.byalif.App.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String firstName;

	private String lastName;
	private String password;

	private String email;

	@JsonIgnore
	@Builder.Default
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Posts> posts = new ArrayList();

	@Builder.Default
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Follower> followers = new ArrayList();

	@Builder.Default
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Following> following = new ArrayList();

	@JsonManagedReference
	public List<Follower> getFollowers() {
		return this.followers;
	}

	@JsonManagedReference
	public List<Following> getFollowing() {
		return this.following;
	}

	public void addPost(Posts post) {
		posts.add(post);
	}

	public void removePost(Posts post) {
		posts.remove(post);

	}

	public String follow(Following follow) {
		for (int i = 0; i < following.size(); i++) {
			if (following.contains(follow)) {
				return "ALREADY_FOLLOW";
			}
		}
		following.add(follow);
		return "VALID";
	}

//	public String unfollow(Following follow) {
//		for (int i = 0; i < followers.size(); i++) {
//			if (followers.get(i).getEmail().equalsIgnoreCase(follow.getEmail())) {
//				following.remove(follow);
//				return "VALID";
//			}
//		}
//		return "NOT_FOLLOWING";
//
//	}

	public String gainFollow(Follower follow) {
		for (int i = 0; i < followers.size(); i++) {
			if (followers.contains(follow)) {
				return "ALREADY_FOLLOW";
			}
		}
		followers.add(follow);
		return "VALID";
	}

//	public String loseFollow(Follower follow) {
//		for (int i = 0; i < following.size(); i++) {
//			if (following.get(i).getEmail().equalsIgnoreCase(follow.getEmail())) {
//				followers.remove(follow);
//				return "VALID";
//			}
//		}
//		return "NOT_FOLLOWING";
//	}

}
