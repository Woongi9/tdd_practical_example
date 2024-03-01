package com.tdd.practical.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tdd.practical.entity.Users;

public interface UserRepository extends JpaRepository<Users, Long> {
}
