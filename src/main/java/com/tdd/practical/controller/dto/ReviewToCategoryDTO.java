package com.tdd.practical.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewToCategoryDTO {
	long categoryId;
	long userId;
	String title;
	String content;

	@Builder
	public ReviewToCategoryDTO(long categoryId, long userId,
		String title, String content) {
		this.categoryId = categoryId;
		this.userId = userId;
		this.title = title;
		this.content = content;
	}
}
