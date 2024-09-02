package guru.qa.niffler.data.dao.impl;

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
  private final Connection connection;

  public SpendDaoJdbc(Connection connection) {
    this.connection = connection;
  }

  @Override
  public SpendEntity create(SpendEntity spend) {
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
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
  }

  @Override
  public Optional<SpendEntity> findSpendById(UUID id){
    Optional<SpendEntity> spendEntities;
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
            spendEntity.setCategory(rs.getObject("category_id", CategoryEntity.class));

            spendEntities = Optional.of(spendEntity);
          } else {
            return Optional.empty();
          }
        }
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }

      return spendEntities;
  }

  @Override
  public List<SpendEntity> findAllByUserName(String username){
    List<SpendEntity> spendEntities = new ArrayList<>();
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
            spendEntity.setCategory(rs.getObject("category_id", CategoryEntity.class));
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
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }

      return spendEntities;
  }

  @Override
  public void deleteSpend(SpendEntity spend){
      try (PreparedStatement ps = connection.prepareStatement(
          "DELETE " +
              "FROM spend " +
              "WHERE id = ?",
          Statement.RETURN_GENERATED_KEYS
      )){
        ps.setObject(1, spend.getId());
        ps.execute();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
  }
}
