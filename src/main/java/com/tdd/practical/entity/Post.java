package com.tdd.practical.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Post {
	@Id
	@GeneratedValue
	long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Users")
	Users users;

	@Column
	String title;

	@Column
	String content;

	@OneToMany(fetch = FetchType.LAZY)
	List<MapCategoryAndPost> categoryMapList;

	@OneToMany(fetch = FetchType.LAZY)
	List<Review> reviewList;

	@Builder
	public Post(Users users, String title,
		String content, List<MapCategoryAndPost> categoryMapList,
		List<Review> reviewList) {
		this.users = users;
		this.title = title;
		this.content = content;
		this.categoryMapList = categoryMapList;
		this.reviewList = reviewList;
	}

	public void updateReview(List<Review> reviewList) {
		this.reviewList = reviewList;
	}
}
