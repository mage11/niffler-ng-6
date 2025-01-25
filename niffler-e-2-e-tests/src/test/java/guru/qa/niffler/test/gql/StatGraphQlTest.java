package guru.qa.niffler.test.gql;

import com.apollographql.apollo.api.ApolloResponse;
import com.apollographql.java.client.ApolloCall;
import com.apollographql.java.rx2.Rx2Apollo;
import guru.qa.StatQuery;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;

import guru.qa.type.CurrencyValues;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StatGraphQlTest extends BaseGraphQlTest {

  @User
  @Test
  @ApiLogin
  void statTest(@Token String bearerToken) {
    final ApolloCall<StatQuery.Data> currenciesCall = apolloClient.query(StatQuery.builder()
            .filterCurrency(null)
            .statCurrency(null)
            .filterPeriod(null)
            .build())
        .addHttpHeader("authorization", bearerToken);

    final ApolloResponse<StatQuery.Data> response = Rx2Apollo.single(currenciesCall).blockingGet();
    final StatQuery.Data data = response.dataOrThrow();
    StatQuery.Stat result = data.stat;
    Assertions.assertEquals(
        0.0,
        result.total
    );
  }

  @User(
      categories = {
          @Category(name = "category1"),
          @Category(name = "category2", archived = true)
      },
      spendings = {
          @Spending(category = "category1",
              description = "desc1",
              amount = 10.0,
              currency = guru.qa.niffler.model.rest.CurrencyValues.RUB),
          @Spending(category = "category2",
              description = "desc2",
              amount = 20.0,
              currency = guru.qa.niffler.model.rest.CurrencyValues.RUB),
      }
  )
  @Test
  @ApiLogin
  void statShouldShowTotalSumOfOneCurrency(@Token String bearerToken) {
    final ApolloCall<StatQuery.Data> currenciesCall = apolloClient.query(StatQuery.builder()
            .filterCurrency(CurrencyValues.RUB)
            .statCurrency(CurrencyValues.RUB)
            .filterPeriod(null)
            .build())
        .addHttpHeader("authorization", bearerToken);

    final ApolloResponse<StatQuery.Data> response = Rx2Apollo.single(currenciesCall).blockingGet();
    final StatQuery.Data data = response.dataOrThrow();
    StatQuery.Stat result = data.stat;

    Assertions.assertEquals(30.0, result.total);
    Assertions.assertEquals(CurrencyValues.RUB.rawValue, result.currency.rawValue);
  }

  @User(
      categories = {
          @Category(name = "category1"),
          @Category(name = "category2", archived = true)
      },
      spendings = {
          @Spending(category = "category1",
              description = "desc1",
              amount = 10.0,
              currency = guru.qa.niffler.model.rest.CurrencyValues.RUB),
          @Spending(category = "category2",
              description = "desc2",
              amount = 20.0,
              currency = guru.qa.niffler.model.rest.CurrencyValues.EUR),
      }
  )
  @Test
  @ApiLogin
  void statShouldShowTotalOfFilteredCurrency(@Token String bearerToken) {
    final ApolloCall<StatQuery.Data> currenciesCall = apolloClient.query(StatQuery.builder()
            .filterCurrency(CurrencyValues.EUR)
            .statCurrency(CurrencyValues.EUR)
            .filterPeriod(null)
            .build())
        .addHttpHeader("authorization", bearerToken);

    final ApolloResponse<StatQuery.Data> response = Rx2Apollo.single(currenciesCall).blockingGet();
    final StatQuery.Data data = response.dataOrThrow();
    StatQuery.Stat result = data.stat;

    Assertions.assertEquals(20.0, result.total);
    Assertions.assertEquals(CurrencyValues.EUR.rawValue, result.currency.rawValue);
  }

}
