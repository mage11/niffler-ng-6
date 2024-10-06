package guru.qa.niffler.test.web;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UsersDbClient;
import org.junit.jupiter.api.Test;

import java.util.Date;

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

  @Test
  void springJdbcTest() {
    UsersDbClient usersDbClient = new UsersDbClient();
    UserJson user = usersDbClient.createUser(
        new UserJson(
            null,
            "valentin-6",
            null,
            null,
            null,
            CurrencyValues.RUB,
            null,
            null,
            null
        )
    );
    System.out.println(user);
  }

  @Test
  void repositoryTest(){
    UsersDbClient usersDbClient = new UsersDbClient();

    UserJson user = usersDbClient.createUser(
        new UserJson(
            null,
            "myself",
            null,
            null,
            null,
            CurrencyValues.RUB,
            null,
            null,
            null
        )
    );


    UserJson friend = usersDbClient.createUser(
        new UserJson(
            null,
            "friend1",
            null,
            null,
            null,
            CurrencyValues.RUB,
            null,
            null,
            null
        )
    );


    UserJson income = usersDbClient.createUser(
        new UserJson(
            null,
            "income",
            null,
            null,
            null,
            CurrencyValues.RUB,
            null,
            null,
            null
        )
    );

    UserJson outcome = usersDbClient.createUser(
        new UserJson(
            null,
            "outcome",
            null,
            null,
            null,
            CurrencyValues.RUB,
            null,
            null,
            null
        )
    );

    usersDbClient.addIncomeInvitation(user, income);
    usersDbClient.addOutcomeInvitation(user, outcome);
    usersDbClient.addFriend(user, friend);
  }
}
