package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProfilePage {

    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement nameInput = $("#name");
    private final SelenideElement categoryInput = $("#category");
    private final SelenideElement saveChangesButton = $("button[type='submit']");
    private final SelenideElement showArchivedToggle = $(By.xpath("//input[@type='checkbox']"));
    private final ElementsCollection categories = $$(".MuiChip-label");

    public ProfilePage clickToShowArchivedToggle(){

        showArchivedToggle.click();

        return this;
    }

    public ProfilePage searchingCategoryShouldBeExist(String categoryName){

        categories.find(text(categoryName)).shouldBe(visible);

        return this;
    }

    public ProfilePage searchingCategoryShouldNotBeExist(String categoryName){

        categories.find(text(categoryName)).shouldNotBe(visible);

        return this;
    }
}
