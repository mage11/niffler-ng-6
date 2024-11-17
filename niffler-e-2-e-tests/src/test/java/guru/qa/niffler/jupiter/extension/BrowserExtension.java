package guru.qa.niffler.jupiter.extension;

import com.codeborne.selenide.SelenideConfig;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.logevents.SelenideLogger;
import guru.qa.niffler.jupiter.converter.Browsers;
import io.qameta.allure.Allure;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.LifecycleMethodExecutionExceptionHandler;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.ByteArrayInputStream;

public class BrowserExtension implements
    BeforeEachCallback,
    AfterEachCallback,
    TestExecutionExceptionHandler,
    LifecycleMethodExecutionExceptionHandler {
  private static final ThreadLocal<SelenideDriver> driver = new ThreadLocal<>();

  @Override
  public void afterEach(ExtensionContext context) throws Exception {
    if (driver.get().hasWebDriverStarted()) {
        driver.get().close();
      }
  }

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    String browser = context.getDisplayName();
    SelenideConfig config = browser.equals(Browsers.FIREFOX.name())
        ? Browsers.FIREFOX.driverConfig()
        : Browsers.CHROME.driverConfig();

    driver.set(new SelenideDriver(config));

    SelenideLogger.addListener("Allure-selenide", new AllureSelenide()
        .savePageSource(false)
        .screenshots(false)
    );
  }

  @Override
  public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
    doScreenshot();
    throw throwable;
  }

  @Override
  public void handleBeforeEachMethodExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
    doScreenshot();
    throw throwable;
  }

  @Override
  public void handleAfterEachMethodExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
    doScreenshot();
    throw throwable;
  }

  private void doScreenshot() {
    if (driver.get().hasWebDriverStarted()) {
      Allure.addAttachment(
          "Screen on fail for browser: " + driver.get().getSessionId(),
          new ByteArrayInputStream(
              ((TakesScreenshot) driver.get()).getScreenshotAs(OutputType.BYTES)
          )
      );
    }
  }
}
