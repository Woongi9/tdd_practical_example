package com.tdd.practical.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Review {
	@Id
	@GeneratedValue
	long id;

	@Column
	String title;

	@Column
	String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Users")
	Users users;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Post")
	Post post;

	@Builder
	public Review(String title, String content,
		Users users, Post post) {
		this.title = title;
		this.content = content;
		this.users = users;
		this.post = post;
	}
}
