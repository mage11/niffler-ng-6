package guru.qa.niffler.test.rest;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.RestTest;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import guru.qa.niffler.model.rest.FriendJson;
import guru.qa.niffler.model.rest.FriendState;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.service.impl.GatewayApiClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import java.util.List;
@RestTest
public class FriendsTest {
    @RegisterExtension
    private static final ApiLoginExtension apiLoginExtension = ApiLoginExtension.rest();
    private final GatewayApiClient gatewayApiClient = new GatewayApiClient();
    @User(friends = 2, incomeInvitations = 1)
    @ApiLogin
    @Test
    void allFriendsAndIncomeInvitationsShouldBeReturnedFroUser(UserJson user, @Token String token) {
        final List<UserJson> expectedFriends = user.testData().friends();
        final List<UserJson> expectedInvitations = user.testData().incomeInvitations();
        final List<UserJson> result = gatewayApiClient.allFriends(
            token,
            null
        );
        Assertions.assertNotNull(result);
        Assertions.assertEquals(3, result.size());
        final List<UserJson> friendsFromResponse = result.stream().filter(
            u -> u.friendState() == FriendState.FRIEND
        ).toList();
        final List<UserJson> invitationsFromResponse = result.stream().filter(
            u -> u.friendState() == FriendState.INVITE_RECEIVED
        ).toList();
        Assertions.assertEquals(2, friendsFromResponse.size());
        Assertions.assertEquals(1, invitationsFromResponse.size());
        Assertions.assertEquals(
            expectedInvitations.getFirst().username(),
            invitationsFromResponse.getFirst().username()
        );
        final UserJson firstUserFromRequest = friendsFromResponse.getFirst();
        final UserJson secondUserFromRequest = friendsFromResponse.getLast();
        Assertions.assertEquals(
            expectedFriends.getFirst().username(),
            firstUserFromRequest.username()
        );
        Assertions.assertEquals(
            expectedFriends.getLast().username(),
            secondUserFromRequest.username()
        );
    }

    @User(friends = 2)
    @ApiLogin
    @Test
    void friendshipShouldBeRemoved(@Token String token){
        final List<UserJson> friendsBefore = gatewayApiClient.allFriends(token, null);
        Assertions.assertEquals(2, friendsBefore.size());

        final UserJson friend = friendsBefore.getFirst();
        gatewayApiClient.removeFriend(token, friend.username());
        final List<UserJson> friendsAfter = gatewayApiClient.allFriends(token, null);
        Assertions.assertEquals(1, friendsBefore.size());
        Assertions.assertNotEquals(friend.username(), friendsBefore.getFirst().username());
    }

    @User(incomeInvitations = 1)
    @ApiLogin
    @Test
    void invitationShouldBeAccepted(@Token String token) {
        final List<UserJson> invitationsBefore = gatewayApiClient.allFriends(token, null);
        Assertions.assertEquals(1, invitationsBefore.size());

        final UserJson invitation = invitationsBefore.getFirst();
        gatewayApiClient.acceptInvitation(token, new FriendJson(invitation.username()));
        final List<UserJson> friendsAfter = gatewayApiClient.allFriends(token, null);
        Assertions.assertEquals(1, friendsAfter.size());
        Assertions.assertEquals(FriendState.FRIEND, friendsAfter.getFirst().friendState());
    }

    @User(incomeInvitations = 1)
    @ApiLogin
    @Test
    void invitationShouldBeDeclined(@Token String token) {
        final List<UserJson> invitationsBefore = gatewayApiClient.allFriends(token, null);
        Assertions.assertEquals(1, invitationsBefore.size());

        final UserJson invitation = invitationsBefore.getFirst();
        gatewayApiClient.declineInvitation(token, new FriendJson(invitation.username()));
        final List<UserJson> invitationsAfter = gatewayApiClient.allFriends(token, null);
        Assertions.assertTrue(invitationsAfter.isEmpty());
    }

    @User(outcomeInvitations = 1)
    @ApiLogin
    @Test
    void outcomeInviteShouldBeExist(UserJson user, @Token String token){
        final FriendJson friendship = new FriendJson(user.testData().outcomeInvitations().getFirst().username());
        gatewayApiClient.sendInvitation(token, friendship);

        final List<UserJson> invitesCurrentUser = gatewayApiClient.allUsers(
            token,
            user.testData().outcomeInvitations().getFirst().username());
        Assertions.assertEquals(invitesCurrentUser.getFirst().friendState(), FriendState.INVITE_SENT);
    }
}
