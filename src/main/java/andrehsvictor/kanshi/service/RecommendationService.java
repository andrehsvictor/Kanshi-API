package andrehsvictor.kanshi.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import andrehsvictor.kanshi.dto.RecommendationDTO;
import andrehsvictor.kanshi.dto.SaveOrUpdateRecommendationDTO;
import andrehsvictor.kanshi.entity.Anime;
import andrehsvictor.kanshi.entity.Recommendation;
import andrehsvictor.kanshi.entity.User;
import andrehsvictor.kanshi.repository.AnimeRepository;
import andrehsvictor.kanshi.repository.RecommendationRepository;
import andrehsvictor.kanshi.repository.UserRepository;

@Service
public class RecommendationService {

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AnimeRepository animeRepository;

    @Autowired
    private UserRepository userRepository;

    public Page<RecommendationDTO> findAll(Pageable pageable) {
        Page<Recommendation> recommendations = recommendationRepository.findAll(pageable);
        return recommendations.map(recommendation -> modelMapper.map(recommendation, RecommendationDTO.class));
    }

    public Page<RecommendationDTO> findAllByAnimeId(Long animeId, Pageable pageable) {
        animeRepository.findById(animeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Anime not found"));
        Page<Recommendation> recommendations = recommendationRepository.findAllByAnimeId(animeId, pageable);
        return recommendations.map(recommendation -> modelMapper.map(recommendation, RecommendationDTO.class));
    }

    public RecommendationDTO findById(Long id) {
        Recommendation recommendation = recommendationRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recommendation not found"));
        return modelMapper.map(recommendation, RecommendationDTO.class);
    }

    public RecommendationDTO save(SaveOrUpdateRecommendationDTO saveRecommendationDTO, Long animeId,
            JwtAuthenticationToken token) {
        Recommendation recommendation = modelMapper.map(saveRecommendationDTO, Recommendation.class);
        Anime anime = animeRepository.findById(animeId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Anime not found"));
        recommendation.setAnime(anime);
        User user = userRepository.findByEmail(token.getToken().getSubject()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (user.getRecommendations().stream().anyMatch(r -> r.getAnime().getId().equals(animeId))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You have already recommended this anime");
        }
        recommendation.setUser(user);
        recommendation = recommendationRepository.save(recommendation);
        return modelMapper.map(recommendation, RecommendationDTO.class);
    }

    public void delete(Long id, JwtAuthenticationToken token) {
        Recommendation recommendation = recommendationRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recommendation not found"));
        if (!recommendation.getUser().getEmail().equals(token.getToken().getSubject())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only delete your own recommendations");
        }
        recommendationRepository.delete(recommendation);
    }

    public RecommendationDTO update(Long id, SaveOrUpdateRecommendationDTO updateRecommendationDTO,
            JwtAuthenticationToken token) {
        Recommendation recommendation = recommendationRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recommendation not found"));
        if (!recommendation.getUser().getEmail().equals(token.getToken().getSubject())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only update your own recommendations");
        }
        recommendation.setComment(updateRecommendationDTO.getComment());
        recommendation.setRating(updateRecommendationDTO.getRating());
        recommendation = recommendationRepository.save(recommendation);
        return modelMapper.map(recommendation, RecommendationDTO.class);
    }
}
