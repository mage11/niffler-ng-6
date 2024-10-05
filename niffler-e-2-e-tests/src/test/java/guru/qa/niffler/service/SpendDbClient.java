package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendDbClient {

    private static final Config CFG = Config.getInstance();

    private final CategoryDao categoryDao = new CategoryDaoJdbc();
    private final SpendDao spendDao = new SpendDaoJdbc();

    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(
        CFG.spendJdbcUrl()
    );

    public SpendJson createSpend(SpendJson spend) {
        return jdbcTxTemplate.execute(() -> {
                SpendEntity spendEntity = SpendEntity.fromJson(spend);
                if (spendEntity.getCategory().getId() == null) {
                    CategoryEntity categoryEntity = categoryDao.create(spendEntity.getCategory());
                    spendEntity.setCategory(categoryEntity);
                }
                return SpendJson.fromEntity(
                    spendDao.create(spendEntity)
                );
            }
        );
    }

    public Optional<SpendEntity> findSpendById(UUID id) {
        return jdbcTxTemplate.execute(() -> {
            return spendDao.findSpendById(id);
        });
    }

    public List<SpendEntity> findAllSpendsByUserName(String username) {
        return jdbcTxTemplate.execute(() -> {
            return spendDao.findAllByUserName(username);
        });
    }

    public void deleteSpend(SpendEntity spend) {
        jdbcTxTemplate.execute(() -> {
            spendDao.deleteSpend(spend);
            return null;
        });
    }

    public CategoryJson createCategory(CategoryJson category) {
        return jdbcTxTemplate.execute(() -> {
            CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
            return CategoryJson.fromEntity(categoryDao.create(categoryEntity));
        });
    }

    public Optional<CategoryEntity> findCategoryById(UUID id) {
        return jdbcTxTemplate.execute(() -> {
            return categoryDao.findCategoryById(id);
        });
    }

    public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        return jdbcTxTemplate.execute(() -> {
            return categoryDao.findCategoryByUsernameAndCategoryName(username, categoryName);
        });
    }

    public List<CategoryEntity> findAllCategoriesByUsername(String username) {
        return jdbcTxTemplate.execute(() -> {
            return categoryDao.findAllByUsername(username);
        });
    }

    public void deleteCategory(CategoryEntity category) {
        jdbcTxTemplate.execute(() -> {
            categoryDao.deleteCategory(category);
            return null;
        });
    }
}
