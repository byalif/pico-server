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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comments {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String comment;

	private String email;

	@Builder.Default
	@OneToMany(mappedBy = "theComment", orphanRemoval = true, fetch = FetchType.EAGER)
	private List<CommentLike> like = new ArrayList<CommentLike>();

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "post_id", referencedColumnName = "Id")
	private Posts post;

	public void add(Posts post) {
		this.post = post;
	}

	@JsonManagedReference
	public List<CommentLike> getLike() {
		return this.like;
	}

	public void addComLike(CommentLike comLike) {
		like.add(comLike);

	}

	public void removeComLike(CommentLike comLike) {
		like.remove(comLike);

	}

}
