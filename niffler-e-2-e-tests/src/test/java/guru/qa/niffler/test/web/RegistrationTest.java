package guru.qa.niffler.test.web;

import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.utils.SelenideUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

public class RegistrationTest {
    private final SelenideDriver driver = new SelenideDriver(SelenideUtils.chromeConfig);
    private static final Config CFG = Config.getInstance();
    private final String messageOfSuccessRegistration = "Congratulations! You've registered!";

    @Test
    public void shouldRegisterNewUser(){

        String username = getRandomString();
        String password = getRandomString();

        driver.open(CFG.frontUrl(), LoginPage.class)
            .clickToCreateNewAccountButton()
            .setUserName(username)
            .setPassword(password)
            .setPasswordSubmit(password)
            .submitRegistration()
            .shouldBeSuccessRegistration(messageOfSuccessRegistration);

    }

    @Test
    public void shouldNotRegisterUserWithExistingUserName(){

        String username = getRandomString();
        String password = getRandomString();
        String errorMessage = "Username `" + username + "` already exists";

        driver.open(CFG.frontUrl(), LoginPage.class)
            .clickToCreateNewAccountButton()
            .setUserName(username)
            .setPassword(password)
            .setPasswordSubmit(password)
            .submitRegistration()
            .shouldBeSuccessRegistration(messageOfSuccessRegistration)
            .clickToSignInButton()
            .clickToCreateNewAccountButton()
            .setUserName(username)
            .setPassword(password)
            .setPasswordSubmit(password)
            .submitRegistration()
            .shouldBeErrorMessage(errorMessage);
    }

    @Test
    public void  shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual(){

        String username = getRandomString();
        String password = getRandomString();
        String passwordNotEqual = "Passwords should be equal";

        driver.open(CFG.frontUrl(), LoginPage.class)
            .clickToCreateNewAccountButton()
            .setUserName(username)
            .setPassword(password)
            .setPasswordSubmit(username)
            .submitRegistration()
            .shouldBeErrorMessage(passwordNotEqual);

    }

    @Test
    public void  shouldShowErrorIfPasswordNotEnoughLength(){

        String username = getRandomString();
        String password = "1";
        String passwordErrorLength = "Allowed password length should be from 3 to 12 characters";

        driver.open(CFG.frontUrl(), LoginPage.class)
            .clickToCreateNewAccountButton()
            .setUserName(username)
            .setPassword(password)
            .setPasswordSubmit(username)
            .submitRegistration()
            .shouldBeErrorMessage(passwordErrorLength);

    }

    public String getRandomString(){

        return RandomStringUtils.random(5, true, true);
    }


}
