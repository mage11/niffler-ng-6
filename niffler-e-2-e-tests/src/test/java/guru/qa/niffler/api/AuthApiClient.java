package guru.qa.niffler.api;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.api.core.CodeInterceptor;
import guru.qa.niffler.api.core.CodeStore;
import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.utils.OauthUtils;
import retrofit2.Response;

import javax.annotation.Nonnull;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthApiClient extends RestClient {

    private final AuthApi authApi;
    private static final String RESPONSE_TYPE = "code";
    private static final String CLIENT_ID = "client";
    private static final String SCOPE = "openid";
    private static final String REDIRECT_URI = CFG.frontUrl() + "authorized";
    private static final String CODE_CHALLENGE_METHOD = "S256";
    private static final String GRANT_TYPE = "authorization_code";

    public AuthApiClient(){
        super(CFG.authUrl(), true, new CodeInterceptor());
        this.authApi = retrofit.create(AuthApi.class);
    }

    public String login(String username, String password) {
        Response<JsonNode> response;
        final String codeVerifier = OauthUtils.generateCodeVerifier();
        final String codeChallenge = OauthUtils.generateCodeChallenge(codeVerifier);
        try {

            authApi.authorize(
                RESPONSE_TYPE,
                CLIENT_ID,
                SCOPE,
                REDIRECT_URI,
                codeChallenge,
                CODE_CHALLENGE_METHOD
            ).execute();

            authApi.login(
                username,
                password,
                ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN")
            ).execute();

            response = authApi.token(
                CLIENT_ID,
                REDIRECT_URI,
                GRANT_TYPE,
                CodeStore.getCode(),
                codeVerifier
            ).execute();

        } catch (IOException e) {
            throw new AssertionError(e);
        }

        return response.body().get("id_token").asText();
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
