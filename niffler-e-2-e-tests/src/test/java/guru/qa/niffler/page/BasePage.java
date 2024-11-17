package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;

public abstract class BasePage<T extends BasePage<?>> {

  private final SelenideElement alert;
  private final ElementsCollection formErrors;
  protected final SelenideDriver driver;
  protected BasePage(SelenideDriver driver) {
    this.driver = driver;
    this.alert = driver.$(".MuiSnackbar-root");
    this.formErrors = driver.$$("p.Mui-error, .input__helper-text");
  }

  @SuppressWarnings("unchecked")
  public T checkAlert(String message) {
    alert.shouldHave(text(message));
    return (T) this;
  }
}
