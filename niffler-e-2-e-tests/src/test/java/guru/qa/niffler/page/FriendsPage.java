package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.SearchField;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@ParametersAreNonnullByDefault
public class FriendsPage extends BasePage<FriendsPage> {

    public static final String URL = CFG.frontUrl() + "people/friends";
    private final SelenideElement friendsButton = $("a[href='/people/friends']");
    private final SelenideElement allPeopleButton = $("a[href='/people/all']");
    private final SelenideElement panelFriends = $("#simple-tabpanel-friends");
    private final ElementsCollection allPeopleList = $$("tbody>tr");
    private final ElementsCollection friendList =
        $$(By.xpath("//h2[text()='My friends']/following-sibling::table/tbody/tr"));
    private final ElementsCollection requestsList =
        $$(By.xpath("//h2[text()='Friend requests']/following-sibling::table/tbody/tr"));
    private final SelenideElement declineButtonInContainer =
        $(By.xpath("//*[starts-with(@class, 'MuiDialog-container')]"))
            .$(By.xpath("*//button[text()='Decline']"));
    private final SearchField searchField = new SearchField();

    @Step("Проверить существование влкадки друзей")
    public FriendsPage panelFriendsShouldBeExist() {

        panelFriends.shouldBe(exist);
        return this;
    }

    @Step("Проверить присутствие друга в списке друзей {friendName}")
    public FriendsPage foundFriendShouldBeExistInFriendList(String friendName) {

        String buttonName = "Unfriend";

        searchField.search(friendName);
        friendList.find(text(friendName))
            .shouldBe(visible)
            .shouldHave(text(buttonName));
        return this;
    }

    @Step("Список друзей должен быть пуст")
    public FriendsPage friendListShouldBeEmpty() {

        String defaultTitleWhenFriendListIsEmpty = "There are no users yet";
        panelFriends.shouldHave(text(defaultTitleWhenFriendListIsEmpty));
        return this;
    }

    @Step("Должна быть заявка от {userName}")
    public FriendsPage foundUserShouldBeExistInRequestList(String userName) {

        String buttonAcceptText = "Accept";
        String buttonDeclineText = "Decline";
        requestsList.find(text(userName))
            .shouldBe(visible)
            .shouldHave(text(buttonAcceptText))
            .shouldHave(text(buttonDeclineText));
        return this;
    }

    @Step("Принять заявку от {username}")
    public FriendsPage acceptInviteFromUser(String username) {
        searchField.search(username);
        requestsList.find(text(username))
            .shouldBe(visible)
            .$(By.xpath("*//button[text()='Accept']")).click();
        return this;
    }

    @Step("Отклонить заявку от {username}")
    public FriendsPage declineInviteFromUser(String username) {
        searchField.search(username);
        requestsList.find(text(username))
            .shouldBe(visible)
            .$(By.xpath("*//button[text()='Decline']")).click();
        declineButtonInContainer.click();
        return this;
    }

    @Step("Открыть влкадку People")
    public FriendsPage clickToAllPeopleButton() {

        allPeopleButton.click();
        return this;
    }

    @Step("Найти исходящию заявку по юзеру {userName}")
    public FriendsPage foundUserInAllPeopleListShouldHaveOutcomeStatus(String userName) {

        String outcomeStatusText = "Waiting...";
        searchField.search(userName);
        allPeopleList.find(text(userName))
            .shouldBe(visible)
            .shouldHave(text(outcomeStatusText));
        return this;
    }

}
