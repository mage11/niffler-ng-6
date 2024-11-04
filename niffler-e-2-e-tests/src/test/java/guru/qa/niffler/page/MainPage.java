package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.SpendingTable;
import guru.qa.niffler.utils.ScreenDiffResult;
import org.openqa.selenium.By;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ParametersAreNonnullByDefault
public class MainPage extends BasePage {

    private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");
    private final SelenideElement statisticBlock = $("#stat");
    private final SelenideElement spendingBlock = $("#spendings");
    private final SelenideElement searchInput = $(By.xpath("//input[@placeholder='Search']"));
    private final ElementsCollection legendContainers = $$("#legend-container>ul>li");
    private final SelenideElement statCanvas = $("canvas[role=img]");
    private final Header header = new Header();
    private final SpendingTable spendingTable = new SpendingTable();

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

    public MainPage statisticImgShouldBeLikeExpected(BufferedImage expected){
        Selenide.sleep(2000);
        BufferedImage actual;
        try {
            actual = ImageIO.read(statCanvas.screenshot());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertFalse(new ScreenDiffResult(actual, expected));

        return this;
    }

    public MainPage categoryExistInContainerBelowStat(String categoryName, String amount){
        legendContainers.find(text(categoryName)).shouldBe(exist).shouldHave(text(amount));
        return this;
    }

    public MainPage deleteSpending(String description){
        spendingTable.deleteSpending(description);
        return this;
    }

    public MainPage spendingShouldNotBeFound(String description){
        spendingTable.spendingShouldNotBeFound(description);
        return this;
    }
}
