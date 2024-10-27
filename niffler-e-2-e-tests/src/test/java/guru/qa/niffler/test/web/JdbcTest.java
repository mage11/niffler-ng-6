package guru.qa.niffler.test.web;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendClient;
import guru.qa.niffler.service.UsersDbClient;
import guru.qa.niffler.service.impl.SpendDbClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JdbcTest {

  @Test
  void txTest() {
    SpendDbClient spendDbClient = new SpendDbClient();

    SpendJson spend = spendDbClient.createSpend(
        new SpendJson(
            null,
            new Date(),
            new CategoryJson(
                null,
                "cat-name-tx-3",
                "duck",
                false
            ),
            CurrencyValues.RUB,
            1000.0,
            "spend-name-tx-3",
            "duck"
        )
    );

    System.out.println(spend);
  }


  static UsersDbClient usersDbClient = new UsersDbClient();
  static SpendClient spendClient = new SpendDbClient();

  @ValueSource(strings = {
      "valentin-10"
  })
  @ParameterizedTest
  void springJdbcTest(String uname) {

    UserJson user = usersDbClient.createUser(
        uname,
        "12345"
    );

    usersDbClient.addIncomeInvitation(user, 1);
    usersDbClient.addOutcomeInvitation(user, 1);
  }

  @Test
  void SpendClientTest(){

    CategoryJson category = spendClient.createCategory(
        new CategoryJson(
            null,
            RandomDataUtils.randomCategoryName(),
            "PetyaMain",
            false
        )
    );

    assertNotNull(category.id(), "У созданной категории есть id");

    CategoryJson updCategory = spendClient.updateCategory(
        new CategoryJson(
            category.id(),
            category.name() + "upd",
            category.username(),
            category.archived()
        )
    );

    assertEquals(category.name()+"upd", updCategory.name(), "У категории изменилось название");

    SpendJson spend = spendClient.createSpend(
        new SpendJson(
            null,
            new Date(),
            updCategory,
            CurrencyValues.RUB,
            100.0,
            "desc",
            "PetyaMain"
        )
    );

    assertNotNull(spend.id(), "У созданной траты есть id");

    SpendJson updSpend = spendClient.updateSpend(
        new SpendJson(
            spend.id(),
            spend.spendDate(),
            updCategory,
            spend.currency(),
            150.0,
            spend.description()+"upd",
            spend.username()
        )
    );

    assertEquals(spend.description()+"upd", updSpend.description(), "У траты обновилось описание");
    assertEquals(150.0, updSpend.amount(), "У траты обновилось сумма");

    Optional<CategoryJson> foundCategoryById = spendClient.findCategoryById(updCategory.id());
    assertFalse(foundCategoryById.isEmpty(), "Категория нашлась");

    Optional<CategoryJson> foundCategoryByUsernameAndCategoryName = spendClient.findCategoryByUsernameAndCategoryName(
        updCategory.username(), updCategory.name());
    assertFalse(foundCategoryByUsernameAndCategoryName.isEmpty(), "Категория нашлась");

    Optional<SpendJson> foundSpendById = spendClient.findSpendById(updSpend.id());
    assertFalse(foundSpendById.isEmpty(), "Трата нашлась");

    List<SpendJson> foundSpendByUsernameAndDescription = spendClient.findSpendByUsernameAndDescription(
        updCategory.username(), updSpend.description());
    assertFalse(foundSpendByUsernameAndDescription.isEmpty(), "Траты нашлись");

    spendClient.deleteSpend(updSpend);
    assertTrue(spendClient.findSpendById(updSpend.id()).isEmpty(), "Трата не нашлась после удаления");

    spendClient.deleteCategory(updCategory);
    assertTrue(spendClient.findCategoryById(updCategory.id()).isEmpty(), "Категория не нашлась после удаления");
  }

  @Test
  void UsersClientTest(){
    UserJson user = usersDbClient.createUser(
        RandomDataUtils.randomName(),
        "12345"
    );

    UserJson user2 = usersDbClient.createUser(
        RandomDataUtils.randomName(),
        "12345"
    );

    usersDbClient.addIncomeInvitation(user, 1);
    usersDbClient.addOutcomeInvitation(user, 1);
    usersDbClient.addFriend(user, user2);
    usersDbClient.addFriend(user, 1);
  }

}
