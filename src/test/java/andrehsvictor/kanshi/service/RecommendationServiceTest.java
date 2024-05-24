package andrehsvictor.kanshi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import andrehsvictor.kanshi.dto.RecommendationDTO;
import andrehsvictor.kanshi.dto.SaveOrUpdateRecommendationDTO;
import andrehsvictor.kanshi.entity.Anime;
import andrehsvictor.kanshi.entity.Recommendation;
import andrehsvictor.kanshi.entity.User;
import andrehsvictor.kanshi.repository.AnimeRepository;
import andrehsvictor.kanshi.repository.RecommendationRepository;
import andrehsvictor.kanshi.repository.UserRepository;
import andrehsvictor.kanshi.util.AnimeFactory;
import andrehsvictor.kanshi.util.RecommendationFactory;
import andrehsvictor.kanshi.util.UserFactory;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.MockitoAnnotations.openMocks;

import java.util.List;
import java.util.Optional;

class RecommendationServiceTest {

    @Mock
    RecommendationRepository recommendationRepository;

    @Mock
    AnimeRepository animeRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    ModelMapper mockModelMapper;

    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    @InjectMocks
    RecommendationService recommendationService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void findAll_shouldReturnRecommendations() {
        Recommendation recommendation = RecommendationFactory.create();
        RecommendationDTO recommendationDTO = modelMapper.map(recommendation, RecommendationDTO.class);

        when(recommendationRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(recommendation)));
        when(mockModelMapper.map(recommendation, RecommendationDTO.class)).thenReturn(recommendationDTO);

        List<RecommendationDTO> recommendations = recommendationService.findAll(Pageable.unpaged()).toList();

        assertThat(recommendations).contains(recommendationDTO);

        verify(recommendationRepository).findAll(any(Pageable.class));
        verify(mockModelMapper).map(recommendation, RecommendationDTO.class);
    }

    @Test
    void findById_shouldReturnRecommendation() {
        Recommendation recommendation = RecommendationFactory.create();
        recommendation.setId(1L);
        RecommendationDTO recommendationDTO = modelMapper.map(recommendation, RecommendationDTO.class);

        when(recommendationRepository.findById(1L)).thenReturn(Optional.of(recommendation));
        when(mockModelMapper.map(recommendation, RecommendationDTO.class)).thenReturn(recommendationDTO);

        RecommendationDTO foundRecommendation = recommendationService.findById(1L);

        assertThat(foundRecommendation.getId()).isEqualTo(recommendationDTO.getId());
        assertThat(foundRecommendation.getAnime()).isEqualTo(recommendationDTO.getAnime());
        assertThat(foundRecommendation.getUser()).isEqualTo(recommendationDTO.getUser());
        assertThat(foundRecommendation.getComment()).isEqualTo(recommendationDTO.getComment());

        verify(recommendationRepository).findById(1L);
        verify(mockModelMapper).map(recommendation, RecommendationDTO.class);
    }

    @Test
    void save_shouldReturnRecommendation() {
        Recommendation recommendation = RecommendationFactory.create();
        Anime anime = AnimeFactory.create();
        recommendation.setAnime(anime);
        User user = UserFactory.create();
        recommendation.setUser(user);
        RecommendationDTO recommendationDTO = modelMapper.map(recommendation, RecommendationDTO.class);
        SaveOrUpdateRecommendationDTO saveRecommendationDTO = new SaveOrUpdateRecommendationDTO();
        saveRecommendationDTO.setComment("comment");
        saveRecommendationDTO.setRating(5.0);

        JwtAuthenticationToken jwtAuthenticationToken = mock(JwtAuthenticationToken.class);
        when(jwtAuthenticationToken.getToken()).thenReturn(mock(Jwt.class));
        when(jwtAuthenticationToken.getToken().getSubject()).thenReturn("user1");
        when(recommendationRepository.save(any(Recommendation.class))).thenReturn(recommendation);
        when(animeRepository.findById(1L)).thenReturn(Optional.of(recommendation.getAnime()));
        when(userRepository.findByEmail(jwtAuthenticationToken.getToken().getSubject()))
                .thenReturn(Optional.of(recommendation.getUser()));
        when(mockModelMapper.map(saveRecommendationDTO, Recommendation.class)).thenReturn(recommendation);
        when(mockModelMapper.map(recommendation, RecommendationDTO.class)).thenReturn(recommendationDTO);

        RecommendationDTO savedRecommendation = recommendationService
                .save(saveRecommendationDTO, 1L, jwtAuthenticationToken);

        assertThat(savedRecommendation.getId()).isEqualTo(recommendationDTO.getId());
        assertThat(savedRecommendation.getAnime()).isEqualTo(recommendationDTO.getAnime());
        assertThat(savedRecommendation.getUser()).isEqualTo(recommendationDTO.getUser());
        assertThat(savedRecommendation.getComment()).isEqualTo(recommendationDTO.getComment());

        verify(recommendationRepository).save(any(Recommendation.class));
        verify(mockModelMapper).map(recommendation, RecommendationDTO.class);
    }

    @Test
    void delete_shouldDeleteRecommendation() {
        Recommendation recommendation = RecommendationFactory.create();
        User user = UserFactory.create();
        user.setEmail("user1");
        recommendation.setUser(user);
        recommendation.setId(1L);

        JwtAuthenticationToken jwtAuthenticationToken = mock(JwtAuthenticationToken.class);
        when(jwtAuthenticationToken.getToken()).thenReturn(mock(Jwt.class));
        when(jwtAuthenticationToken.getToken().getSubject()).thenReturn("user1");
        when(recommendationRepository.findById(1L)).thenReturn(Optional.of(recommendation));

        recommendationService.delete(1L, jwtAuthenticationToken);

        verify(recommendationRepository).delete(recommendation);
    }

    @Test
    void update_shouldReturnUpdatedRecommendation() {
        Recommendation recommendation = RecommendationFactory.create();
        User user = UserFactory.create();
        user.setEmail("user1");
        recommendation.setUser(user);
        recommendation.setId(1L);
        RecommendationDTO recommendationDTO = modelMapper.map(recommendation, RecommendationDTO.class);
        SaveOrUpdateRecommendationDTO updateRecommendationDTO = new SaveOrUpdateRecommendationDTO();
        updateRecommendationDTO.setComment("comment");
        updateRecommendationDTO.setRating(5.0);

        JwtAuthenticationToken jwtAuthenticationToken = mock(JwtAuthenticationToken.class);
        when(jwtAuthenticationToken.getToken()).thenReturn(mock(Jwt.class));
        when(jwtAuthenticationToken.getToken().getSubject()).thenReturn("user1");
        when(recommendationRepository.findById(1L)).thenReturn(Optional.of(recommendation));
        when(recommendationRepository.save(recommendation)).thenReturn(recommendation);
        when(mockModelMapper.map(updateRecommendationDTO, Recommendation.class)).thenReturn(recommendation);
        when(mockModelMapper.map(recommendation, RecommendationDTO.class)).thenReturn(recommendationDTO);

        RecommendationDTO updatedRecommendation = recommendationService.update(1L, updateRecommendationDTO,
                jwtAuthenticationToken);

        assertThat(updatedRecommendation.getId()).isEqualTo(recommendationDTO.getId());
        assertThat(updatedRecommendation.getAnime()).isEqualTo(recommendationDTO.getAnime());
        assertThat(updatedRecommendation.getUser()).isEqualTo(recommendationDTO.getUser());
        assertThat(updatedRecommendation.getComment()).isEqualTo(recommendationDTO.getComment());

        verify(recommendationRepository).save(recommendation);
        verify(mockModelMapper).map(recommendation, RecommendationDTO.class);
    }

    @Test
    void update_shouldThrowExceptionWhenUserIsNotOwner() {
        Recommendation recommendation = RecommendationFactory.create();
        User user = UserFactory.create();
        user.setEmail("user2");
        recommendation.setUser(user);
        recommendation.setId(1L);
        SaveOrUpdateRecommendationDTO updateRecommendationDTO = new SaveOrUpdateRecommendationDTO();
        updateRecommendationDTO.setComment("comment");
        updateRecommendationDTO.setRating(5.0);

        JwtAuthenticationToken jwtAuthenticationToken = mock(JwtAuthenticationToken.class);
        when(jwtAuthenticationToken.getToken()).thenReturn(mock(Jwt.class));
        when(jwtAuthenticationToken.getToken().getSubject()).thenReturn("user1");
        when(recommendationRepository.findById(1L)).thenReturn(Optional.of(recommendation));

        try {
            recommendationService.update(1L, updateRecommendationDTO, jwtAuthenticationToken);
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("403 FORBIDDEN \"You can only update your own recommendations\"");
        }
    }

    @Test
    void delete_shouldThrowExceptionWhenUserIsNotOwner() {
        Recommendation recommendation = RecommendationFactory.create();
        User user = UserFactory.create();
        user.setEmail("user2");
        recommendation.setUser(user);
        recommendation.setId(1L);

        JwtAuthenticationToken jwtAuthenticationToken = mock(JwtAuthenticationToken.class);
        when(jwtAuthenticationToken.getToken()).thenReturn(mock(Jwt.class));
        when(jwtAuthenticationToken.getToken().getSubject()).thenReturn("user1");
        when(recommendationRepository.findById(1L)).thenReturn(Optional.of(recommendation));

        try {
            recommendationService.delete(1L, jwtAuthenticationToken);
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("403 FORBIDDEN \"You can only delete your own recommendations\"");
        }
    }

    @Test
    void save_shouldThrowExceptionWhenUserHasAlreadyRecommendedAnime() {
        Recommendation recommendation = RecommendationFactory.create();
        User user = UserFactory.create();
        user.setEmail("user1");
        Anime anime = AnimeFactory.create();
        recommendation.setAnime(anime);
        recommendation.setUser(user);
        recommendation.setId(1L);
        SaveOrUpdateRecommendationDTO saveRecommendationDTO = new SaveOrUpdateRecommendationDTO();
        saveRecommendationDTO.setComment("comment");
        saveRecommendationDTO.setRating(5.0);

        JwtAuthenticationToken jwtAuthenticationToken = mock(JwtAuthenticationToken.class);
        when(jwtAuthenticationToken.getToken()).thenReturn(mock(Jwt.class));
        when(jwtAuthenticationToken.getToken().getSubject()).thenReturn("user1");
        when(recommendationRepository.save(any(Recommendation.class))).thenReturn(recommendation);
        when(animeRepository.findById(1L)).thenReturn(Optional.of(recommendation.getAnime()));
        when(userRepository.findByEmail(jwtAuthenticationToken.getToken().getSubject()))
                .thenReturn(Optional.of(recommendation.getUser()));
        when(mockModelMapper.map(saveRecommendationDTO, Recommendation.class)).thenReturn(recommendation);
        when(mockModelMapper.map(recommendation, RecommendationDTO.class))
                .thenReturn(modelMapper.map(recommendation, RecommendationDTO.class));
        when(recommendationRepository.findAll()).thenReturn(List.of(recommendation));

        try {
            recommendationService.save(saveRecommendationDTO, 1L, jwtAuthenticationToken);
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("400 BAD_REQUEST \"You have already recommended this anime\"");
        }
    }

    @Test
    void save_shouldThrowExceptionWhenAnimeNotFound() {
        Recommendation recommendation = RecommendationFactory.create();
        User user = UserFactory.create();
        user.setEmail("user1");
        recommendation.setUser(user);
        recommendation.setId(1L);
        SaveOrUpdateRecommendationDTO saveRecommendationDTO = new SaveOrUpdateRecommendationDTO();
        saveRecommendationDTO.setComment("comment");
        saveRecommendationDTO.setRating(5.0);

        JwtAuthenticationToken jwtAuthenticationToken = mock(JwtAuthenticationToken.class);
        when(jwtAuthenticationToken.getToken()).thenReturn(mock(Jwt.class));
        when(jwtAuthenticationToken.getToken().getSubject()).thenReturn("user1");
        when(animeRepository.findById(1L)).thenReturn(Optional.empty());

        try {
            recommendationService.save(saveRecommendationDTO, 1L, jwtAuthenticationToken);
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("404 NOT_FOUND \"Anime not found\"");
        }
    }

    @Test
    void findById_shouldThrowExceptionWhenRecommendationNotFound() {
        when(recommendationRepository.findById(1L)).thenReturn(Optional.empty());

        try {
            recommendationService.findById(1L);
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("404 NOT_FOUND \"Recommendation not found\"");
        }
    }

    @Test
    void delete_shouldThrowExceptionWhenRecommendationNotFound() {
        JwtAuthenticationToken jwtAuthenticationToken = mock(JwtAuthenticationToken.class);
        when(jwtAuthenticationToken.getToken()).thenReturn(mock(Jwt.class));
        when(jwtAuthenticationToken.getToken().getSubject()).thenReturn("user1");
        when(recommendationRepository.findById(1L)).thenReturn(Optional.empty());

        try {
            recommendationService.delete(1L, jwtAuthenticationToken);
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("404 NOT_FOUND \"Recommendation not found\"");
        }
    }

    @Test
    void update_shouldThrowExceptionWhenRecommendationNotFound() {
        SaveOrUpdateRecommendationDTO updateRecommendationDTO = new SaveOrUpdateRecommendationDTO();
        updateRecommendationDTO.setComment("comment");
        updateRecommendationDTO.setRating(5.0);

        JwtAuthenticationToken jwtAuthenticationToken = mock(JwtAuthenticationToken.class);
        when(jwtAuthenticationToken.getToken()).thenReturn(mock(Jwt.class));
        when(jwtAuthenticationToken.getToken().getSubject()).thenReturn("user1");
        when(recommendationRepository.findById(1L)).thenReturn(Optional.empty());

        try {
            recommendationService.update(1L, updateRecommendationDTO, jwtAuthenticationToken);
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("404 NOT_FOUND \"Recommendation not found\"");
        }
    }

    @Test
    void update_shouldThrowExceptionWhenAnimeNotFound() {
        Recommendation recommendation = RecommendationFactory.create();
        User user = UserFactory.create();
        user.setEmail("user1");
        recommendation.setUser(user);
        recommendation.setId(1L);
        SaveOrUpdateRecommendationDTO updateRecommendationDTO = new SaveOrUpdateRecommendationDTO();
        updateRecommendationDTO.setComment("comment");
        updateRecommendationDTO.setRating(5.0);

        JwtAuthenticationToken jwtAuthenticationToken = mock(JwtAuthenticationToken.class);
        when(jwtAuthenticationToken.getToken()).thenReturn(mock(Jwt.class));
        when(jwtAuthenticationToken.getToken().getSubject()).thenReturn("user1");
        when(recommendationRepository.findById(1L)).thenReturn(Optional.of(recommendation));
        when(animeRepository.findById(1L)).thenReturn(Optional.empty());

        try {
            recommendationService.update(1L, updateRecommendationDTO, jwtAuthenticationToken);
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("404 NOT_FOUND \"Anime not found\"");
        }
    }

    @Test
    void save_shouldThrowExceptionWhenUserNotFound() {
        Recommendation recommendation = RecommendationFactory.create();
        Anime anime = AnimeFactory.create();
        recommendation.setAnime(anime);
        recommendation.setId(1L);
        SaveOrUpdateRecommendationDTO saveRecommendationDTO = new SaveOrUpdateRecommendationDTO();
        saveRecommendationDTO.setComment("comment");
        saveRecommendationDTO.setRating(5.0);

        JwtAuthenticationToken jwtAuthenticationToken = mock(JwtAuthenticationToken.class);
        when(jwtAuthenticationToken.getToken()).thenReturn(mock(Jwt.class));
        when(jwtAuthenticationToken.getToken().getSubject()).thenReturn("user1");
        when(mockModelMapper.map(saveRecommendationDTO, Recommendation.class)).thenReturn(recommendation);
        // when(mockModelMapper.map(recommendation, RecommendationDTO.class))
        //         .thenReturn(modelMapper.map(recommendation, RecommendationDTO.class));
        when(recommendationRepository.save(any(Recommendation.class))).thenReturn(recommendation);
        when(animeRepository.findById(1L)).thenReturn(Optional.of(recommendation.getAnime()));
        when(userRepository.findByEmail(jwtAuthenticationToken.getToken().getSubject())).thenReturn(Optional.empty());

        try {
            recommendationService.save(saveRecommendationDTO, 1L, jwtAuthenticationToken);
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("404 NOT_FOUND \"User not found\"");
        }
    }
}
