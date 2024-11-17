package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.SpendingTable;
import guru.qa.niffler.page.component.StatComponent;
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
import static org.junit.jupiter.api.Assertions.assertFalse;

@ParametersAreNonnullByDefault
public class MainPage extends BasePage<MainPage> {

    private final ElementsCollection tableRows;
    private final SelenideElement statisticBlock;
    private final SelenideElement spendingBlock;
    private final SelenideElement searchInput;
    private final ElementsCollection legendContainers;
    private final SelenideElement statCanvas;
    private final Header header;
    private final SpendingTable spendingTable;
    private final StatComponent statComponent;

    public MainPage(SelenideDriver driver){
        super(driver);
        this.tableRows = driver.$("#spendings tbody").$$("tr");
        this.statisticBlock = driver.$("#stat");
        this.spendingBlock = driver.$("#spendings");
        this.searchInput = driver.$(By.xpath("//input[@placeholder='Search']"));
        this.legendContainers = driver.$$("#legend-container>ul>li");
        this.statCanvas = driver.$("canvas[role=img]");
        this.header = new Header(driver);
        this.spendingTable = new SpendingTable(driver);
        this.statComponent = new StatComponent(driver);
    }

    @Nonnull
    public EditSpendingPage editSpending(String spendingDescription) {
        searchInput.setValue(spendingDescription).pressEnter();
        tableRows.find(text(spendingDescription)).$$("td").get(5).click();
        return new EditSpendingPage(driver);
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

    public MainPage statisticImgShouldBeLikeExpected(BufferedImage expected) throws InterruptedException {
        Thread.sleep(2000);
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

    public StatComponent getStatComponent(){
        return statComponent;
    }

    public SpendingTable getSpendingTable(){
        return spendingTable;
    }
}
