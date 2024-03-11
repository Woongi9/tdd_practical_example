package com.tdd.practical.service;

import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.tdd.practical.controller.dto.ReviewToCategoryDTO;
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

	Users getUser() {
		Users user = Users.builder()
			.name("유저1")
			.build();
		user.setId(1L);
		return user;
	}

	@Test
	@DisplayName("User Id null")
	void noUserId() {
		// given
		ReviewToCategoryDTO reviewToCategoryDTO = ReviewToCategoryDTO.builder().build();

		Assertions.assertThatIllegalArgumentException()
			.isThrownBy(() -> reviewService.createReviewForCategory(reviewToCategoryDTO));
	}

	@Test
	@DisplayName("User Id not exist")
	void notExistUserId() {
		// given
		long userId = 999999L;
		ReviewToCategoryDTO reviewToCategoryDTO = ReviewToCategoryDTO.builder().userId(userId).build();

		Assertions.assertThatIllegalArgumentException()
			.isThrownBy(() -> reviewService.createReviewForCategory(reviewToCategoryDTO));
	}

	@Test
	@DisplayName("Category Id null")
	void noCategoryId() {
		// given
		ReviewToCategoryDTO reviewToCategoryDTO = ReviewToCategoryDTO.builder().build();

		given(userRepository.findById(any(Long.class))).willReturn(Optional.of(getUser()));

		Assertions.assertThatIllegalArgumentException()
			.isThrownBy(() -> reviewService.createReviewForCategory(reviewToCategoryDTO));
	}

	@Test
	@DisplayName("Category Id not exist")
	void notExistCategoryId() {
		// given
		long userId = 999999L;
		ReviewToCategoryDTO reviewToCategoryDTO = ReviewToCategoryDTO.builder().userId(userId).build();

		given(userRepository.findById(any(Long.class))).willReturn(Optional.of(getUser()));

		Assertions.assertThatIllegalArgumentException()
			.isThrownBy(() -> reviewService.createReviewForCategory(reviewToCategoryDTO));
	}
}