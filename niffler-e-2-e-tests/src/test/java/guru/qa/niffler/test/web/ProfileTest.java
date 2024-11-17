package guru.qa.niffler.test.web;

import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.utils.RandomDataUtils;
import guru.qa.niffler.utils.SelenideUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.awt.image.BufferedImage;

@ExtendWith(BrowserExtension.class)
public class ProfileTest {
    private final SelenideDriver driver = new SelenideDriver(SelenideUtils.chromeConfig);
    private static final Config CFG = Config.getInstance();


    @User(
        username = "PetyaMain",
        categories = @Category(
            name = "archivedCategory",
            archived = true
        )
    )
    @Test
    public void archivedCategoryShouldPresentInCategoriesList(CategoryJson category) {

        driver.open(CFG.frontUrl(), LoginPage.class)
            .login("PetyaMain", "123")
            .clickToProfile()
            .clickToShowArchivedToggle()
            .searchingCategoryShouldBeExist(category.name());
    }

    @User(
        username = "PetyaMain",
        categories = @Category(
            name = "activeCategory",
            archived = false
        )
    )
    @Test
    public void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {

        driver.open(CFG.frontUrl(), LoginPage.class)
            .login("PetyaMain", "123")
            .clickToProfile()
            .searchingCategoryShouldBeExist(category.name());
    }

    @User(
        username = "PetyaMain",
        categories = @Category(
            name = "archivedCategoryNotPresent",
            archived = true
        )
    )
    @Test
    public void archivedCategoryShouldNotPresentInCategoriesList(CategoryJson category) {

        driver.open(CFG.frontUrl(), LoginPage.class)
            .login("PetyaMain", "123")
            .clickToProfile()
            .searchingCategoryShouldNotBeExist(category.name());
    }

    @User()
    @Test
    void editName(UserJson user) {
        String successMessage = "Profile successfully updated";
        driver.open(CFG.frontUrl(), LoginPage.class)
            .login(user.username(), user.testData().password())
            .clickToProfile()
            .setNameAndSave(RandomDataUtils.randomName())
            .checkAlert(successMessage);
    }

    @User
    @ScreenShotTest(value = "img/cat-expected.jpeg", rewriteExpected = false)
    void uploadPhoto(BufferedImage expected, UserJson user){
        driver.open(CFG.frontUrl(), LoginPage.class)
            .login(user.username(), user.testData().password())
            .clickToProfile()
            .uploadPhoto("img/cat.jpeg")
            .photoShouldBeLikeExpected(expected);
    }

}
