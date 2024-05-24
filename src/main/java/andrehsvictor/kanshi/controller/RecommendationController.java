package andrehsvictor.kanshi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import andrehsvictor.kanshi.dto.RecommendationDTO;
import andrehsvictor.kanshi.dto.SaveOrUpdateRecommendationDTO;
import andrehsvictor.kanshi.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Recommendation")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @GetMapping("/api/1.0/recommendations")
    @Operation(summary = "Get all recommendations", responses = {
            @ApiResponse(responseCode = "200", description = "Recommendations returned successfully")
    })
    public ResponseEntity<Page<RecommendationDTO>> findAll(Pageable pageable) {
        return ResponseEntity.ok(recommendationService.findAll(pageable));
    }

    @GetMapping("/api/1.0/recommendations/{id}")
    @Operation(summary = "Get recommendation by id", responses = {
            @ApiResponse(responseCode = "200", description = "Recommendation returned successfully"),
            @ApiResponse(responseCode = "404", description = "Recommendation not found")
    })
    public ResponseEntity<RecommendationDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(recommendationService.findById(id));
    }

    @GetMapping("/api/1.0/animes/{animeId}/recommendations/all")
    @Operation(summary = "Get all recommendations by anime id", responses = {
            @ApiResponse(responseCode = "200", description = "Recommendations returned successfully")
    })
    public ResponseEntity<Page<RecommendationDTO>> findAllByAnimeId(@PathVariable Long animeId, Pageable pageable) {
        return ResponseEntity.ok(recommendationService.findAllByAnimeId(animeId, pageable));
    }

    @PostMapping("/api/1.0/animes/{animeId}/recommendations")
    @Operation(summary = "Recommend an anime", responses = {
            @ApiResponse(responseCode = "200", description = "Anime recommended successfully"),
            @ApiResponse(responseCode = "404", description = "Anime not found"),
            @ApiResponse(responseCode = "400", description = "Invalid recommendation")
    })
    public ResponseEntity<RecommendationDTO> recommend(@PathVariable Long animeId, JwtAuthenticationToken token,
            @RequestBody SaveOrUpdateRecommendationDTO saveRecommendationDTO) {
        return ResponseEntity.ok(recommendationService.save(saveRecommendationDTO, animeId, token));
    }

    @DeleteMapping("/api/1.0/recommendations/{id}")
    @Operation(summary = "Delete recommendation by id", responses = {
            @ApiResponse(responseCode = "204", description = "Recommendation deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Recommendation not found")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id, JwtAuthenticationToken token) {
        recommendationService.delete(id, token);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/api/1.0/recommendations/{id}")
    @Operation(summary = "Update recommendation by id", responses = {
            @ApiResponse(responseCode = "200", description = "Recommendation updated successfully"),
            @ApiResponse(responseCode = "404", description = "Recommendation not found")
    })
    public ResponseEntity<RecommendationDTO> update(@PathVariable Long id, JwtAuthenticationToken token,
            @RequestBody SaveOrUpdateRecommendationDTO updateRecommendationDTO) {
        return ResponseEntity.ok(recommendationService.update(id, updateRecommendationDTO, token));
    }
}
