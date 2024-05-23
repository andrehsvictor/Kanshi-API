package andrehsvictor.kanshi.dto;

import java.time.LocalDate;
import java.util.Set;

public class AnimeDTO {
    private Long id;
    private String title;
    private String description;
    private Integer episodes;
    private Integer seasons;
    private Boolean finished;
    private LocalDate releaseDate;
    private String imageUrl;
    private Set<UserDTO> likedBy;
    private Set<GenreDTO> genres;
    private Set<ShortRecommendationDTO> recommendations;

    public AnimeDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getEpisodes() {
        return episodes;
    }

    public void setEpisodes(Integer episodes) {
        this.episodes = episodes;
    }

    public Integer getSeasons() {
        return seasons;
    }

    public void setSeasons(Integer seasons) {
        this.seasons = seasons;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Set<UserDTO> getLikedBy() {
        return likedBy;
    }

    public void setLikedBy(Set<UserDTO> likedBy) {
        this.likedBy = likedBy;
    }

    public Set<GenreDTO> getGenres() {
        return genres;
    }

    public void setGenres(Set<GenreDTO> genres) {
        this.genres = genres;
    }

    public Set<ShortRecommendationDTO> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(Set<ShortRecommendationDTO> recommendations) {
        this.recommendations = recommendations;
    }

    

}
