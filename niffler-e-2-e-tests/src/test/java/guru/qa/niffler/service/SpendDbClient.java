package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import static guru.qa.niffler.data.Databases.transaction;
import static java.sql.Connection.TRANSACTION_SERIALIZABLE;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendDbClient {

  private static final Config CFG = Config.getInstance();

  public SpendJson createSpend(SpendJson spend) {
    return transaction(connection -> {
          SpendEntity spendEntity = SpendEntity.fromJson(spend);
          if (spendEntity.getCategory().getId() == null) {
            CategoryEntity categoryEntity = new CategoryDaoJdbc(connection)
                .create(spendEntity.getCategory());
            spendEntity.setCategory(categoryEntity);
          }
          return SpendJson.fromEntity(
              new SpendDaoJdbc(connection).create(spendEntity));
        },
        CFG.spendJdbcUrl()
    );
  }

  public Optional<SpendEntity> findSpendById(UUID id){
    return transaction(connection -> {
      return new SpendDaoJdbc(connection).findSpendById(id);
    },
        CFG.spendJdbcUrl());
  }

  public List<SpendEntity> findAllSpendsByUserName(String username){
    return transaction(connection -> {
      return new SpendDaoJdbc(connection).findAllByUserName(username);
    },
        CFG.spendJdbcUrl());
  }

  public void deleteSpend(SpendEntity spend){
     transaction(connection -> {
      new SpendDaoJdbc(connection).deleteSpend(spend);
             return null;
         },
        CFG.spendJdbcUrl());
  }

  public CategoryJson createCategory(CategoryJson category){
    return transaction(connection -> {
      CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
      return CategoryJson.fromEntity(new CategoryDaoJdbc(connection).create(categoryEntity));
    }, CFG.spendJdbcUrl());
  }

  public Optional<CategoryEntity> findCategoryById(UUID id){
    return transaction(connection -> {
          return new CategoryDaoJdbc(connection).findCategoryById(id);
        },
        CFG.spendJdbcUrl());
  }

  public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName){
    return transaction(connection -> {
          return new CategoryDaoJdbc(connection).findCategoryByUsernameAndCategoryName(username, categoryName);
        },
        CFG.spendJdbcUrl());
  }

  public List<CategoryEntity> findAllCategoriesByUsername(String username){
    return transaction(connection -> {
          return new CategoryDaoJdbc(connection).findAllByUsername(username);
        },
        CFG.spendJdbcUrl());
  }

  public void deleteCategory(CategoryEntity category){
    transaction(connection -> {
          new CategoryDaoJdbc(connection).deleteCategory(category);
            return null;
        },
        CFG.spendJdbcUrl());
  }
}
