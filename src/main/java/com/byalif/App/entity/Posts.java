package com.byalif.App.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

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
public class Posts {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;

	private String title;
	private String description;
	private String image;

	@Builder.Default
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Likes> likes = new ArrayList();

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

	@Builder.Default
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<Comments> comments = new ArrayList();

	public void add(User user) {
		this.user = user;

	}

	@JsonManagedReference
	public List<Likes> getLikes() {
		return this.likes;
	}

	@JsonManagedReference
	public List<Comments> getComments() {
		return this.comments;
	}

	public String addLike(Likes lik) {
		for (int i = 0; i < likes.size(); i++) {
			if (likes.get(i).getEmail().equalsIgnoreCase(lik.getEmail())) {
				return "LIKED_ALREADY";
			}
		}
		likes.add(lik);
		return "VALID";

	}

	public String removeLike(Likes lik) {
		for (int i = 0; i < likes.size(); i++) {
			if (likes.get(i).getEmail().equalsIgnoreCase(lik.getEmail())) {
				likes.remove(i);
				return "UNLIKED_ALREADY";
			}
		}
		return "UNLIKED";

	}

	public void addComment(Comments comment) {
		comments.add(comment);
	}

	public String deleteComment(Comments come) {
		for (int i = 0; i < comments.size(); i++) {
			if (comments.get(i).getEmail().equalsIgnoreCase(come.getEmail())) {
				comments.remove(i);
				return "DELETED";
			}
		}
		return "ALREADY_DELETED";

	}

}