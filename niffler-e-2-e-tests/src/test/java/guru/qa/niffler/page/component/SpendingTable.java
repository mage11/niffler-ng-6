package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.EditSpendingPage;
import guru.qa.niffler.utils.DataFilterValues;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class SpendingTable {

    private final SelenideElement periodElement = $(By.xpath("//div[@id='period']"));
    private final SelenideElement deleteBtn = $(By.xpath("//button[@id='delete']"));
    private final ElementsCollection options = $$("[role='option']");
    private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");
    private final SearchField searchField = new SearchField();

    @Step("Выбрать период {period}")
    public SpendingTable selectPeriod(DataFilterValues period){
        periodElement.click();
        options.find(text(period.getPeriod())).click();
        return this;
    }

    @Step("Удалить трату {description}")
    public SpendingTable deleteSpending(String description){
        searchField.search(description);
        tableRows.find(exactText(description)).$$("td").get(0).click();
        deleteBtn.click();
        return this;
    }

    @Step("Найти трату {description}")
    public SpendingTable searchSpendingByDescription(String description){
        searchField.search(description);
        tableRows.find(exactText(description)).shouldBe(visible);
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
        return new EditSpendingPage();
    }

}
