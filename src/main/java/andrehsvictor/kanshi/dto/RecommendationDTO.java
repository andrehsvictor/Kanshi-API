package andrehsvictor.kanshi.dto;

public class RecommendationDTO {
    private Long id;
    private ShortAnimeDTO anime;
    private UserDTO user;
    private String comment;
    private Double rating;

    public RecommendationDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ShortAnimeDTO getAnime() {
        return anime;
    }

    public void setAnime(ShortAnimeDTO anime) {
        this.anime = anime;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    
}
