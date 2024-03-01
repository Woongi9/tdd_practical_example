package com.tdd.practical.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tdd.practical.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
