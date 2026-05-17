package com.atlantbh.cinemabh.mapper;

import com.atlantbh.cinemabh.dto.request.user.RegisterUserRequest;
import com.atlantbh.cinemabh.dto.response.UserDetailsResponse;
import com.atlantbh.cinemabh.entity.City;
import com.atlantbh.cinemabh.entity.User;
import com.atlantbh.cinemabh.enums.UserRole;

public interface UserMapper {
  UserDetailsResponse toDetailsResponse(User user);

  User toEntity(
      RegisterUserRequest request,
      String phoneNumberNormalized,
      City city,
      String passwordHash,
      UserRole role,
      boolean isVerified);
}
