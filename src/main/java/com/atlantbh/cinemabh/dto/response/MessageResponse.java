package com.atlantbh.cinemabh.dto.response;

public record MessageResponse<T>(String message, T data) {}
