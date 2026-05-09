package com.atlantbh.cinemabh.mapper.impl;

import com.atlantbh.cinemabh.dto.request.user.RegisterUserRequest;
import com.atlantbh.cinemabh.dto.response.UserPreviewResponse;
import com.atlantbh.cinemabh.entity.City;
import com.atlantbh.cinemabh.entity.User;
import com.atlantbh.cinemabh.enums.UserRole;
import com.atlantbh.cinemabh.mapper.UserMapper;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {
  @Override
  public UserPreviewResponse toPreviewResponse(User user) {
    return new UserPreviewResponse(
        user.getId(),
        user.getFirstName(),
        user.getLastName(),
        user.getEmail(),
        user.getPhoneNumber(),
        user.getImageUrl(),
        user.getUserRole().name(),
        user.isVerified());
  }

  @Override
  public User toEntity(
      RegisterUserRequest request, String phoneNumberNormalized, City city, String passwordHash) {
    User user = new User();
    user.setFirstName(request.firstName());
    user.setLastName(request.lastName());
    user.setEmail(request.email());
    user.setPhoneNumber(phoneNumberNormalized);
    user.setImageUrl(request.imageUrl());
    user.setStreet(request.street());
    user.setCity(city);
    user.setCreatedAt(LocalDateTime.now());
    user.setUserRole(UserRole.REGISTERED_USER);
    user.setPasswordHash(passwordHash);
    user.setVerified(false);
    return user;
  }
}
