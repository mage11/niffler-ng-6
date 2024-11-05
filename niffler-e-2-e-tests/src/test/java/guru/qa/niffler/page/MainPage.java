package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {
  private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");
  private final SelenideElement statisticBlock = $("#stat");
  private final SelenideElement spendingBlock = $("#spendings");
  private final SelenideElement searchInput = $(By.xpath("//input[@placeholder='Search']"));
  private final Header header = new Header();

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
    return header.toProfilePage();
  }

  public FriendsPage clickToFriendsPage(){
    return header.toFriendsPage();
  }

  public MainPage headersBlocksShouldBeExists(){

    String statisticsHeader = "Statistics";
    String spendingHeader = "History of Spendings";

    statisticBlock.shouldBe(exist).shouldHave(text(statisticsHeader));
    spendingBlock.shouldBe(exist).shouldHave(text(spendingHeader));

    return this;
  }

  public EditSpendingPage addNewSpending(){
    return header.addNewSpendingPage();
  }
}
