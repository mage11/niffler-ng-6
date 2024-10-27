package guru.qa.niffler.service.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.data.repository.impl.SpendRepositoryHibernate;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendClient;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static java.util.Objects.requireNonNull;

@ParametersAreNonnullByDefault
public class SpendDbClient implements SpendClient {

    private static final Config CFG = Config.getInstance();
    private final SpendRepository spendRepository = new SpendRepositoryHibernate();
    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(CFG.spendJdbcUrl());

    @Nonnull
    @Override
    public SpendJson createSpend(SpendJson spend) {
        return xaTransactionTemplate.execute(() -> {
                SpendEntity spendEntity = SpendEntity.fromJson(spend);
                if (spendEntity.getCategory().getId() == null) {
                    CategoryEntity categoryEntity = spendRepository.createCategory(spendEntity.getCategory());
                    spendEntity.setCategory(categoryEntity);
                }
                else {
                    spendEntity.setCategory(spendRepository.updateCategory(spendEntity.getCategory()));
                }
                return SpendJson.fromEntity(
                    spendRepository.create(spendEntity)
                );
            }
        );
    }

    @Nonnull
    @Override
    public SpendJson updateSpend(SpendJson spend) {
        return xaTransactionTemplate.execute(() -> {
                SpendEntity spendEntity = SpendEntity.fromJson(spend);
                if (spendEntity.getCategory().getId() == null) {
                    CategoryEntity categoryEntity = spendRepository.createCategory(spendEntity.getCategory());
                    spendEntity.setCategory(categoryEntity);
                }
                return SpendJson.fromEntity(
                    spendRepository.update(spendEntity)
                );
            }
        );
    }

    @Nonnull
    @Override
    public Optional<SpendJson> findSpendById(UUID id) {
        return xaTransactionTemplate.execute(() -> {
            return spendRepository.findById(id).map(SpendJson::fromEntity);
        });
    }

    @Nonnull
    @Override
    public List<SpendJson> findSpendByUsernameAndDescription(String username, String description) {
        return xaTransactionTemplate.execute(() -> {
            return spendRepository.findByUsernameAndSpendDescription(username, description)
                .stream()
                .map(SpendJson::fromEntity)
                .toList();
        });
    }

    @Override
    public void deleteSpend(SpendJson spend) {
        xaTransactionTemplate.execute(() -> {
            spendRepository.remove(SpendEntity.fromJson(spend));
            return null;
        });
    }

    @Nonnull
    @Override
    public CategoryJson createCategory(CategoryJson category) {
        return xaTransactionTemplate.execute(() -> {
            CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
            return CategoryJson.fromEntity(spendRepository.createCategory(categoryEntity));
        });
    }

    @Nonnull
    @Override
    public CategoryJson updateCategory(CategoryJson category) {
        return xaTransactionTemplate.execute(() -> {
            CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
            return CategoryJson.fromEntity(spendRepository.updateCategory(categoryEntity));
        });
    }

    public Optional<CategoryJson> findCategoryById(UUID id) {
        return xaTransactionTemplate.execute(() -> {
            return spendRepository.findCategoryById(id).map(CategoryJson::fromEntity);
        });
    }

    @Nonnull
    @Override
    public Optional<CategoryJson> findCategoryByUsernameAndCategoryName(String username, String spendName) {
        return xaTransactionTemplate.execute(() -> {
            return spendRepository.findCategoryByUsernameAndSpendName(username, spendName).map(CategoryJson::fromEntity);
        });
    }

    @Override
    public void deleteCategory(CategoryJson category) {
        xaTransactionTemplate.execute(() -> {
            spendRepository.removeCategory(CategoryEntity.fromJson(category));
            return null;
        });
    }
}
