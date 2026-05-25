package com.atlantbh.cinemabh.mapper.impl;

import com.atlantbh.cinemabh.dto.request.user.RegisterUserRequest;
import com.atlantbh.cinemabh.dto.response.UserDetailsResponse;
import com.atlantbh.cinemabh.entity.City;
import com.atlantbh.cinemabh.entity.User;
import com.atlantbh.cinemabh.enums.UserRole;
import com.atlantbh.cinemabh.mapper.UserMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {
  @Override
  public UserDetailsResponse toDetailsResponse(User user) {
    return new UserDetailsResponse(
        user.getId(),
        user.getFirstName(),
        user.getLastName(),
        user.getEmail(),
        user.getPhoneNumber(),
        user.getImagePath(),
        user.getUserRole().toString(),
        user.isVerified());
  }

  @Override
  public User toEntity(
      RegisterUserRequest request,
      String phoneNumberNormalized,
      City city,
      String passwordHash,
      UserRole role,
      boolean isVerified) {
    User user = new User();
    user.setFirstName(request.firstName());
    user.setLastName(request.lastName());
    user.setEmail(request.email());
    user.setPhoneNumber(phoneNumberNormalized);
    /*    user.setImagePath(request.imageUrl());
    user.setStreet(request.address().street());*/
    user.setCity(city);
    user.setUserRole(role);
    user.setPasswordHash(passwordHash);
    user.setVerified(isVerified);
    return user;
  }

  @Override
  public User toEntity(
      RegisterUserRequest request, String passwordHash, UserRole role, boolean isVerified) {
    User user = new User();
    user.setFirstName(request.firstName());
    user.setLastName(request.lastName());
    user.setEmail(request.email());
    user.setUserRole(role);
    user.setPasswordHash(passwordHash);
    user.setVerified(isVerified);
    return user;
  }
}
