package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;

@WebTest
public class SpendingWebTest {

  private static final Config CFG = Config.getInstance();

  @User(
      username = "PetyaMain",
      spendings = @Spending(
          category = "Обучение",
          description = "Обучение Advanced 2.0",
          amount = 79990
      )
  )
  @Test
  void categoryDescriptionShouldBeChangedFromTable(SpendJson spend) {
    final String newDescription = "Обучение Niffler Next Generation";
    String successMessage = "Spending is edited successfully";
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login("PetyaMain", "123")
        .headersBlocksShouldBeExists()
        .editSpending(spend.description())
        .setDescription(newDescription)
        .save()
        .checkAlert(successMessage);

    new MainPage().checkThatTableContainsSpending(newDescription);
  }

  @User
  @Test
  void addNewSpending(UserJson user){
    String categoryName = RandomDataUtils.randomCategoryName();
    String successMessage = "New spending is successfully created";
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.testData().password())
        .addNewSpending()
        .setAmount("33")
        .setDate("10/21/2024")
        .setCategory(categoryName)
        .setDescription(RandomDataUtils.randomCategoryName())
        .save()
        .checkAlert(successMessage);
  }
}

