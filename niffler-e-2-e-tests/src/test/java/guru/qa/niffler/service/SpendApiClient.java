package guru.qa.niffler.service;

import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpendApiClient implements SpendClient{

    private final Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(Config.getInstance().spendUrl())
        .addConverterFactory(JacksonConverterFactory.create())
        .build();
    private final SpendApi spendApi = retrofit.create(SpendApi.class);

    @Override
    public SpendJson createSpend(SpendJson spend) {
        final Response<SpendJson> response;
        try {
            response = spendApi.addSpend(spend)
                .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(201, response.code());
        return response.body();
    }

    @Override
    public SpendJson updateSpend(SpendJson spend) {
        final Response<SpendJson> response;
        try {
            response = spendApi.editSpend(spend)
                .execute();
        } catch (IOException e){
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    @Override
    @Deprecated
    public Optional<SpendJson> findSpendById(UUID id) {
        throw new UnsupportedOperationException("Нужно использовать метод getSpend(String id, String username)");
    }

    public SpendJson getSpend(String id){
        final Response<SpendJson> response;
        try {
            response = spendApi.getSpend(id)
                .execute();
        } catch (IOException e){
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    @Override
    @Deprecated
    public List<SpendJson> findSpendByUsernameAndDescription(String username, String description) {
        throw new UnsupportedOperationException("Не поддерживается. Нужно использовать getSpends(String username, CurrencyValues filterCurrency, Date from, Date to)");
    }

    public List<SpendJson> getSpends(String username, CurrencyValues filterCurrency, String from, String to){
        final Response<List<SpendJson>> response;
        try {
            response = spendApi.allSpends(username, filterCurrency, from, to)
                .execute();
        } catch (IOException e){
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    @Override
    public void deleteSpend(SpendJson spend) {
        final Response<Void> response;
        try {
            response = spendApi.removeSpends(spend.username(), List.of(spend.id().toString()))
                .execute();
        } catch (IOException e){
            throw new AssertionError(e);
        }
        assertEquals(202, response.code());

    }

    @Override
    public CategoryJson createCategory(CategoryJson category) {
        final Response<CategoryJson> response;
        try {
            response = spendApi.addCategory(category)
                .execute();
        } catch (IOException e){
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    @Override
    public CategoryJson updateCategory(CategoryJson category) {
        final Response<CategoryJson> response;
        try {
            response = spendApi.updateCategory(category)
                .execute();
        } catch (IOException e){
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    @Override
    @Deprecated
    public Optional<CategoryJson> findCategoryById(UUID id) {
        throw new UnsupportedOperationException("Не поддерживается. Нужно использовать getCategories(String username, boolean excludeArchived)");
    }

    public List<CategoryJson> getCategories(String username){
        final Response<List<CategoryJson>> response;
        try {
            response = spendApi.allCategories(username)
                .execute();
        } catch (IOException e){
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    @Override
    @Deprecated
    public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String name) {
        throw new UnsupportedOperationException("Не поддерживается. Нужно использовать getCategories(String username, boolean excludeArchived)");
    }

    @Override
    @Deprecated
    public void deleteCategory(CategoryJson category) {
        throw new UnsupportedOperationException("Не поддерживается.");
    }
}
