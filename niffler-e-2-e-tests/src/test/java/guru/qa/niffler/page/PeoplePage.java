package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;

@ParametersAreNonnullByDefault
public class PeoplePage extends BasePage<PeoplePage> {

  private final SelenideElement peopleTable;

  public PeoplePage(SelenideDriver driver){
    super(driver);
    this.peopleTable = driver.$("#all");
  }

  @Nonnull
  public PeoplePage checkInvitationSentToUser(String username) {
    SelenideElement friendRow = peopleTable.$$("tr").find(text(username));
    friendRow.shouldHave(text("Waiting..."));
    return this;
  }
}
