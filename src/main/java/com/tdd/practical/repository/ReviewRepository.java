package com.tdd.practical.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tdd.practical.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
