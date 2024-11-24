package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.rest.CategoryJson;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.ProfilePage;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.awt.image.BufferedImage;

@WebTest
public class ProfileTest {

    private static final Config CFG = Config.getInstance();


    @User(
        username = "PetyaMain",
        categories = @Category(
            name = "archivedCategory",
            archived = true
        )
    )
    @ApiLogin
    @Test
    public void archivedCategoryShouldPresentInCategoriesList(CategoryJson category) {

        Selenide.open(ProfilePage.URL, ProfilePage.class)
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
    @ApiLogin
    @Test
    public void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {

        Selenide.open(ProfilePage.URL, ProfilePage.class)
            .searchingCategoryShouldBeExist(category.name());
    }

    @User(
        username = "PetyaMain",
        categories = @Category(
            name = "archivedCategoryNotPresent",
            archived = true
        )
    )
    @ApiLogin
    @Test
    public void archivedCategoryShouldNotPresentInCategoriesList(CategoryJson category) {

        Selenide.open(ProfilePage.URL, ProfilePage.class)
            .searchingCategoryShouldNotBeExist(category.name());
    }

    @User()
    @ApiLogin
    @Test
    void editName(UserJson user) {
        String successMessage = "Profile successfully updated";
        Selenide.open(ProfilePage.URL, ProfilePage.class)
            .setNameAndSave(RandomDataUtils.randomName())
            .checkAlert(successMessage);
    }

    @User
    @ApiLogin
    @ScreenShotTest(value = "img/cat-expected.jpeg", rewriteExpected = false)
    void uploadPhoto(BufferedImage expected, UserJson user){
        Selenide.open(ProfilePage.URL, ProfilePage.class)
            .uploadPhoto("img/cat.jpeg")
            .photoShouldBeLikeExpected(expected);
    }

}
