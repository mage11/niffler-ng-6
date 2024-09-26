package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendDaoJdbc implements SpendDao {

  private static final Config CFG = Config.getInstance();
  private final CategoryDao categoryDao = new CategoryDaoJdbc();

  @Override
  public SpendEntity create(SpendEntity spend) {
    try (Connection connection = Databases.connection(CFG.spendJdbcUrl())) {
      try (PreparedStatement ps = connection.prepareStatement(
          "INSERT INTO spend (username, spend_date, currency, amount, description, category_id) " +
              "VALUES ( ?, ?, ?, ?, ?, ?)",
          Statement.RETURN_GENERATED_KEYS
      )) {
        ps.setString(1, spend.getUsername());
        ps.setDate(2, spend.getSpendDate());
        ps.setString(3, spend.getCurrency().name());
        ps.setDouble(4, spend.getAmount());
        ps.setString(5, spend.getDescription());
        ps.setObject(6, spend.getCategory().getId());

        ps.executeUpdate();

        final UUID generatedKey;
        try (ResultSet rs = ps.getGeneratedKeys()) {
          if (rs.next()) {
            generatedKey = rs.getObject("id", UUID.class);
          } else {
            throw new SQLException("Can`t find id in ResultSet");
          }
        }
        spend.setId(generatedKey);
        return spend;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<SpendEntity> findSpendById(UUID id){
    Optional<SpendEntity> spendEntities;
    try (Connection connection = Databases.connection(CFG.spendJdbcUrl())) {
      try (PreparedStatement ps = connection.prepareStatement(
          "SELECT * " +
              "FROM spend " +
              "WHERE id = ?;",
          Statement.RETURN_GENERATED_KEYS
      )){
        ps.setObject(1, id);
        ps.execute();

        try(ResultSet rs = ps.getResultSet()){
          if (rs.next()) {
            SpendEntity spendEntity = new SpendEntity();
            spendEntity.setId(rs.getObject("id", UUID.class));
            spendEntity.setSpendDate(rs.getDate("spend_date"));
            spendEntity.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
            spendEntity.setAmount(rs.getDouble("amount"));
            spendEntity.setDescription(rs.getString("description"));
            spendEntity.setUsername(rs.getString("username"));

            UUID categoryId = rs.getObject("category_id", UUID.class);
            CategoryEntity category = categoryDao.findCategoryById(categoryId).get();
            spendEntity.setCategory(category);

            spendEntities = Optional.of(spendEntity);
          } else {
            return Optional.empty();
          }
        }
      }

      return spendEntities;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<SpendEntity> findAllByUserName(String username){
    List<SpendEntity> spendEntities = new ArrayList<>();
    try (Connection connection = Databases.connection(CFG.spendJdbcUrl())) {
      try (PreparedStatement ps = connection.prepareStatement(
          "SELECT * " +
              "FROM spend " +
              "WHERE username = ?",
          Statement.RETURN_GENERATED_KEYS
      )){
        ps.setObject(1, username);
        ps.execute();

        try(ResultSet rs = ps.getResultSet()){
          if (rs.next()) {
            SpendEntity spendEntity = new SpendEntity();
            UUID categoryId = rs.getObject("category_id", UUID.class);
            CategoryEntity categoryEntity = categoryDao.findCategoryById(categoryId).get();

            spendEntity.setCategory(categoryEntity);
            spendEntity.setId(rs.getObject("id", UUID.class));
            spendEntity.setSpendDate(rs.getDate("spend_date"));
            spendEntity.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
            spendEntity.setAmount(rs.getDouble("amount"));
            spendEntity.setDescription(rs.getString("description"));
            spendEntity.setUsername(rs.getString("username"));

            spendEntities.add(spendEntity);
          } else {
            return Collections.emptyList();
          }
        }
      }

      return spendEntities;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteSpend(SpendEntity spend){
    try (Connection connection = Databases.connection(CFG.spendJdbcUrl())) {
      try (PreparedStatement ps = connection.prepareStatement(
          "DELETE " +
              "FROM spend " +
              "WHERE id = ?",
          Statement.RETURN_GENERATED_KEYS
      )){
        ps.setObject(1, spend.getId());
        ps.execute();
      }

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
