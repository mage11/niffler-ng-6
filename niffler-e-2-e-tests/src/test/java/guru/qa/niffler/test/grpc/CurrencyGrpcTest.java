package guru.qa.niffler.test.grpc;

import com.google.protobuf.Empty;
import guru.qa.niffler.grpc.CalculateRequest;
import guru.qa.niffler.grpc.CalculateResponse;
import guru.qa.niffler.grpc.Currency;
import guru.qa.niffler.grpc.CurrencyResponse;
import guru.qa.niffler.grpc.CurrencyValues;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class CurrencyGrpcTest extends BaseGrpcTest {

  @Test
  void allCurrenciesShouldReturned() {
    final CurrencyResponse response = blockingStub.getAllCurrencies(Empty.getDefaultInstance());
    final List<Currency> allCurrenciesList = response.getAllCurrenciesList();
    Assertions.assertEquals(4, allCurrenciesList.size());
  }

  @Test
  void testCalculateRateForRUBToRUB() {
    double amount = 100.00;
    CalculateRequest calculateRequest = CalculateRequest.newBuilder()
        .setAmount(amount)
        .setDesiredCurrency(CurrencyValues.RUB)
        .setSpendCurrency(CurrencyValues.RUB)
        .build();
    CalculateResponse calculateResponse = blockingStub.calculateRate(calculateRequest);
    Assertions.assertEquals(amount, calculateResponse.getCalculatedAmount());
  }

  @Test
  void testCalculateRateForUSDToUSD() {
    double amount = 101.00;
    CalculateRequest calculateRequest = CalculateRequest.newBuilder()
        .setAmount(amount)
        .setDesiredCurrency(CurrencyValues.USD)
        .setSpendCurrency(CurrencyValues.USD)
        .build();
    CalculateResponse calculateResponse = blockingStub.calculateRate(calculateRequest);
    Assertions.assertEquals(amount, calculateResponse.getCalculatedAmount());
  }

  @Test
  void testCalculateRateForEURToEUR() {
    double amount = 102.00;
    CalculateRequest calculateRequest = CalculateRequest.newBuilder()
        .setAmount(amount)
        .setDesiredCurrency(CurrencyValues.EUR)
        .setSpendCurrency(CurrencyValues.EUR)
        .build();
    CalculateResponse calculateResponse = blockingStub.calculateRate(calculateRequest);
    Assertions.assertEquals(amount, calculateResponse.getCalculatedAmount());
  }

  @Test
  void testCalculateRateForRUBToUSD() {
    double amount = 100.00;
    double expectedAmount = 6666.67;
    CalculateRequest calculateRequest = CalculateRequest.newBuilder()
        .setAmount(amount)
        .setDesiredCurrency(CurrencyValues.RUB)
        .setSpendCurrency(CurrencyValues.USD)
        .build();
    CalculateResponse calculateResponse = blockingStub.calculateRate(calculateRequest);
    Assertions.assertEquals(expectedAmount, calculateResponse.getCalculatedAmount());
  }

  @Test
  void testCalculateRateForUSDToRUB() {
    double amount = 100.00;
    double expectedAmount = 1.5;
    CalculateRequest calculateRequest = CalculateRequest.newBuilder()
        .setAmount(amount)
        .setDesiredCurrency(CurrencyValues.USD)
        .setSpendCurrency(CurrencyValues.RUB)
        .build();
    CalculateResponse calculateResponse = blockingStub.calculateRate(calculateRequest);
    Assertions.assertEquals(expectedAmount, calculateResponse.getCalculatedAmount());
  }

  @Test
  void testCalculateRateForUSDToEUR() {
    double amount = 100.00;
    double expectedAmount = 108.0;
    CalculateRequest calculateRequest = CalculateRequest.newBuilder()
        .setAmount(amount)
        .setDesiredCurrency(CurrencyValues.USD)
        .setSpendCurrency(CurrencyValues.EUR)
        .build();
    CalculateResponse calculateResponse = blockingStub.calculateRate(calculateRequest);
    Assertions.assertEquals(expectedAmount, calculateResponse.getCalculatedAmount());
  }

  @Test
  void testCalculateRateForEURToUSD() {
    double amount = 100.00;
    double expectedAmount = 92.59;
    CalculateRequest calculateRequest = CalculateRequest.newBuilder()
        .setAmount(amount)
        .setDesiredCurrency(CurrencyValues.EUR)
        .setSpendCurrency(CurrencyValues.USD)
        .build();
    CalculateResponse calculateResponse = blockingStub.calculateRate(calculateRequest);
    Assertions.assertEquals(expectedAmount, calculateResponse.getCalculatedAmount());
  }
}
