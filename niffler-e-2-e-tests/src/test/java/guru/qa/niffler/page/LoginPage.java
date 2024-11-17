package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;

@ParametersAreNonnullByDefault
public class LoginPage extends BasePage<LoginPage> {

    private final SelenideElement usernameInput;
    private final SelenideElement passwordInput;
    private final SelenideElement submitButton;
    private final SelenideElement createNewAccountButton;
    private final SelenideElement formError;

    public LoginPage(SelenideDriver driver) {
        super(driver);
        this.usernameInput = driver.$("input[name='username']");
        this.passwordInput = driver.$("input[name='password']");
        this.submitButton = driver.$("button[type='submit']");
        this.createNewAccountButton = driver.$("a[href='/register']");
        this.formError = driver.$(".form__error");
    }

    @Nonnull
    public MainPage login(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        submitButton.click();
        return new MainPage(driver);
    }

    @Nonnull
    public RegisterPage clickToCreateNewAccountButton() {
        createNewAccountButton.click();
        return new RegisterPage(driver);
    }

    @Nonnull
    public LoginPage shouldBeErrorMessage(String message) {
        formError.shouldHave(text(message));
        return this;
    }
}
