package guru.qa.niffler.test.web;

import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.converter.BrowserConverter;
import guru.qa.niffler.jupiter.converter.Browsers;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.utils.SelenideUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.EnumSource;

public class RegistrationTest {
    private final SelenideDriver defaultDriver = new SelenideDriver(SelenideUtils.chromeConfig);
    private static final Config CFG = Config.getInstance();
    private final String messageOfSuccessRegistration = "Congratulations! You've registered!";

    @ParameterizedTest
    @EnumSource(Browsers.class)
    public void shouldRegisterNewUser(@ConvertWith(BrowserConverter.class) SelenideDriver driver){

        String username = getRandomString();
        String password = getRandomString();

        driver.open(CFG.frontUrl());
        LoginPage loginPage = new LoginPage(driver);
        loginPage
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

        defaultDriver.open(CFG.frontUrl(), LoginPage.class)
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

        defaultDriver.open(CFG.frontUrl(), LoginPage.class)
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

        defaultDriver.open(CFG.frontUrl(), LoginPage.class)
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
