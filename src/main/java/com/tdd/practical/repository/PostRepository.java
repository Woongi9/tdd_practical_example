package com.tdd.practical.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tdd.practical.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
