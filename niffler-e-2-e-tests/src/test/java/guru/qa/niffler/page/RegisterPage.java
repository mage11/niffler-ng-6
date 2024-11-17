package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;

@ParametersAreNonnullByDefault
public class RegisterPage extends BasePage<RegisterPage> {

    private final SelenideElement usernameInput;
    private final SelenideElement passwordInput;
    private final SelenideElement passwordSubmitInput;
    private final SelenideElement submitButton;
    private final SelenideElement messageOfSuccessRegistration;
    private final SelenideElement signInButton;
    private final SelenideElement formError;

    public RegisterPage(SelenideDriver driver){
        super(driver);
        this.usernameInput= driver.$("#username");
        this.passwordInput = driver.$("#password");
        this.passwordSubmitInput = driver.$("#passwordSubmit");
        this.submitButton = driver.$("button[type='submit']");
        this.messageOfSuccessRegistration = driver.$(".form__paragraph_success");
        this.signInButton = driver.$(".form_sign-in");
        this.formError = driver.$(".form__error");
    }

    @Step("Установить в поле username значение {userName}")
    @Nonnull
    public RegisterPage setUserName(String userName) {

        usernameInput.setValue(userName);

        return this;
    }

    @Step("Установить в поле password значение {password}")
    @Nonnull
    public RegisterPage setPassword(String password) {

        passwordInput.setValue(password);

        return this;
    }

    @Step("Установить в поле passwordSubmit значение {password}")
    @Nonnull
    public RegisterPage setPasswordSubmit(String password) {

        passwordSubmitInput.setValue(password);

        return this;
    }

    @Step("Подтвердить регистрацию по кнопке подтверждения")
    @Nonnull
    public RegisterPage submitRegistration() {

        submitButton.click();

        return this;
    }

    @Step("Провереить успешность регистрации")
    @Nonnull
    public RegisterPage shouldBeSuccessRegistration(String message) {

        messageOfSuccessRegistration.shouldHave(text(message));

        return this;
    }

    @Step("Нажать на кнопку входа")
    @Nonnull
    public LoginPage clickToSignInButton() {

        signInButton.click();

        return new LoginPage(driver);
    }

    @Step("Должна быть показана ошибка {message}")
    @Nonnull
    public RegisterPage shouldBeErrorMessage(String message) {

        formError.shouldHave(text(message));

        return this;
    }

}
