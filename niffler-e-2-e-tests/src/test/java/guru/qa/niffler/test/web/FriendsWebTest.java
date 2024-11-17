package guru.qa.niffler.test.web;

import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.StaticUser;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.utils.SelenideUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({BrowserExtension.class, UsersQueueExtension.class})
public class FriendsWebTest {
    private final SelenideDriver driver = new SelenideDriver(SelenideUtils.chromeConfig);
    private static final Config CFG = Config.getInstance();

    @Test
    void friendShouldBePresentInFriendsTable(
        @UserType(UserType.Type.WITH_FRIENDS) StaticUser staticUser){

        driver.open(CFG.frontUrl(), LoginPage.class)
            .login(staticUser.username(), staticUser.password())
            .clickToFriendsPage()
            .panelFriendsShouldBeExist()
            .foundFriendShouldBeExistInFriendList(staticUser.friend());
    }

    @Test
    void friendShouldBeEmptyInFriendsTable(
        @UserType(UserType.Type.EMPTY) StaticUser staticUser){

        driver.open(CFG.frontUrl(), LoginPage.class)
            .login(staticUser.username(), staticUser.password())
            .clickToFriendsPage()
            .panelFriendsShouldBeExist()
            .friendListShouldBeEmpty();
    }

    @Test
    void incomeInvitationBePresentFriendsTable(
        @UserType(UserType.Type.WITH_INCOME_REQUEST) StaticUser staticUser){

        driver.open(CFG.frontUrl(), LoginPage.class)
            .login(staticUser.username(), staticUser.password())
            .clickToFriendsPage()
            .panelFriendsShouldBeExist()
            .foundUserShouldBeExistInRequestList(staticUser.income());
    }

    @Test
    void outcomeInvitationBePresentFriendsTable(
        @UserType(UserType.Type.WITH_OUTCOME_REQUEST) StaticUser staticUser){

        driver.open(CFG.frontUrl(), LoginPage.class)
            .login(staticUser.username(), staticUser.password())
            .clickToFriendsPage()
            .panelFriendsShouldBeExist()
            .clickToAllPeopleButton()
            .foundUserInAllPeopleListShouldHaveOutcomeStatus(staticUser.outcome());

    }

    @User(
        incomeFriend = true
    )
    @Test
    void acceptInviteFriend(UserJson user){
        driver.open(CFG.frontUrl(), LoginPage.class)
            .login(user.username(), user.testData().password())
            .headersBlocksShouldBeExists()
            .clickToFriendsPage()
            .acceptInviteFromUser(user.testData().incomeFriends().get(0).username())
            .foundFriendShouldBeExistInFriendList(user.testData().incomeFriends().get(0).username());
    }

    @User(
        incomeFriend = true
    )
    @Test
    void declineInviteFromUser(UserJson user){
        driver.open(CFG.frontUrl(), LoginPage.class)
            .login(user.username(), user.testData().password())
            .headersBlocksShouldBeExists()
            .clickToFriendsPage()
            .declineInviteFromUser(user.testData().incomeFriends().get(0).username())
            .friendListShouldBeEmpty();
    }



}
