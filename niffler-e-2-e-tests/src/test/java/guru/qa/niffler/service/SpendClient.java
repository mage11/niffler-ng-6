package guru.qa.niffler.service;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public interface SpendClient {

    @Nonnull
    SpendJson createSpend(SpendJson spend);

    @Nonnull
    CategoryJson createCategory(CategoryJson category);

    @Nonnull
    SpendJson updateSpend(SpendJson spend);

    @Nonnull
    Optional<SpendJson> findSpendById(UUID id);

    @Nonnull
    List<SpendJson> findSpendByUsernameAndDescription(String username, String description);

    void deleteSpend(SpendJson spend);

    @Nonnull
    CategoryJson updateCategory(CategoryJson category);

    @Nonnull
    Optional<CategoryJson> findCategoryById(UUID id);

    @Nonnull
    Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String name);

    void deleteCategory(CategoryJson category);

}
