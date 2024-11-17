package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Calendar;

import javax.annotation.ParametersAreNonnullByDefault;

import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static org.openqa.selenium.Keys.BACK_SPACE;
import static org.openqa.selenium.Keys.CONTROL;

@ParametersAreNonnullByDefault
public class EditSpendingPage extends BasePage<EditSpendingPage> {

    private final SelenideElement saveBtn;
    private final SelenideElement amount;
    private final SelenideElement category;
    private final SelenideElement description;
    private Calendar calendar;

    public EditSpendingPage(SelenideDriver driver){
        super(driver);
        this.saveBtn = driver.$("#save");
        this.amount = driver.$(By.xpath("//input[@id='amount']"));
        this.category = driver.$(By.xpath("//input[@id='category']"));
        this.description = driver.$(By.xpath("//input[@id='description']"));
        this.calendar  = new Calendar(driver);

    }
    @Step("Указать стоимость")
    public EditSpendingPage setAmount(String amountValue) {
        amount.sendKeys(CONTROL + "a");
        amount.sendKeys(BACK_SPACE);
        amount.setValue(amountValue);
        return this;
    }

    @Step("Указать название категории")
    public EditSpendingPage setCategory(String categoryName) {
        category.sendKeys(CONTROL + "a");
        category.sendKeys(BACK_SPACE);
        category.setValue(categoryName);
        return this;
    }

    @Step("Указать название описание")
    public EditSpendingPage setDescription(String descriptionValue) {
        description.sendKeys(CONTROL + "a");
        description.sendKeys(BACK_SPACE);
        description.setValue(descriptionValue);
        return this;
    }

    @Step("Указать дату для затрат")
    public EditSpendingPage setDate(String date) {
        calendar.setDateInCalendar(date);
        return this;
    }

    @Step("Сохранить трату")
    public MainPage save() {
        saveBtn.click();
        return new MainPage(driver);
    }
}
