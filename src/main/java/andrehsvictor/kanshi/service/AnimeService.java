package andrehsvictor.kanshi.service;

import java.util.HashSet;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import andrehsvictor.kanshi.dto.AnimeDTO;
import andrehsvictor.kanshi.dto.ShortAnimeDTO;
import andrehsvictor.kanshi.entity.Anime;
import andrehsvictor.kanshi.entity.User;
import andrehsvictor.kanshi.repository.AnimeRepository;
import andrehsvictor.kanshi.repository.UserRepository;

@Service
public class AnimeService {

    @Autowired
    private AnimeRepository animeRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    public Page<ShortAnimeDTO> findAll(Pageable pageable) {
        Page<Anime> animes = animeRepository.findAll(pageable);
        return animes.map(anime -> modelMapper.map(anime, ShortAnimeDTO.class));
    }

    public AnimeDTO findById(Long id) {
        Anime anime = animeRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Anime not found"));
        return modelMapper.map(anime, AnimeDTO.class);
    }

    public void like(Long id, JwtAuthenticationToken token) {
        Anime anime = animeRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Anime not found"));
        User user = userRepository.findByEmail(token.getToken().getSubject()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (user.getLikedAnimes().contains(anime)) {
            anime.getLikedBy().remove(user);
            animeRepository.save(anime);
            return;
        }
        anime.getLikedBy().add(user);
        animeRepository.save(anime);
    }

    public Set<ShortAnimeDTO> findLikedAnimes(JwtAuthenticationToken token) {
        User user = userRepository.findByEmail(token.getToken().getSubject()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Set<Anime> likedAnimes = user.getLikedAnimes();
        Set<ShortAnimeDTO> likedAnimesDTO = new HashSet<>();
        likedAnimes.forEach(anime -> likedAnimesDTO.add(modelMapper.map(anime, ShortAnimeDTO.class)));
        return likedAnimesDTO;
    }

}
