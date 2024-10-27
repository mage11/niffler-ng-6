package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import org.openqa.selenium.By;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class MainPage {

    private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");
    private final SelenideElement statisticBlock = $("#stat");
    private final SelenideElement spendingBlock = $("#spendings");
    private final SelenideElement searchInput = $(By.xpath("//input[@placeholder='Search']"));
    private final Header header = new Header();

    @Nonnull
    public EditSpendingPage editSpending(String spendingDescription) {
        searchInput.setValue(spendingDescription).pressEnter();
        tableRows.find(text(spendingDescription)).$$("td").get(5).click();
        return new EditSpendingPage();
    }

    @Nonnull
    public void checkThatTableContainsSpending(String spendingDescription) {
        searchInput.setValue(spendingDescription).pressEnter();
        tableRows.find(text(spendingDescription)).should(visible);
    }

    @Nonnull
    public ProfilePage clickToProfile() {
        return header.toProfilePage();
    }

    @Nonnull
    public FriendsPage clickToFriendsPage() {
        return header.toFriendsPage();
    }

    @Nonnull
    public MainPage headersBlocksShouldBeExists() {
        String statisticsHeader = "Statistics";
        String spendingHeader = "History of Spendings";

        statisticBlock.shouldBe(exist).shouldHave(text(statisticsHeader));
        spendingBlock.shouldBe(exist).shouldHave(text(spendingHeader));

        return this;
    }

    @Nonnull
    public EditSpendingPage addNewSpending() {
        return header.addNewSpendingPage();
    }
}
