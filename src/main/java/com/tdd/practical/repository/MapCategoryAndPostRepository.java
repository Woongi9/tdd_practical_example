package com.tdd.practical.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tdd.practical.entity.Category;
import com.tdd.practical.entity.MapCategoryAndPost;

public interface MapCategoryAndPostRepository extends JpaRepository<MapCategoryAndPost, Long> {

	List<MapCategoryAndPost> getByCategory(Category category);
}
