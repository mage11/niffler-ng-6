package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.SearchField;
import guru.qa.niffler.utils.ScreenDiffResult;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.openqa.selenium.Keys.BACK_SPACE;
import static org.openqa.selenium.Keys.CONTROL;

@ParametersAreNonnullByDefault
public class ProfilePage extends BasePage{

    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement nameInput = $("#name");
    private final SelenideElement categoryInput = $("#category");
    private final SelenideElement saveChangesButton = $("button[type='submit']");
    private final SelenideElement showArchivedToggle = $(By.xpath("//input[@type='checkbox']"));
    private final ElementsCollection categories = $$(".MuiChip-label");
    private final SearchField searchField = new SearchField();
    private final SelenideElement photoInput = $("#image__input");
    private final SelenideElement avatar = $(".MuiAvatar-img");

    @Step("Переключить переключатель отображения архивных категорий ")
    @Nonnull
    public ProfilePage clickToShowArchivedToggle() {
        showArchivedToggle.click();
        return this;
    }

    @Step("Найденная категория должна существовать")
    @Nonnull
    public ProfilePage searchingCategoryShouldBeExist(String categoryName) {
        searchField.search(categoryName);
        categories.find(text(categoryName)).shouldBe(visible);
        return this;
    }

    @Step("Найденная категория НЕ должна существовать")
    @Nonnull
    public ProfilePage searchingCategoryShouldNotBeExist(String categoryName) {
        searchField.search(categoryName);
        categories.find(text(categoryName)).shouldNotBe(visible);
        return this;
    }

    @Step("Установить для поля name значение {name}")
    @Nonnull
    public ProfilePage setNameAndSave(String name) {
        nameInput.sendKeys(CONTROL + "a");
        nameInput.sendKeys(BACK_SPACE);
        nameInput.setValue(name);
        saveChangesButton.click();
        nameInput.shouldHave(value(name));
        return this;
    }

    public ProfilePage uploadPhoto(String path){
        photoInput.uploadFromClasspath(path);
        avatar.screenshot();
        return this;
    }

    public void photoShouldBeLikeExpected(BufferedImage expected)  {
        avatar.shouldBe(exist);
        BufferedImage actual;
        try {
            actual = ImageIO.read(avatar.screenshot());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertFalse(new ScreenDiffResult(actual, expected));
    }
}
