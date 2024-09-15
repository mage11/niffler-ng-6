package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.BrowserExtension;
import guru.qa.niffler.jupiter.Category;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class ProfileWebTest {

    private static final Config CFG = Config.getInstance();

    @Category(
        username = "PetyaMain",
        name = "archivedCategory",
        archived = false
    )
    @Test
    public void archivedCategoryShouldPresentInCategoriesList(CategoryJson category){

        Selenide.open(CFG.frontUrl(), LoginPage.class)
            .login("PetyaMain", "123")
            .clickToProfile()
            .clickToShowArchivedToggle()
            .searchingCategoryShouldBeExist(category.name());
    }

    @Category(
        username = "PetyaMain",
        name = "activeCategory",
        archived = true
    )
    @Test
    public void activeCategoryShouldPresentInCategoriesList(CategoryJson category){

        Selenide.open(CFG.frontUrl(), LoginPage.class)
            .login("PetyaMain", "123")
            .clickToProfile()
            .searchingCategoryShouldBeExist(category.name());
    }

}
