package com.tdd.practical.entity;

import com.tdd.practical.entity.common.CommonEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class MapCategoryAndPost extends CommonEntity {

	@Id
	@GeneratedValue
	long id;

	@ManyToOne(fetch = FetchType.LAZY)
	Category category;

	@ManyToOne(fetch = FetchType.LAZY)
	Post post;

	@Builder
	public MapCategoryAndPost(Category category, Post post) {
		this.id = id;
		this.category = category;
		this.post = post;
	}
}

