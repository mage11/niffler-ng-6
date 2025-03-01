package guru.qa.niffler.test.kafka;

import guru.qa.niffler.api.AuthApiClient;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.jupiter.annotation.meta.KafkaTest;
import guru.qa.niffler.utils.DatabaseObserver;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

@KafkaTest
public class UserdataKafkaTest {

    private final AuthApiClient authApi = new AuthApiClient();

    @Test
    void test() throws IOException {
        final String username = RandomDataUtils.randomUserName();
        final String password = "12345";

        authApi.getRegisterPage();
        authApi.registerUser(
            username,
            password
        );

        UserEntity userEntity = DatabaseObserver.getUserFromDbByUsername(username);
        System.out.println(username);
        Assertions.assertNotNull(userEntity);
        Assertions.assertEquals(username, userEntity.getUsername());
    }

}
