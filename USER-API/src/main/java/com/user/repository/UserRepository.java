package com.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.entity.User;

public interface UserRepository extends JpaRepository<User, String> {

	Optional<User> findByEmail(String email);
	Optional<User> findByUsername(String username);

}
