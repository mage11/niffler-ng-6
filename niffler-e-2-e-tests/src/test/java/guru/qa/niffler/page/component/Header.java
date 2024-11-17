package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.PeoplePage;
import guru.qa.niffler.page.ProfilePage;
import guru.qa.niffler.page.EditSpendingPage;
import io.qameta.allure.Step;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Selectors.byText;


@ParametersAreNonnullByDefault
public class Header {

    private final SelenideElement menuButton;
    private final SelenideElement profileOption;
    private final SelenideElement friendsOption;
    private final SelenideElement allPeopleOption;
    private final SelenideElement signOut;
    private final SelenideElement logOutButton;
    private final SelenideElement addNewSpending;
    private final SelenideElement main;
    private final SelenideDriver driver;

    public Header(SelenideDriver driver) {
        this.driver = driver;
        this.menuButton = driver.$("button[aria-label='Menu']");
        this.profileOption = driver.$("a[href='/profile']");
        this.friendsOption = driver.$("a[href='/people/friends']");
        this.allPeopleOption = driver.$("a[href='/people/all']");
        this.signOut = driver.$(byText("Sign out"));
        this.logOutButton = driver.$(byText("Log out"));
        this.addNewSpending = driver.$("a[href='/spending']");
        this.main = driver.$("a[href='/main']");
    }

    @Step("Перейти на страницу Profile")
    public ProfilePage toProfilePage() {
        menuButton.click();
        profileOption.click();
        return new ProfilePage(driver);
    }

    @Step("Перейти на страницу Friends")
    public FriendsPage toFriendsPage() {
        menuButton.click();
        friendsOption.click();
        return new FriendsPage(driver);
    }

    @Step("Перейти на страницу People")
    public PeoplePage toPeoplePage() {
        menuButton.click();
        allPeopleOption.click();
        return new PeoplePage(driver);
    }

    @Step("Разлогинить текущего юзера")
    public LoginPage signOut() {
        menuButton.click();
        signOut.click();
        logOutButton.click();
        return new LoginPage(driver);
    }

    @Step("Перейти на страницу создания траты")
    public EditSpendingPage addNewSpendingPage() {
        addNewSpending.click();
        return new EditSpendingPage(driver);
    }

    @Step("Перейти на главную страницу")
    public MainPage toMainPage() {
        main.click();
        return new MainPage(driver);
    }
}
