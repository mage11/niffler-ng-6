package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.EditSpendingPage;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.PeoplePage;
import guru.qa.niffler.page.ProfilePage;
import io.qameta.allure.Step;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class Header {

    private final SelenideElement menuButton = $("button[aria-label='Menu']");
    private final SelenideElement profileOption = $("a[href='/profile']");
    private final SelenideElement friendsOption = $("a[href='/people/friends']");
    private final SelenideElement allPeopleOption = $("a[href='/people/all']");
    private final SelenideElement signOut = $(byText("Sign out"));
    private final SelenideElement logOutButton = $(byText("Log out"));
    private final SelenideElement addNewSpending = $("a[href='/spending']");
    private final SelenideElement main = $("a[href='/main']");

    @Step("Перейти на страницу Profile")
    public ProfilePage toProfilePage() {
        menuButton.click();
        profileOption.click();
        return new ProfilePage();
    }

    @Step("Перейти на страницу Friends")
    public FriendsPage toFriendsPage() {
        menuButton.click();
        friendsOption.click();
        return new FriendsPage();
    }

    @Step("Перейти на страницу People")
    public PeoplePage toPeoplePage() {
        menuButton.click();
        allPeopleOption.click();
        return new PeoplePage();
    }

    @Step("Разлогинить текущего юзера")
    public LoginPage signOut() {
        menuButton.click();
        signOut.click();
        logOutButton.click();
        return new LoginPage();
    }

    @Step("Перейти на страницу создания траты")
    public EditSpendingPage addNewSpendingPage() {
        addNewSpending.click();
        return new EditSpendingPage();
    }

    @Step("Перейти на главную страницу")
    public MainPage toMainPage() {
        main.click();
        return new MainPage();
    }
}
