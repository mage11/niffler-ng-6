package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.extern.java.Log;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage {

    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement passwordSubmitInput = $("#passwordSubmit");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement messageOfSuccessRegistration = $(".form__paragraph_success");
    private final SelenideElement signInButton = $(".form_sign-in");
    private final SelenideElement formError = $(".form__error");

    @Step("Установить в поле username значение {userName}")
    public RegisterPage setUserName(String userName){

        usernameInput.setValue(userName);

        return this;
    }

    @Step("Установить в поле password значение {password}")
    public RegisterPage setPassword(String password){

        passwordInput.setValue(password);

        return this;
    }

    @Step("Установить в поле passwordSubmit значение {password}")
    public RegisterPage setPasswordSubmit(String password){

        passwordSubmitInput.setValue(password);

        return this;
    }

    @Step("Подтвердить регистрацию по кнопке подтверждения")
    public RegisterPage submitRegistration(){

        submitButton.click();

        return this;
    }

    @Step("Провереить успешность регистрации")
    public RegisterPage shouldBeSuccessRegistration(String message){

        messageOfSuccessRegistration.shouldHave(text(message));

        return this;
    }

    @Step("Нажать на кнопку входа")
    public LoginPage clickToSignInButton(){

        signInButton.click();

        return  new LoginPage();
    }

    @Step("Должна быть показана ошибка {message}")
    public RegisterPage shouldBeErrorMessage(String message){

        formError.shouldHave(text(message));

        return this;
    }

}
