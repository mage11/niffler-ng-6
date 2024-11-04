package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

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

  @User(
       spendings =
           @Spending(
               category = "Обучение",
               description = "Обучение Advanced 2.0",
               amount = 9999
           )
  )
  @ScreenShotTest(value = "img/stat-expected.jpeg", rewriteExpected = false)
  @Test
  void testStatistic(UserJson user, BufferedImage expected){
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.testData().password())
        .categoryExistInContainerBelowStat("Обучение", "9999")
        .statisticImgShouldBeLikeExpected(expected);
  }

  @User(
      spendings =
      @Spending(
          category = "Обучение-edit",
          description = "Обучение Advanced 2.0",
          amount = 9999
      )
  )
  @ScreenShotTest(value = "img/stat-edit-expected.jpeg", rewriteExpected = false)
  void testEditStatistic(UserJson user, BufferedImage expected){
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.testData().password())
        .categoryExistInContainerBelowStat("Обучение-edit", "9999")
        .editSpending("Обучение Advanced 2.0")
        .setAmount("888")
        .setDescription("Обучение-edit")
        .setCategory("newCategory")
        .save()
        .categoryExistInContainerBelowStat("newCategory", "888")
        .statisticImgShouldBeLikeExpected(expected);
  }

  @User(
      spendings =
      @Spending(
          category = "Обучение-delete",
          description = "Обучение Advanced 2.0",
          amount = 9999
      )
  )
  @ScreenShotTest(value = "img/stat-delete-expected.jpeg", rewriteExpected = false)
  void testDeleteStatistic(UserJson user, BufferedImage expected){
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.testData().password())
        .categoryExistInContainerBelowStat("Обучение-delete", "9999")
        .deleteSpending("Обучение Advanced 2.0")
        .spendingShouldNotBeFound("Обучение Advanced 2.0")
        .statisticImgShouldBeLikeExpected(expected);
  }

  @User(
      categories =
      @Category(
          name = "Обучение-arch",
          archived = true),
      spendings =
      @Spending(
          category = "Обучение-arch",
          description = "Обучение Advanced 2.0",
          amount = 777
      )
  )
  @ScreenShotTest(value = "img/stat-archived-expected.jpeg", rewriteExpected = true)
  void testArchiveStatistic(UserJson user, BufferedImage expected){
    Selenide.open(CFG.frontUrl(), LoginPage.class)
        .login(user.username(), user.testData().password())
        .categoryExistInContainerBelowStat("Archived", "777")
        .statisticImgShouldBeLikeExpected(expected);
  }
}

