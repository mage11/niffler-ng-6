package guru.qa.niffler.test.soap;

import guru.qa.jaxb.userdata.AcceptInvitationRequest;
import guru.qa.jaxb.userdata.CurrentUserRequest;
import guru.qa.jaxb.userdata.DeclineInvitationRequest;
import guru.qa.jaxb.userdata.FriendsPageRequest;
import guru.qa.jaxb.userdata.FriendshipStatus;
import guru.qa.jaxb.userdata.PageInfo;
import guru.qa.jaxb.userdata.RemoveFriendRequest;
import guru.qa.jaxb.userdata.SendInvitationRequest;
import guru.qa.jaxb.userdata.UserResponse;
import guru.qa.jaxb.userdata.UsersResponse;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.SoapTest;

import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.service.impl.UserdataSoapClient;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.stream.Stream;

@SoapTest
public class SoapUsersTest {

  private final UserdataSoapClient userdataSoapClient = new UserdataSoapClient();

  @Test
  @User
  void currentUserTest(UserJson user) {
    CurrentUserRequest request = new CurrentUserRequest();
    request.setUsername(user.username());
    UserResponse response = userdataSoapClient.currentUser(request);
    Assertions.assertEquals(
        user.username(),
        response.getUser().getUsername()
    );
  }

  @Test
  @User(incomeInvitations = 1)
  void invitationShouldBeSent(UserJson user) {
    SendInvitationRequest request = new SendInvitationRequest();
    request.setUsername(user.username());
    request.setFriendToBeRequested(user.testData().incomeInvitations().getFirst().username());
    UserResponse response = userdataSoapClient.sendInvitation(request);
    Assertions.assertEquals(FriendshipStatus.INVITE_SENT, response.getUser().getFriendshipStatus());
  }

  @Test
  @User(incomeInvitations = 1)
  void invitationShouldBeAccepted(UserJson user) {
    AcceptInvitationRequest request = new AcceptInvitationRequest();
    request.setUsername(user.username());
    request.setFriendToBeAdded(user.testData().incomeInvitations().getFirst().username());
    UserResponse response = userdataSoapClient.acceptInvitation(request);
    Assertions.assertEquals(FriendshipStatus.FRIEND, response.getUser().getFriendshipStatus());
  }

  @Test
  @User(incomeInvitations = 1)
  void invitationShouldBeDeclined(UserJson user) {
    DeclineInvitationRequest request = new DeclineInvitationRequest();
    request.setUsername(user.username());
    request.setInvitationToBeDeclined(user.testData().incomeInvitations().getFirst().username());
    UserResponse response = userdataSoapClient.declineInvitation(request);
    Assertions.assertEquals(FriendshipStatus.VOID, response.getUser().getFriendshipStatus());
  }

  @Test
  @User(friends = 1)
  void friendshipShouldBeRemoved(UserJson user) {
    RemoveFriendRequest request = new RemoveFriendRequest();
    request.setUsername(user.username());
    request.setFriendToBeRemoved(user.testData().friends().getFirst().username());
    userdataSoapClient.removeFriend(request);
    CurrentUserRequest currentUserRequest = new CurrentUserRequest();
    currentUserRequest.setUsername(user.username());
    UserResponse response = userdataSoapClient.currentUser(currentUserRequest);
    Assertions.assertEquals(FriendshipStatus.VOID, response.getUser().getFriendshipStatus());
  }

  @ParameterizedTest
  @MethodSource("provideTestData")
  @User(friends = 7)
  void friendsShouldBeReceived(int page, int size, int expectedCountFriends, UserJson user) {
    FriendsPageRequest request = new FriendsPageRequest();
    PageInfo pageInfo = new PageInfo();
    pageInfo.setPage(page);
    pageInfo.setSize(size);
    request.setUsername(user.username());
    request.setPageInfo(pageInfo);
    UsersResponse response = userdataSoapClient.getFriends(request);
    Assertions.assertEquals(expectedCountFriends, response.getUser().size());
  }

  @Test
  @User(friends = 3)
  void targetFriendShouldBeReceived( UserJson user) {
    FriendsPageRequest request = new FriendsPageRequest();
    PageInfo pageInfo = new PageInfo();
    pageInfo.setPage(0);
    pageInfo.setSize(3);
    request.setUsername(user.username());
    request.setPageInfo(pageInfo);
    String targetUsernameFriend = user.testData().friends().getFirst().username();
    request.setSearchQuery(targetUsernameFriend);
    UsersResponse response = userdataSoapClient.getFriends(request);
    Assertions.assertEquals(1, response.getUser().size());
    Assertions.assertEquals(targetUsernameFriend, response.getUser().getFirst().getUsername());
  }

  private static Stream<Arguments> provideTestData() {
    return Stream.of(
        Arguments.of(0, 4, 4),
        Arguments.of(1, 4, 3),
        Arguments.of(0, 7, 7),
        Arguments.of(0, 8, 7)
    );
  }
}
