package guru.qa.niffler.test.web;

import guru.qa.niffler.api.UserApiClient;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.rest.UserJson;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@WebTest
@Order(1)
public class FirstTest {

    private final UserApiClient userApiClient = new UserApiClient();

    @User
    @Test
    void userListShouldBeEmpty(UserJson user){
        assertTrue(userApiClient.allUsers(user).isEmpty());
    }
}
