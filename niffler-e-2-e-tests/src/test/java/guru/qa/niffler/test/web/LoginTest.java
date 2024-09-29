package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

public class LoginTest {

    private static final Config CFG = Config.getInstance();
    @Test
    public void mainPageShouldBeDisplayedAfterSuccessLogin(){

        Selenide.open(CFG.frontUrl(), LoginPage.class)
            .login("PetyaMain", "123")
            .headersBlocksShouldBeExists();
    }

    @Test
    public void userShouldStayOnLoginPageAfterLoginWithBadCredentials(){

        String errorMessage = "Неверные учетные данные пользователя";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
            .login("PetyaMain", "321");

        LoginPage currentLoginPage = new LoginPage();
        currentLoginPage.shouldBeErrorMessage(errorMessage);
    }

}
