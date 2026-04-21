package com.atlantbh.cinemabh.validator;

import com.atlantbh.cinemabh.exception.InvalidPaginationException;

public class PaginationValidator {

  private static final int MAX_PAGE_SIZE = 100;

  private PaginationValidator() {}

  public static void validate(int pageNumber, int pageSize) {
    if (pageNumber < 0) {
      throw new InvalidPaginationException("pageNumber must be >= 0");
    }

    if (pageSize <= 0) {
      throw new InvalidPaginationException("pageSize must be > 0");
    }

    if (pageSize > MAX_PAGE_SIZE) {
      throw new InvalidPaginationException("pageSize must be <= " + MAX_PAGE_SIZE);
    }
  }
}
