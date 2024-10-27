package guru.qa.niffler.utils;

import com.github.javafaker.Faker;

import javax.annotation.Nonnull;

public class RandomDataUtils {
    private static final Faker faker = new Faker();

    @Nonnull
    public static String randomUserName(){
        return  faker.name().username();
    }

    @Nonnull
    public static String randomName(){
        return faker.name().name();
    }

    @Nonnull
    public static String randomSurname(){
        return faker.name().lastName();
    }

    @Nonnull
    public static String randomCategoryName(){
        return faker.random().hex();
    }

    @Nonnull
    public static String randomPassword(){
        return faker.internet().password();
    }
}
