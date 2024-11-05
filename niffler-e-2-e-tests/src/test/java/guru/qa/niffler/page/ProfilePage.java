package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import guru.qa.niffler.page.component.SearchField;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.openqa.selenium.Keys.BACK_SPACE;
import static org.openqa.selenium.Keys.CONTROL;

public class ProfilePage {

    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement nameInput = $("#name");
    private final SelenideElement categoryInput = $("#category");
    private final SelenideElement saveChangesButton = $("button[type='submit']");
    private final SelenideElement showArchivedToggle = $(By.xpath("//input[@type='checkbox']"));
    private final ElementsCollection categories = $$(".MuiChip-label");
    private final SearchField searchField = new SearchField();

    @Step("Переключить переключатель отображения архивных категорий ")
    public ProfilePage clickToShowArchivedToggle(){
        showArchivedToggle.click();
        return this;
    }

    @Step("Найденная категория должна существовать")
    public ProfilePage searchingCategoryShouldBeExist(String categoryName){
        searchField.search(categoryName);
        categories.find(text(categoryName)).shouldBe(visible);
        return this;
    }

    @Step("Найденная категория НЕ должна существовать")
    public ProfilePage searchingCategoryShouldNotBeExist(String categoryName){
        searchField.search(categoryName);
        categories.find(text(categoryName)).shouldNotBe(visible);
        return this;
    }

    @Step("Установить для поля name значение {name}")
    public ProfilePage setNameAndSave(String name){
        nameInput.sendKeys(CONTROL + "a");
        nameInput.sendKeys(BACK_SPACE);
        nameInput.setValue(name);
        saveChangesButton.click();
        nameInput.shouldHave(value(name));
        return this;
    }
}
