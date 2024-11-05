package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Calendar;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static org.openqa.selenium.Keys.BACK_SPACE;
import static org.openqa.selenium.Keys.CONTROL;

public class EditSpendingPage {

  private final SelenideElement descriptionInput = $("#description");
  private final SelenideElement saveBtn = $("#save");
  private final SelenideElement amount = $(By.xpath("//input[@id='amount']"));
  private final SelenideElement category = $(By.xpath("//input[@id='category']"));
  private final SelenideElement description = $(By.xpath("//input[@id='description']"));
  private Calendar calendar = new Calendar();

  @Step("Указать стоимость")
  public EditSpendingPage setAmount(String amountValue){
    amount.sendKeys(CONTROL + "a");
    amount.sendKeys(BACK_SPACE);
    amount.setValue(amountValue);
    return this;
  }

  @Step("Указать название категории")
  public EditSpendingPage setCategory(String categoryName){
    category.sendKeys(CONTROL + "a");
    category.sendKeys(BACK_SPACE);
    category.setValue(categoryName);
    return this;
  }

  @Step("Указать название описание")
  public EditSpendingPage setDescription(String descriptionValue){
    description.sendKeys(CONTROL + "a");
    description.sendKeys(BACK_SPACE);
    description.setValue(descriptionValue);
    return this;
  }

  @Step("Указать дату для затрат")
  public EditSpendingPage setDate(String date){
    calendar.setDateInCalendar(date);
    return this;
  }

  @Step("Сохранить трату")
  public MainPage save() {
    saveBtn.click();
    return new MainPage();
  }
}
