package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
@WebTest
public class LoginTest {

  private static final Config CFG = Config.getInstance();

  @Test
  void mainPageShouldBeDisplayedAfterSuccessLogin(UserJson user) {
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.testData().password())
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
