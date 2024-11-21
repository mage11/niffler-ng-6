package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class ProfileTest {

    private static final Config CFG = Config.getInstance();


    @User(
        username = "PetyaMain",
        categories = @Category(
            name = "archivedCategory",
            archived = true
        )
    )
    @Test
    public void archivedCategoryShouldPresentInCategoriesList(CategoryJson category){

        Selenide.open(CFG.frontUrl(), LoginPage.class)
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
    public void activeCategoryShouldPresentInCategoriesList(CategoryJson category){

        Selenide.open(CFG.frontUrl(), LoginPage.class)
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
    public void archivedCategoryShouldNotPresentInCategoriesList(CategoryJson category){

        Selenide.open(CFG.frontUrl(), LoginPage.class)
            .login("PetyaMain", "123")
            .clickToProfile()
            .searchingCategoryShouldNotBeExist(category.name());
    }

}
