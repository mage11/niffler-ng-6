package guru.qa.niffler.test.web;

import guru.qa.niffler.api.UserApiClient;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

@Order(Integer.MAX_VALUE)
public class LastTest {

    private final UserApiClient userApiClient = new UserApiClient();

    @User
    @Test
    void userListShouldNotBeEmpty(UserJson user){
        assertFalse(userApiClient.allUsers(user).isEmpty());
    }

}
