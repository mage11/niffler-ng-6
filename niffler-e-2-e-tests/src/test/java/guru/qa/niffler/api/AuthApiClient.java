package guru.qa.niffler.api;

import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import retrofit2.Response;

import javax.annotation.Nonnull;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthApiClient extends RestClient {

    private final AuthApi authApi;

    public AuthApiClient(){
        super(CFG.authUrl());
        this.authApi = retrofit.create(AuthApi.class);
    }

    public void login(String username, String password){
        final Response<Void> response;
        try {
            response = authApi.login(username,
                password,
                ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN")).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(302, response.code());
    }

    public void getRegisterPage() {
        final Response<Void> response;
        try {
            response = authApi.registerPage().execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
    }

    public void registerUser(@Nonnull String username, @Nonnull String password){
        final Response<Void> response;
        try {
            response = authApi.registerUser(
                username,
                password,
                password,
                ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN")
            ).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(201, response.code());
    }

}
