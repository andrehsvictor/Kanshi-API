package andrehsvictor.kanshi.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import andrehsvictor.kanshi.dto.AnimeDTO;
import andrehsvictor.kanshi.dto.ShortAnimeDTO;
import andrehsvictor.kanshi.service.AnimeService;

@RestController
public class AnimeController {

    @Autowired
    private AnimeService animeService;

    @GetMapping("/api/1.0/animes")
    public ResponseEntity<Page<ShortAnimeDTO>> findAll(Pageable pageable) {
        return ResponseEntity.ok(animeService.findAll(pageable));
    }

    @GetMapping("/api/1.0/animes/{id}")
    public ResponseEntity<AnimeDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(animeService.findById(id));
    }

    @PatchMapping("/api/1.0/animes/{id}/like")
    public ResponseEntity<Void> like(@PathVariable Long id, JwtAuthenticationToken token) {
        animeService.like(id, token);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/1.0/animes/liked")
    public ResponseEntity<Set<ShortAnimeDTO>> findLikedAnimes(JwtAuthenticationToken token) {
        return ResponseEntity.ok(animeService.findLikedAnimes(token));
    }
}
