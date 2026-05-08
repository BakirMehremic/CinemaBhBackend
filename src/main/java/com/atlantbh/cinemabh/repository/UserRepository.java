package com.atlantbh.cinemabh.repository;

import com.atlantbh.cinemabh.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmailOrPhoneNumber(String email, String phone);

  Optional<User> findByEmail(String email);
}
