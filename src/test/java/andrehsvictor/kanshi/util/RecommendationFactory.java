package andrehsvictor.kanshi.util;

import andrehsvictor.kanshi.entity.Recommendation;
import net.datafaker.Faker;

public class RecommendationFactory {
    private static Faker faker = new Faker();

    public static Recommendation create() {
        Recommendation recommendation = new Recommendation();
        recommendation.setComment(faker.lorem().sentence());
        recommendation.setRating(faker.number().randomDouble(1, 0, 10));
        return recommendation;
    }
}
