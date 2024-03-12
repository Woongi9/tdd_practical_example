package com.tdd.practical.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

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

@SpringBootTest
class ReviewServiceImplTest {

	@Mock
	UserRepository userRepository;
	@Mock
	PostRepository postRepository;
	@Mock
	ReviewRepository reviewRepository;
	@Mock
	CategoryRepository categoryRepository;
	@Mock
	MapCategoryAndPostRepository mapCategoryAndPostRepository;
	@InjectMocks
	ReviewServiceImpl reviewService;

	Users getUser(String name, long id) {
		Users user = Users.builder()
			.name(name)
			.build();
		user.setId(id);
		return user;
	}

	Category getCategory(long id) {
		Category category = Category.builder()
			.name("공부")
			.build();
		category.setId(id);
		return category;
	}

	List<MapCategoryAndPost> getMapCategoryAndPostList() {
		Users user1 = getUser("user1", 2L);
		Users user2 = getUser("user2", 2L);

		Post post1 = getPost(user1, "Post1 제목");
		Post post2 = getPost(user2, "Post2 제목");

		Review review11 = getReview(post1, user2, "댓글1", "이게 글인가요???");
		Review review12 = getReview(post1, user2, "댓글2", "이게 글 맞나요???");

		List<Review> reviewList1 = new ArrayList<>();
		reviewList1.add(review11);
		reviewList1.add(review12);
		post1.updateReview(reviewList1);

		Review review21 = getReview(post2, user1, "review1", "이게 글이지");
		Review review22 = getReview(post2, user1, "review2", "이게 글이지2222");

		List<Review> reviewList2 = new ArrayList<>();
		reviewList2.add(review21);
		reviewList2.add(review22);
		post2.updateReview(reviewList2);

		Category category = getCategory(1L);
		MapCategoryAndPost mapCategoryAndPost1 = getMapCategoryAndPost(post1, category);
		MapCategoryAndPost mapCategoryAndPost2 = getMapCategoryAndPost(post2, category);

		List<MapCategoryAndPost> mapCategoryAndPostList = new ArrayList<>();
		mapCategoryAndPostList.add(mapCategoryAndPost1);
		mapCategoryAndPostList.add(mapCategoryAndPost2);
		return mapCategoryAndPostList;
	}

	private static Post getPost(Users user, String title) {
		Post post1 = Post.builder()
			.title(title)
			.users(user)
			.build();
		return post1;
	}

	private static Review getReview(Post post1, Users user2, String title, String content) {
		Review review11 = Review.builder()
			.post(post1)
			.title(title)
			.content(content)
			.users(user2)
			.build();
		return review11;
	}

	private static MapCategoryAndPost getMapCategoryAndPost(Post post1, Category category) {
		MapCategoryAndPost mapCategoryAndPost1 = MapCategoryAndPost.builder()
			.post(post1)
			.category(category)
			.build();
		return mapCategoryAndPost1;
	}

	@Test
	@DisplayName("User Id null")
	void noUserId() {
		// given
		ReviewToCategoryDTO reviewToCategoryDTO = ReviewToCategoryDTO.builder().build();

		assertThatIllegalArgumentException()
			.isThrownBy(() -> reviewService.createReviewForCategory(reviewToCategoryDTO));
	}

	@Test
	@DisplayName("User Id not exist")
	void notExistUserId() {
		// given
		long userId = 999999L;
		ReviewToCategoryDTO reviewToCategoryDTO = ReviewToCategoryDTO.builder().userId(userId).build();

		assertThatIllegalArgumentException()
			.isThrownBy(() -> reviewService.createReviewForCategory(reviewToCategoryDTO));
	}

	@Test
	@DisplayName("Category Id null")
	void noCategoryId() {
		// given
		ReviewToCategoryDTO reviewToCategoryDTO = ReviewToCategoryDTO.builder().build();

		given(userRepository.findById(any(Long.class))).willReturn(Optional.of(getUser("유저1", 1L)));

		assertThatIllegalArgumentException()
			.isThrownBy(() -> reviewService.createReviewForCategory(reviewToCategoryDTO));
	}

	@Test
	@DisplayName("Category Id not exist")
	void notExistCategoryId() {
		// given
		long userId = 999999L;
		ReviewToCategoryDTO reviewToCategoryDTO = ReviewToCategoryDTO.builder().userId(userId).build();

		given(userRepository.findById(any(Long.class))).willReturn(Optional.of(getUser("유저1", 1L)));

		assertThatIllegalArgumentException()
			.isThrownBy(() -> reviewService.createReviewForCategory(reviewToCategoryDTO));
	}

	@Test
	@DisplayName("Study Category - MapCategoryAndPost not mapping with Category")
	void studyCategoryNotMappingCategoryAndPost() {
		// given
		long userId = 999999L;
		long categoryId = 1L;
		ReviewToCategoryDTO reviewToCategoryDTO = ReviewToCategoryDTO.builder()
			.userId(userId)
			.categoryId(categoryId)
			.build();

		given(userRepository.findById(any(Long.class))).willReturn(Optional.of(getUser("유저1", 1L)));
		given(categoryRepository.findById(any(Long.class))).willReturn(Optional.of(getCategory(1L)));

		// when
		ResponseEntity<?> responseEntity = reviewService.createReviewForCategory(reviewToCategoryDTO);

		// then
		Map<String, Object> body = (Map<String, Object>)responseEntity.getBody();
		assertThat((int)body.get("reviewCount")).isEqualTo(0);
		assertThat((List<Review>)body.get("reviewList")).isEmpty();
	}

	@Test
	@DisplayName("Study Category")
	void studyCategory() {
		// given
		long userId = 999999L;
		long categoryId = 1L;
		ReviewToCategoryDTO reviewToCategoryDTO = ReviewToCategoryDTO.builder()
			.userId(userId)
			.categoryId(categoryId)
			.build();

		given(userRepository.findById(any(Long.class))).willReturn(Optional.of(getUser("유저1", 1L)));
		given(categoryRepository.findById(any(Long.class))).willReturn(Optional.of(getCategory(1L)));
		given(mapCategoryAndPostRepository.getByCategory(any(Category.class))).willReturn(getMapCategoryAndPostList());

		// when
		ResponseEntity<?> responseEntity = reviewService.createReviewForCategory(reviewToCategoryDTO);

		// then
		Map<String, Object> body = (Map<String, Object>)responseEntity.getBody();
		assertThat((int)body.get("reviewCount")).isEqualTo(2);
		List<Review> reviewList = (List<Review>)body.get("reviewList");
		assertThat(reviewList.size()).isEqualTo(2);
	}

	@Test
	@DisplayName("Not Study Category - MapCategoryAndPost not mapping with Category")
	void notStudyCategoryNotMappingCategoryAndPost() {
		// given
		long userId = 999999L;
		long categoryId = 2L;
		ReviewToCategoryDTO reviewToCategoryDTO = ReviewToCategoryDTO.builder()
			.userId(userId)
			.categoryId(categoryId)
			.build();

		given(userRepository.findById(any(Long.class))).willReturn(Optional.of(getUser("유저1", 1L)));
		given(categoryRepository.findById(any(Long.class))).willReturn(Optional.of(getCategory(1L)));

		// when
		ResponseEntity<?> responseEntity = reviewService.createReviewForCategory(reviewToCategoryDTO);

		// then
		Map<String, Object> body = (Map<String, Object>)responseEntity.getBody();
		assertThat((int)body.get("reviewCount")).isEqualTo(0);
		assertThat((List<Review>)body.get("reviewList")).isEmpty();
	}

	@Test
	@DisplayName("Not Study Category")
	void notStudyCategory() {
		// given
		long userId = 999999L;
		long categoryId = 2L;
		ReviewToCategoryDTO reviewToCategoryDTO = ReviewToCategoryDTO.builder()
			.userId(userId)
			.categoryId(categoryId)
			.build();

		given(userRepository.findById(any(Long.class))).willReturn(Optional.of(getUser("유저1", 1L)));
		given(categoryRepository.findById(any(Long.class))).willReturn(Optional.of(getCategory(2L)));
		given(mapCategoryAndPostRepository.getByCategory(any(Category.class))).willReturn(getMapCategoryAndPostList());

		// when
		ResponseEntity<?> responseEntity = reviewService.createReviewForCategory(reviewToCategoryDTO);

		// then
		Map<String, Object> body = (Map<String, Object>)responseEntity.getBody();
		assertThat((int)body.get("reviewCount")).isEqualTo(1);
		List<Review> reviewList = (List<Review>)body.get("reviewList");
		assertThat(reviewList.size()).isEqualTo(1);
	}
}