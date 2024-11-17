package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import static org.openqa.selenium.Keys.BACK_SPACE;
import static org.openqa.selenium.Keys.CONTROL;

public class SearchField {

    private final SelenideElement searchInput;

    public SearchField(SelenideDriver driver){
        this.searchInput = driver.$(By.xpath("//input[@placeholder='Search']"));
    }
    @Step("Найти значение {value}")
    public SearchField search(String value){
        clear();
        searchInput.setValue(value).pressEnter();
        return this;
    }

    @Step("Очистить поле ввода")
    public SearchField clear(){
        searchInput.sendKeys(CONTROL + "a");
        searchInput.sendKeys(BACK_SPACE);
        return this;
    }

}
