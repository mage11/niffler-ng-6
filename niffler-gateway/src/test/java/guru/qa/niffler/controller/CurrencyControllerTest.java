package guru.qa.niffler.controller;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import guru.qa.niffler.grpc.Currency;
import guru.qa.niffler.grpc.CurrencyResponse;
import guru.qa.niffler.grpc.CurrencyValues;
import guru.qa.niffler.grpc.NifflerCurrencyServiceGrpc;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.wiremock.grpc.Jetty12GrpcExtensionFactory;
import org.wiremock.grpc.dsl.WireMockGrpcService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.wiremock.grpc.dsl.WireMockGrpc.message;
import static org.wiremock.grpc.dsl.WireMockGrpc.method;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CurrencyControllerTest {

    private final WireMockServer wiremock = new WireMockServer(
        new WireMockConfiguration()
            .port(8091)
            .globalTemplating(true)
            .withRootDirectory("src/test/resources/wiremock")
            .extensions(new Jetty12GrpcExtensionFactory())
    );

    private WireMockGrpcService mockService;

    @BeforeEach
    void beforeEach() {
        wiremock.start();
        mockService = new WireMockGrpcService(
            new WireMock(wiremock.port()),
            NifflerCurrencyServiceGrpc.SERVICE_NAME
        );
    }

    @AfterEach
    void afterEach() {
        wiremock.shutdown();
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetAllCurrencies() throws Exception {
        mockService.stubFor(
            method("GetAllCurrencies")
                .willReturn(message(
                    CurrencyResponse.newBuilder().addAllCurrencies(
                        Currency.newBuilder().setCurrency(CurrencyValues.RUB).setCurrencyRate(1.0).build()
                    ).addAllCurrencies(
                        Currency.newBuilder().setCurrency(CurrencyValues.EUR).setCurrencyRate(0.9).build()
                    ).addAllCurrencies(
                        Currency.newBuilder().setCurrency(CurrencyValues.USD).setCurrencyRate(0.8).build()
                    ).addAllCurrencies(
                        Currency.newBuilder().setCurrency(CurrencyValues.KZT).setCurrencyRate(0.7).build()
                    ).build()
                    )
                )
        );

        mockMvc.perform(get("/api/currencies/all")
                .with(jwt().jwt(c -> c.claim("sub", "user"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].currency").value("RUB"))
            .andExpect(jsonPath("$[0].currencyRate").value(1.0))
            .andExpect(jsonPath("$[1].currency").value("EUR"))
            .andExpect(jsonPath("$[1].currencyRate").value(0.9))
            .andExpect(jsonPath("$[2].currency").value("USD"))
            .andExpect(jsonPath("$[2].currencyRate").value(0.8))
            .andExpect(jsonPath("$[3].currency").value("KZT"))
            .andExpect(jsonPath("$[3].currencyRate").value(0.7))
            .andDo(print());
    }
}
