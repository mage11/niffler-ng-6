package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.StaticUser;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@WebTest
@ExtendWith({BrowserExtension.class, UsersQueueExtension.class})
public class FriendsWebTest {

    private static final Config CFG = Config.getInstance();

    @Test
    void friendShouldBePresentInFriendsTable(
        @UserType(UserType.Type.WITH_FRIENDS) StaticUser staticUser){

        Selenide.open(CFG.frontUrl(), LoginPage.class)
            .login(staticUser.username(), staticUser.password())
            .clickToFriendsPage()
            .panelFriendsShouldBeExist()
            .foundFriendShouldBeExistInFriendList(staticUser.friend());
    }

    @Test
    void friendShouldBeEmptyInFriendsTable(
        @UserType(UserType.Type.EMPTY) StaticUser staticUser){

        Selenide.open(CFG.frontUrl(), LoginPage.class)
            .login(staticUser.username(), staticUser.password())
            .clickToFriendsPage()
            .panelFriendsShouldBeExist()
            .friendListShouldBeEmpty();
    }

    @Test
    void incomeInvitationBePresentFriendsTable(
        @UserType(UserType.Type.WITH_INCOME_REQUEST) StaticUser staticUser){

        Selenide.open(CFG.frontUrl(), LoginPage.class)
            .login(staticUser.username(), staticUser.password())
            .clickToFriendsPage()
            .panelFriendsShouldBeExist()
            .foundUserShouldBeExistInRequestList(staticUser.income());
    }

    @Test
    void outcomeInvitationBePresentFriendsTable(
        @UserType(UserType.Type.WITH_OUTCOME_REQUEST) StaticUser staticUser){

        Selenide.open(CFG.frontUrl(), LoginPage.class)
            .login(staticUser.username(), staticUser.password())
            .clickToFriendsPage()
            .panelFriendsShouldBeExist()
            .clickToAllPeopleButton()
            .foundUserInAllPeopleListShouldHaveOutcomeStatus(staticUser.outcome());

    }

    @User(
        incomeInvitations = 1
    )
    @ApiLogin
    @Test
    void acceptInviteFriend(UserJson user){
        Selenide.open(FriendsPage.URL, FriendsPage.class)
            .acceptInviteFromUser(user.testData().incomeInvitations().get(0).username())
            .foundFriendShouldBeExistInFriendList(user.testData().incomeInvitations().get(0).username());
    }

    @User(
        incomeInvitations = 1
    )
    @ApiLogin
    @Test
    void declineInviteFromUser(UserJson user){
        Selenide.open(FriendsPage.URL, FriendsPage.class)
            .declineInviteFromUser(user.testData().incomeInvitations().get(0).username())
            .friendListShouldBeEmpty();
    }



}
