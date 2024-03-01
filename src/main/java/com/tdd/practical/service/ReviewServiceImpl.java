package com.tdd.practical.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tdd.practical.controller.dto.ReviewToCategoryDTO;
import com.tdd.practical.entity.Category;
import com.tdd.practical.entity.MapCategoryAndPost;
import com.tdd.practical.entity.Post;
import com.tdd.practical.entity.Review;
import com.tdd.practical.entity.Users;
import com.tdd.practical.repository.CategoryRepository;
import com.tdd.practical.repository.MapCategoryAndPostRepository;
import com.tdd.practical.repository.PostRepository;
import com.tdd.practical.repository.ReviewRepository;
import com.tdd.practical.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

	private final UserRepository userRepository;
	private final PostRepository postRepository;
	private final ReviewRepository reviewRepository;
	private final CategoryRepository categoryRepository;
	private final MapCategoryAndPostRepository mapCategoryAndPostRepository;

	@Override
	@Transactional
	public ResponseEntity<?> createReviewForCategory(ReviewToCategoryDTO reviewToCategoryDTO) {
		Users users = userRepository.findById(reviewToCategoryDTO.getUserId()).orElseThrow(() ->
			new IllegalArgumentException("user가 없습니다. userId=" + reviewToCategoryDTO.getUserId()));
		Category category = categoryRepository.findById(reviewToCategoryDTO.getCategoryId()).orElseThrow(() ->
			new IllegalArgumentException("category가 없습니다. categoryId=" + reviewToCategoryDTO.getCategoryId()));

		int reviewCount = 0;
		List<Review> reviewList = new ArrayList<>();
		if (category.getId() == 1) {
			List<MapCategoryAndPost> mapCategoryAndPostList = mapCategoryAndPostRepository.getByCategory(category);
			for (MapCategoryAndPost mapCategoryAndPost : mapCategoryAndPostList) {
				Review reviewBuilder = Review.builder()
					.title(reviewToCategoryDTO.getTitle())
					.content(reviewToCategoryDTO.getContent())
					.users(users)
					.build();
				Post post = mapCategoryAndPost.getPost();
				List<Review> postReviewList = post.getReviewList();
				postReviewList.add(reviewBuilder);
				post.updateReview(postReviewList);
				postRepository.save(post);
				Review savedReview = reviewRepository.save(reviewBuilder);
				reviewList.add(savedReview);
				reviewCount++;
			}
		} else {
			List<MapCategoryAndPost> mapCategoryAndPostList = mapCategoryAndPostRepository.getByCategory(category);
			MapCategoryAndPost mapCategoryAndPost = mapCategoryAndPostList.get(0);
			Review reviewBuilder = Review.builder()
				.title(reviewToCategoryDTO.getTitle())
				.content(reviewToCategoryDTO.getContent())
				.users(users)
				.build();
			Post post = mapCategoryAndPost.getPost();
			List<Review> postReviewList = post.getReviewList();
			postReviewList.add(reviewBuilder);
			post.updateReview(postReviewList);
			postRepository.save(post);
			Review savedReview = reviewRepository.save(reviewBuilder);
			reviewList.add(savedReview);
			reviewCount++;
		}

		Map<String, Object> responseMap = new HashMap<>();
		responseMap.put("reviewCount", reviewCount);
		responseMap.put("reviewList", reviewList);
		return ResponseEntity.ok().body(responseMap);
	}
}
