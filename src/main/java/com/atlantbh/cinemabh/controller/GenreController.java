package com.atlantbh.cinemabh.controller;

import com.atlantbh.cinemabh.dto.response.NameIdPair;
import com.atlantbh.cinemabh.service.GenreService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;

    @GetMapping("/names")
    public ResponseEntity<List<NameIdPair>> getAllGenreNameIdPairs(){
        List<NameIdPair> response = genreService.getAllGenreNameIdPairs();
        return ResponseEntity.ok(response);
    }
}
