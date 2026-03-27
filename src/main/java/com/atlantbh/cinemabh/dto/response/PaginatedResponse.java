package com.atlantbh.cinemabh.dto.response;

import java.util.List;
import org.springframework.data.domain.Page;

public record PaginatedResponse<T>(
    List<T> content, int pageNumber, int pageSize, long totalElements, int totalPages) {

  public static <T> PaginatedResponse<T> from(Page<T> page) {
    return new PaginatedResponse<>(
        page.getContent(),
        page.getNumber(),
        page.getSize(),
        page.getTotalElements(),
        page.getTotalPages());
  }
}
