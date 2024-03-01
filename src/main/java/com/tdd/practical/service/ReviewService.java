package com.tdd.practical.service;

import org.springframework.http.ResponseEntity;

import com.tdd.practical.controller.dto.ReviewToCategoryDTO;

public interface ReviewService {

	public ResponseEntity<?> createReviewForCategory(ReviewToCategoryDTO reviewToCategoryDTO);
}
