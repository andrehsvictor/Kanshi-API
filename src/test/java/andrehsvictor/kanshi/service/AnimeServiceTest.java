package andrehsvictor.kanshi.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import com.nimbusds.jwt.JWT;

import andrehsvictor.kanshi.dto.AnimeDTO;
import andrehsvictor.kanshi.dto.ShortAnimeDTO;
import andrehsvictor.kanshi.entity.Anime;
import andrehsvictor.kanshi.entity.User;
import andrehsvictor.kanshi.repository.AnimeRepository;
import andrehsvictor.kanshi.repository.UserRepository;
import andrehsvictor.kanshi.util.AnimeFactory;
import andrehsvictor.kanshi.util.UserFactory;

class AnimeServiceTest {
    @Mock
    AnimeRepository animeRepository;

    @Mock
    ModelMapper mockModelMapper;

    ModelMapper modelMapper = new ModelMapper();

    @Mock
    UserRepository userRepository;

    @Autowired
    @InjectMocks
    AnimeService animeService;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void findAll_shouldReturnAllAnimes() {
        Anime anime = AnimeFactory.create();
        Set<Anime> animes = Set.of(anime);
        Set<ShortAnimeDTO> shortAnimes = new HashSet<>();
        animes.forEach(ani -> shortAnimes.add(modelMapper.map(ani, ShortAnimeDTO.class)));

        when(animeRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(new ArrayList<>(animes)));
        when(mockModelMapper.map(any(Anime.class), eq(ShortAnimeDTO.class))).thenReturn(shortAnimes.iterator().next());

        Page<ShortAnimeDTO> result = animeService.findAll(Pageable.unpaged());

        assertThat(result).containsExactlyInAnyOrder(shortAnimes.toArray(new ShortAnimeDTO[0]));

        verify(animeRepository).findAll(any(Pageable.class));
        verify(mockModelMapper).map(any(Anime.class), eq(ShortAnimeDTO.class));

    }

    @Test
    void findById_shouldReturnAnime() {
        Anime anime = AnimeFactory.create();
        AnimeDTO animeDTO = modelMapper.map(anime, AnimeDTO.class);

        when(animeRepository.findById(any(Long.class))).thenReturn(java.util.Optional.of(anime));
        when(mockModelMapper.map(any(Anime.class), eq(AnimeDTO.class))).thenReturn(animeDTO);

        AnimeDTO result = animeService.findById(1L);

        assertThat(result).isEqualTo(animeDTO);

        verify(animeRepository).findById(any(Long.class));
        verify(mockModelMapper).map(any(Anime.class), eq(AnimeDTO.class));
    }

    @Test
    void like_shouldLikeAnime() {
        User user = UserFactory.create();
        Anime anime = AnimeFactory.create();

        JwtAuthenticationToken token = mock(JwtAuthenticationToken.class);
        when(token.getToken()).thenReturn(mock(Jwt.class));
        when(token.getToken().getSubject()).thenReturn(user.getEmail());
        when(animeRepository.findById(any(Long.class))).thenReturn(Optional.of(anime));
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));
        when(animeRepository.findById(any(Long.class))).thenReturn(Optional.of(anime));
        when(userRepository.findByEmail(any(String.class)))
                .thenReturn(Optional.of(user));

        animeService.like(1L, token);

        verify(animeRepository).findById(any(Long.class));
        verify(userRepository).findByEmail(any(String.class));
        verify(animeRepository).save(anime);
    }

    @Test
    void like_shouldUnlikeAnime() {
        User user = UserFactory.create();
        Anime anime = AnimeFactory.create();
        anime.getLikedBy().add(user);

        JwtAuthenticationToken token = mock(JwtAuthenticationToken.class);
        when(token.getToken()).thenReturn(mock(Jwt.class));
        when(token.getToken().getSubject()).thenReturn(user.getEmail());
        when(animeRepository.findById(any(Long.class))).thenReturn(Optional.of(anime));
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));
        when(animeRepository.findById(any(Long.class))).thenReturn(Optional.of(anime));
        when(userRepository.findByEmail(any(String.class)))
                .thenReturn(Optional.of(user));

        animeService.like(1L, token);

        verify(animeRepository).findById(any(Long.class));
        verify(userRepository).findByEmail(any(String.class));
        verify(animeRepository).save(anime);
    }

    @Test
    void findLikedAnimes_shouldReturnLikedAnimes() {
        User user = UserFactory.create();
        Anime anime = AnimeFactory.create();
        user.getLikedAnimes().add(anime);
        Set<ShortAnimeDTO> shortAnimes = new HashSet<>();
        shortAnimes.add(modelMapper.map(anime, ShortAnimeDTO.class));

        JwtAuthenticationToken token = mock(JwtAuthenticationToken.class);
        when(token.getToken()).thenReturn(mock(Jwt.class));
        when(token.getToken().getSubject()).thenReturn(user.getEmail());
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));
        when(mockModelMapper.map(any(Anime.class), eq(ShortAnimeDTO.class))).thenReturn(shortAnimes.iterator().next());

        Set<ShortAnimeDTO> result = animeService.findLikedAnimes(token);

        assertThat(result).containsExactlyInAnyOrder(shortAnimes.toArray(new ShortAnimeDTO[0]));

        verify(userRepository).findByEmail(any(String.class));
        verify(mockModelMapper).map(any(Anime.class), eq(ShortAnimeDTO.class));
    }
}
