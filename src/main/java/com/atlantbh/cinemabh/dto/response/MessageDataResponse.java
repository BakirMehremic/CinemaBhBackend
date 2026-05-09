package com.atlantbh.cinemabh.dto.response;

public record MessageDataResponse<T>(String message, T data) {}
