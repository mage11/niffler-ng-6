package guru.qa.niffler.api;

import com.google.common.base.Stopwatch;
import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.service.UsersClient;
import guru.qa.niffler.utils.RandomDataUtils;
import io.qameta.allure.Step;
import org.apache.kafka.common.errors.TimeoutException;
import retrofit2.Response;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ParametersAreNonnullByDefault
public class UserApiClient extends RestClient implements UsersClient {

    private final UserApi userApi;
    private final AuthApiClient authApiClient = new AuthApiClient();
    private static final String defaultPassword = "12345";

    public UserApiClient() {
        super(CFG.userdataUrl());
        this.userApi = retrofit.create(UserApi.class);
    }

    @Nullable
    @Step("Получение текущего пользователя по имени {username}")
    public UserJson getCurrentUser(String username) {
        final Response<UserJson> response;
        try {
            response = userApi.currentUser(username).execute();
        }
        catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    public List<UserJson> allUsers(UserJson user){
        final Response<List<UserJson>> response;
        try {
            response = userApi.allUsers(user.username(), null).execute();
        }
        catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body() == null
            ? Collections.emptyList()
            : response.body();
    }

    @Override
    @Step("Создание нового пользователя с именем {username}")
    @Nonnull
    public UserJson createUser(@Nonnull String username, @Nonnull String password) {
        try {
            authApiClient.getRegisterPage();
            authApiClient.registerUser(
                username,
                password
            );

            Stopwatch sw = Stopwatch.createStarted();
            long maxWaitTime = 2000;
            while (sw.elapsed(TimeUnit.MILLISECONDS) < maxWaitTime) {
                UserJson userJson = getCurrentUser(username);
                if (userJson != null && userJson.id() != null) {
                    return userJson.addTestData(new TestData(password));
                }
                else {
                    Thread.sleep(100);
                }
            }
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        throw new TimeoutException("Пользователь " + username + " не был найден за отведённое время");
    }

    @Override
    public List<UserJson> addIncomeInvitation(UserJson targetUser, int count) {
        if (count > 0) {
            List<UserJson> incomeUsers = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                String randomUserName = RandomDataUtils.randomUserName();
                UserJson createdUser;
                createdUser = createUser(randomUserName, defaultPassword);
                sendInvitation(createdUser.username(), targetUser.username());
                incomeUsers.add(createdUser);
            }
            return incomeUsers;
        }
        return Collections.emptyList();
    }

    @Override
    public List<UserJson> addOutcomeInvitation(UserJson targetUser, int count) {
        if (count > 0) {
            List<UserJson> outcomeUsers = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                String randomUserName = RandomDataUtils.randomUserName();
                UserJson createdUser;
                createdUser = createUser(randomUserName, defaultPassword);
                sendInvitation(targetUser.username(), createdUser.username());
                outcomeUsers.add(createdUser);
            }
            return outcomeUsers;
        }
        return Collections.emptyList();
    }

    @Override
    public void addFriend(UserJson required, UserJson addressee) {
        sendInvitation(addressee.username(), required.username());
        acceptInvitation(addressee.username(), required.username());
    }

    @Override
    public List<UserJson> addFriend(UserJson targetUser, int count) {
        if (count > 0) {
            List<UserJson> friends = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                String randomUserName = RandomDataUtils.randomUserName();
                UserJson createdUser;
                createdUser = createUser(randomUserName, defaultPassword);
                sendInvitation(targetUser.username(), createdUser.username());
                acceptInvitation(targetUser.username(), createdUser.username());
                friends.add(createdUser);
            }
            return friends;
        }
        return Collections.emptyList();
    }

    @Step("Отправка приглашения от пользователя {username} пользователю {targetUsername}")
    @Nullable
    public UserJson sendInvitation(@Nonnull String username, @Nonnull String targetUsername) {
        final Response<UserJson> response;
        try {
            response = userApi.sendInvitation(username,
                    targetUsername)
                .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        System.out.println(response.headers());
        assertEquals(200, response.code());
        return response.body();
    }

    @Step("Принятие приглашения от пользователя {username} пользователю {targetUsername}")
    @Nullable
    public UserJson acceptInvitation(@Nonnull String username, @Nonnull String targetUsername) {
        final Response<UserJson> response;
        try {
            response = userApi.acceptInvitation(username, targetUsername)
                .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    @Step("Получение входящих приглашений юзера {username}")
    @Nonnull
    public List<UserJson> getIncomeInvitations(String username, String searchQuery) {
        final Response<List<UserJson>> response;
        try {
            response = userApi.incomeInvitations(username, searchQuery)
                .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body() == null
            ? Collections.emptyList()
            : response.body();
    }

    @Step("Получение исходящих приглашений юзера {username}")
    @Nonnull
    public List<UserJson> getOutcomeInvitations(String username, String searchQuery) {
        final Response<List<UserJson>> response;
        try {
            response = userApi.outcomeInvitations(username, searchQuery)
                .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body() == null
            ? Collections.emptyList()
            : response.body();
    }

    @Step("Получение всех друзей юзера {username}")
    @Nonnull
    public List<UserJson> getAllFriends(String username, String searchQuery) {
        final Response<List<UserJson>> response;
        try {
            response = userApi.allFriends(username, searchQuery)
                .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body() == null
            ? Collections.emptyList()
            : response.body();
    }

}
