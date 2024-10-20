package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {
  private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");
  private final SelenideElement menuButton = $("button[aria-label='Menu']");
  private final SelenideElement profileOption = $("a[href='/profile']");
  private final SelenideElement friendsOption = $("a[href='/people/friends']");
  private final SelenideElement statisticBlock = $("#stat");
  private final SelenideElement spendingBlock = $("#spendings");
  private final SelenideElement searchInput = $(By.xpath("//input[@placeholder='Search']"));

  public EditSpendingPage editSpending(String spendingDescription) {
    searchInput.setValue(spendingDescription).pressEnter();
    tableRows.find(text(spendingDescription)).$$("td").get(5).click();
    return new EditSpendingPage();
  }

  public void checkThatTableContainsSpending(String spendingDescription) {
    searchInput.setValue(spendingDescription).pressEnter();
    tableRows.find(text(spendingDescription)).should(visible);
  }

  public ProfilePage clickToProfile(){

    menuButton.click();
    profileOption.click();

    return new ProfilePage();
  }

  public FriendsPage clickToFriendsPage(){

    menuButton.click();
    friendsOption.click();
    return new FriendsPage();
  }

  public MainPage headersBlocksShouldBeExists(){

    String statisticsHeader = "Statistics";
    String spendingHeader = "History of Spendings";

    statisticBlock.shouldBe(exist).shouldHave(text(statisticsHeader));
    spendingBlock.shouldBe(exist).shouldHave(text(spendingHeader));

    return this;
  }
}
