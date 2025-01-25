package guru.qa.niffler.test.gql;

import com.apollographql.apollo.api.ApolloResponse;
import com.apollographql.apollo.api.Error;
import com.apollographql.java.client.ApolloCall;
import com.apollographql.java.rx2.Rx2Apollo;
import guru.qa.FriendsCategoriesQuery;
import guru.qa.FriendsOfFriendsQuery;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class FriendsGraphQlTest extends BaseGraphQlTest {
    @User(friends = 1)
    @Test
    @ApiLogin
    void shouldBeNotGetCategoriesOfFriends(@Token String bearerToken) {
        final ApolloCall<FriendsCategoriesQuery.Data> friendsCall = apolloClient.query(FriendsCategoriesQuery.builder()
                .page(0)
                .size(5)
                .build())
                .addHttpHeader("authorization", bearerToken);
        final ApolloResponse<FriendsCategoriesQuery.Data> response = Rx2Apollo.single(friendsCall).blockingGet();
        List<Error> errors = response.errors;

        Assertions.assertEquals("Can`t query categories for another user", errors.getFirst().getMessage());
    }

    @User(friends = 1)
    @Test
    @ApiLogin
    void shouldBeErrorWhenQueryOverManySubQueries(@Token String bearerToken) {
        final ApolloCall<FriendsOfFriendsQuery.Data> friendsCall = apolloClient.query(FriendsOfFriendsQuery.builder()
                .page(0)
                .size(5)
                .build())
            .addHttpHeader("authorization", bearerToken);
        final ApolloResponse<FriendsOfFriendsQuery.Data> response = Rx2Apollo.single(friendsCall).blockingGet();
        List<Error> errors = response.errors;

        Assertions.assertEquals("Can`t fetch over 2 friends sub-queries", errors.getFirst().getMessage());
    }

}
