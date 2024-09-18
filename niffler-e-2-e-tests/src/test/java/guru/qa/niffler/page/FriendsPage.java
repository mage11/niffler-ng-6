package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.github.javafaker.Friends;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class FriendsPage {

    private final SelenideElement friendsButton = $("a[href='/people/friends']");
    private final SelenideElement allPeopleButton = $("a[href='/people/all']");
    private final SelenideElement panelFriends = $("#simple-tabpanel-friends");
    private final SelenideElement searchInput = $(By.xpath("//input[@placeholder='Search']"));
    private final ElementsCollection allPeopleList = $$("tbody>tr");
    private final ElementsCollection friendList =
        $$(By.xpath("//h2[text()='My friends']/following-sibling::table/tbody/tr"));
    private final ElementsCollection requestsList =
        $$(By.xpath("//h2[text()='Friend requests']/following-sibling::table/tbody/tr"));

    public FriendsPage panelFriendsShouldBeExist(){

        panelFriends.shouldBe(exist);
        return this;
    }

    public FriendsPage foundFriendShouldBeExistInFriendList(String friendName){

        String buttonName = "Unfriend";

        searchInput.setValue(friendName).pressEnter();
        friendList.find(text(friendName))
            .shouldBe(visible)
            .shouldHave(text(buttonName));
        return this;
    }

    public FriendsPage friendListShouldBeEmpty(){

        String defaultTitleWhenFriendListIsEmpty = "There are no users yet";
        panelFriends.shouldHave(text(defaultTitleWhenFriendListIsEmpty));
        return this;
    }

    public FriendsPage foundUserShouldBeExistInRequestList(String userName){

        String buttonAcceptText = "Accept";
        String buttonDeclineText = "Decline";
        requestsList.find(text(userName))
            .shouldBe(visible)
            .shouldHave(text(buttonAcceptText))
            .shouldHave(text(buttonDeclineText));
        return this;
    }

    public FriendsPage clickToAllPeopleButton(){

        allPeopleButton.click();
        return this;
    }

    public FriendsPage foundUserInAllPeopleListShouldHaveOutcomeStatus(String userName){

        String outcomeStatusText = "Waiting...";
        searchInput.setValue(userName).pressEnter();
        allPeopleList.find(text(userName))
            .shouldBe(visible)
            .shouldHave(text(outcomeStatusText));
        return this;
    }

}
