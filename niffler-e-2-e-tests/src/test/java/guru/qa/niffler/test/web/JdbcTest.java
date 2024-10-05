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
                "cat-name-tx-2",
                "duck",
                false
            ),
            CurrencyValues.RUB,
            1000.0,
            "spend-name-tx",
            null
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
            "valentin-4",
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
  void jdbcWithTxTest() {
    UsersDbClient usersDbClient = new UsersDbClient();
    UserJson user = usersDbClient.createUserJdbcWithTx(
        new UserJson(
            null,
            "Petya-Test",
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
  void jdbcWithoutTxTest() {
    UsersDbClient usersDbClient = new UsersDbClient();
    UserJson user = usersDbClient.createUserJdbcWithoutTx(
        new UserJson(
            null,
            "Petya-Test",
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
  void SpringJdbcWithTxTest() {
    UsersDbClient usersDbClient = new UsersDbClient();
    UserJson user = usersDbClient.createUserJdbcSpringWithTx(
        new UserJson(
            null,
            "Petya-Test",
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
  void SpringJdbcWithoutTxTest() {
    UsersDbClient usersDbClient = new UsersDbClient();
    UserJson user = usersDbClient.createUserJdbcSpringWithoutTx(
        new UserJson(
            null,
            "Petya-Test",
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
}
