package andrehsvictor.kanshi.dto;

public class SaveOrUpdateRecommendationDTO {
    private String comment;
    private Double rating;

    public SaveOrUpdateRecommendationDTO() {
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
