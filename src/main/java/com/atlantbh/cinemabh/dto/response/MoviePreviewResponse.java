package com.atlantbh.cinemabh.dto.response;

import java.util.List;

public record MoviePreviewResponse(
    String name,
    Long id,
    Integer durationMinutes,
    String coverPhotoUrl,
    String synopsis,
    List<String> genres) {}
