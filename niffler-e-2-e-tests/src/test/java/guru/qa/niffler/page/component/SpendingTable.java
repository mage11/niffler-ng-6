package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.page.EditSpendingPage;
import guru.qa.niffler.utils.DataFilterValues;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;

import static guru.qa.niffler.condition.SpendConditions.spends;

public class SpendingTable {

    private final SelenideElement periodElement;
    private final SelenideElement deleteBtn;
    private final ElementsCollection options;
    private final ElementsCollection tableRows;
    private final SearchField searchField;
    private final SelenideElement deleteButtonInContainer;
    private final SelenideDriver driver;

    public SpendingTable(SelenideDriver driver){
        this.driver = driver;
        this.periodElement = driver.$(By.xpath("//div[@id='period']"));
        this.deleteBtn = driver.$(By.xpath("//button[@id='delete']"));
        this.options = driver.$$("[role='option']");
        this.tableRows = driver.$("#spendings tbody").$$("tr");
        this.deleteButtonInContainer =
            driver.$(By.xpath("//*[starts-with(@class, 'MuiDialog-container')]"))
                .$(By.xpath("*//button[text()='Delete']"));
        this.searchField = new SearchField(driver);
    }

    @Step("Выбрать период {period}")
    public SpendingTable selectPeriod(DataFilterValues period){
        periodElement.click();
        options.find(text(period.getPeriod())).click();
        return this;
    }

    @Step("Удалить трату {description}")
    public SpendingTable deleteSpending(String description){
        searchField.search(description);
        tableRows.find(text(description)).$$("td").get(0).click();
        deleteBtn.click();
        deleteButtonInContainer.click();
        return this;
    }

    @Step("Найти трату {description}")
    public SpendingTable searchSpendingByDescription(String description){
        searchField.search(description);
        tableRows.find(exactText(description)).shouldBe(visible);
        return this;
    }

    public SpendingTable spendingShouldNotBeFound(String description){
        searchField.search(description);
        driver.$(byText("There are no spendings")).shouldBe(exist);
        return this;
    }

    @Step("Найти траты {expectedSpends}")
    public SpendingTable checkTableContains(String... expectedSpends){
        for (String value : expectedSpends){
            searchSpendingByDescription(value);
        }
        return this;
    }

    @Step("Проверить, что ожидаемый размер таблицы {expectedSize}")
    public SpendingTable checkTableSize(int expectedSize){
        tableRows.shouldHave(size(expectedSize));
        return this;
    }

    @Step("Открыть на редактирование трату {description}")
    public EditSpendingPage editSpending(String description){
        tableRows.find(text(description)).$$("td").get(5).click();
        return new EditSpendingPage(driver);
    }

    @Step("Проверить таблицу трат на содержание трат {expectedSpends}")
    public SpendingTable checkTable(SpendJson... expectedSpends){
        tableRows.should(spends(expectedSpends));
        return this;
    }

}
