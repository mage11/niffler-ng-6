package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {
  private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");
  private final SelenideElement menuButton = $("button[aria-label='Menu']");
  private final SelenideElement profileOption = $("a[href='/profile']");
  private final SelenideElement statisticBlock = $("#stat");
  private final SelenideElement spendingBlock = $("#spendings");

  public EditSpendingPage editSpending(String spendingDescription) {
    tableRows.find(text(spendingDescription)).$$("td").get(5).click();
    return new EditSpendingPage();
  }

  public void checkThatTableContainsSpending(String spendingDescription) {
    tableRows.find(text(spendingDescription)).should(visible);
  }

  public ProfilePage clickToProfile(){

    menuButton.click();
    profileOption.click();

    return new ProfilePage();
  }

  public MainPage headersBlocksShouldBeExists(){

    String statisticsHeader = "Statistics";
    String spendingHeader = "History of Spendings";

    statisticBlock.shouldBe(exist).shouldHave(text(statisticsHeader));
    spendingBlock.shouldBe(exist).shouldHave(text(spendingHeader));

    return this;
  }
}
