package guru.qa.niffler.service.impl;

import guru.qa.jaxb.userdata.AcceptInvitationRequest;
import guru.qa.jaxb.userdata.CurrentUserRequest;
import guru.qa.jaxb.userdata.DeclineInvitationRequest;
import guru.qa.jaxb.userdata.FriendsPageRequest;
import guru.qa.jaxb.userdata.RemoveFriendRequest;
import guru.qa.jaxb.userdata.SendInvitationRequest;
import guru.qa.jaxb.userdata.UserResponse;
import guru.qa.jaxb.userdata.UsersResponse;
import guru.qa.niffler.api.UserdataSoapApi;
import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.api.core.converter.SoapConverterFactory;
import guru.qa.niffler.config.Config;
import io.qameta.allure.Step;

import okhttp3.logging.HttpLoggingInterceptor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;

@ParametersAreNonnullByDefault
public class UserdataSoapClient extends RestClient {

  private static final Config CFG = Config.getInstance();
  private final UserdataSoapApi userdataSoapApi;

  public UserdataSoapClient() {
    super(CFG.userdataUrl(), false, SoapConverterFactory.create("niffler-userdata"), HttpLoggingInterceptor.Level.BODY);
    this.userdataSoapApi = create(UserdataSoapApi.class);
  }

  @NotNull
  @Step("Get current user info using SOAP")
  public UserResponse currentUser(CurrentUserRequest request) {
      try {
          return userdataSoapApi.currentUser(request).execute().body();
      }
      catch (IOException e) {
          throw new RuntimeException(e);
      }
  }

  @NotNull
  @Step("Get friends using SOAP")
  public UsersResponse getFriends(FriendsPageRequest request) {
      try {
          return userdataSoapApi.getFriends(request).execute().body();
      }
      catch (IOException e) {
          throw new RuntimeException(e);
      }
  }

  @Step("Remove friend using SOAP")
  public void removeFriend(RemoveFriendRequest request) {
      try {
          userdataSoapApi.removeFriend(request).execute();
      }
      catch (IOException e) {
          throw new RuntimeException(e);
      }
  }

  @NotNull
  @Step("Send invitation using SOAP")
  public UserResponse sendInvitation(SendInvitationRequest request) {
      try {
          return userdataSoapApi.sendInvitation(request).execute().body();
      }
      catch (IOException e) {
          throw new RuntimeException(e);
      }
  }

  @NotNull
  @Step("Accept invitation using SOAP")
  public UserResponse acceptInvitation(AcceptInvitationRequest request) {
      try {
          return userdataSoapApi.acceptInvitation(request).execute().body();
      }
      catch (IOException e) {
          throw new RuntimeException(e);
      }
  }

  @NotNull
  @Step("Decline invitation using SOAP")
  public UserResponse declineInvitation(DeclineInvitationRequest request) {
      try {
          return userdataSoapApi.declineInvitation(request).execute().body();
      }
      catch (IOException e) {
          throw new RuntimeException(e);
      }
  }
}
