package andrehsvictor.kanshi.util;

import andrehsvictor.kanshi.entity.Anime;
import net.datafaker.Faker;

public class AnimeFactory {

    private static Faker faker = new Faker();

    public static Anime create() {
        Anime anime = new Anime();
        anime.setTitle(faker.naruto().character());
        anime.setDescription(faker.lorem().sentence());
        anime.setEpisodes(faker.number().randomDigit());
        anime.setSeasons(faker.number().randomDigit());
        anime.setReleaseDate(faker.date().birthday().toLocalDateTime().toLocalDate());
        anime.setImageUrl(faker.internet().image());
        return anime;
    }
}
