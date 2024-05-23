package andrehsvictor.kanshi.util;

import andrehsvictor.kanshi.entity.User;
import net.datafaker.Faker;

public class UserFactory {
    private static Faker faker = new Faker();

    public static User create() {
        User user = new User();
        user.setUsername(faker.internet().username());
        user.setEmail(faker.internet().emailAddress());
        user.setPassword(faker.internet().password());
        return user;
    }
}
