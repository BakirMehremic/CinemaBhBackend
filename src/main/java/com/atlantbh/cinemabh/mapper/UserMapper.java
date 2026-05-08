package com.atlantbh.cinemabh.mapper;

import com.atlantbh.cinemabh.dto.request.user.RegisterUserRequest;
import com.atlantbh.cinemabh.dto.response.UserPreviewResponse;
import com.atlantbh.cinemabh.entity.City;
import com.atlantbh.cinemabh.entity.User;

public interface UserMapper {
  UserPreviewResponse toPreviewResponse(User user);

  User toEntity(RegisterUserRequest request, City city, String passwordHash);
}
