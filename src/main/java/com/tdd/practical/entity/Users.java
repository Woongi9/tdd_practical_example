package com.tdd.practical.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Users {

	@Id
	@GeneratedValue
	private Long id;

	@Column
	private String name;

	@OneToMany(fetch = FetchType.LAZY)
	List<Post> postList;

	@OneToMany(fetch = FetchType.LAZY)
	List<Review> reviewList;

	@Builder
	public Users(String name, List<Post> postList,
		List<Review> reviewList) {
		this.name = name;
		this.postList = postList;
		this.reviewList = reviewList;
	}
}
