package com.tdd.practical.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
	private static final int STUDY_CATEGORY_ID = 1;

	@Override
	@Transactional
	public ResponseEntity<?> createReviewForCategory(ReviewToCategoryDTO reviewToCategoryDTO) {
		Users users = userRepository.findById(reviewToCategoryDTO.getUserId()).orElseThrow(() ->
			new IllegalArgumentException("user가 없습니다. userId=" + reviewToCategoryDTO.getUserId()));
		Category category = categoryRepository.findById(reviewToCategoryDTO.getCategoryId()).orElseThrow(() ->
			new IllegalArgumentException("category가 없습니다. categoryId=" + reviewToCategoryDTO.getCategoryId()));

		List<Review> reviewList = getReviewList(reviewToCategoryDTO, category, users);

		Map<String, Object> responseMap = new HashMap<>();
		responseMap.put("reviewCount", reviewList.size());
		responseMap.put("reviewList", reviewList);
		return ResponseEntity.ok().body(responseMap);
	}

	private List<Review> getReviewList(ReviewToCategoryDTO reviewToCategoryDTO, Category category, Users users) {
		List<Review> reviewList = new ArrayList<>();
		List<MapCategoryAndPost> mapCategoryAndPostList = mapCategoryAndPostRepository.getByCategory(category);
		if (isStudyCategory(category)) {
			reviewList.addAll(
				reviewRepository.saveAll(getReviewBuilderList(reviewToCategoryDTO, mapCategoryAndPostList, users)));
			postRepository.saveAll(getPostList(reviewList));
		} else {
			MapCategoryAndPost mapCategoryAndPost = mapCategoryAndPostList.get(0);
			Review savedReview = reviewRepository.save(getReview(reviewToCategoryDTO, mapCategoryAndPost, users));
			reviewList.add(savedReview);
			postRepository.save(getUpdatedReviewListPost(mapCategoryAndPost.getPost(), savedReview));
		}
		return reviewList;
	}

	private static boolean isStudyCategory(Category category) {
		return category.getId() == STUDY_CATEGORY_ID;
	}

	private static List<Review> getReviewBuilderList(ReviewToCategoryDTO reviewToCategoryDTO,
		List<MapCategoryAndPost> mapCategoryAndPostList, Users users) {
		List<Review> reviewBuilderList = new ArrayList<>();
		for (MapCategoryAndPost mapCategoryAndPost : mapCategoryAndPostList) {
			reviewBuilderList.add(getReview(reviewToCategoryDTO, mapCategoryAndPost, users));
		}
		return reviewBuilderList;
	}

	private static Review getReview(ReviewToCategoryDTO reviewToCategoryDTO, MapCategoryAndPost mapCategoryAndPost,
		Users users) {
		Review reviewBuilder = Review.builder()
			.title(reviewToCategoryDTO.getTitle())
			.content(reviewToCategoryDTO.getContent())
			.post(mapCategoryAndPost.getPost())
			.users(users)
			.build();
		return reviewBuilder;
	}

	private static List<Post> getPostList(List<Review> reviewList) {
		List<Post> postList = new ArrayList<>();
		for (Review review : reviewList) {
			postList.add(getUpdatedReviewListPost(review.getPost(), review));
		}
		return postList;
	}

	private static Post getUpdatedReviewListPost(Post mapCategoryAndPost, Review savedReview) {
		Post post = mapCategoryAndPost;
		List<Review> postReviewList = post.getReviewList();
		postReviewList.add(savedReview);
		post.updateReview(postReviewList);
		return post;
	}
}
