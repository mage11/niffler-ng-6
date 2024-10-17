package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.mapper.CategoryEntityRowMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.jdbc.Connections.holder;

public class CategoryDaoJdbc implements CategoryDao {

  private static final Config CFG = Config.getInstance();
  private final String url = CFG.spendJdbcUrl();

  @Override
  public CategoryEntity create(CategoryEntity category) {
    try (PreparedStatement ps = holder(url).connection().prepareStatement(
        "INSERT INTO category (username, name, archived) " +
            "VALUES (?, ?, ?)",
        Statement.RETURN_GENERATED_KEYS
    )) {
      ps.setString(1, category.getUsername());
      ps.setString(2, category.getName());
      ps.setBoolean(3, category.isArchived());

      ps.executeUpdate();

      final UUID generatedKey;
      try (ResultSet rs = ps.getGeneratedKeys()) {
        if (rs.next()) {
          generatedKey = rs.getObject("id", UUID.class);
        } else {
          throw new SQLException("Can`t find id in ResultSet");
        }
      }
      category.setId(generatedKey);
      return category;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<CategoryEntity> findById(UUID id) {
    try (PreparedStatement ps = holder(url).connection().prepareStatement(
        "SELECT * FROM category WHERE id = ?"
    )) {
      ps.setObject(1, id);
      ps.execute();
      try (ResultSet rs = ps.getResultSet()) {
        if (rs.next()) {
          return Optional.ofNullable(
              CategoryEntityRowMapper.instance.mapRow(rs, rs.getRow())
          );
        } else {
          return Optional.empty();
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
    Optional<CategoryEntity> categoryEntities;
      try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
          "SELECT * FROM category WHERE username = ? and name = ?;"
      )) {
        ps.setString(1, username);
        ps.setString(2, categoryName);
        ps.execute();

        try (ResultSet rs = ps.getResultSet()) {
          if (rs.next()) {
            CategoryEntity categoryEntity = new CategoryEntity();
            categoryEntity.setId(rs.getObject("id", UUID.class));
            categoryEntity.setUsername(rs.getString("username"));
            categoryEntity.setName(rs.getString("name"));
            categoryEntity.setArchived(rs.getBoolean("archived"));
            categoryEntities = Optional.of(categoryEntity);
          } else {
            return Optional.empty();
          }
        }
      }  catch (SQLException e) {
        throw new RuntimeException(e);
      }

      return categoryEntities;
  }


  @Override
  public List<CategoryEntity> findAllByUsername(String username) {
    List<CategoryEntity> categoryEntities = new ArrayList<>();
      try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
          "SELECT * FROM category WHERE username = ?;"
      )) {
        ps.setString(1, username);
        ps.execute();

        try (ResultSet rs = ps.getResultSet()) {
          if (rs.next()) {
            CategoryEntity categoryEntity = new CategoryEntity();
            categoryEntity.setId(rs.getObject("id", UUID.class));
            categoryEntity.setUsername(rs.getString("username"));
            categoryEntity.setName(rs.getString("name"));
            categoryEntity.setArchived(rs.getBoolean("archived"));
            categoryEntities.add(categoryEntity);
          }
        }
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
      return categoryEntities;
  }

  @Override
  public List<CategoryEntity> findAll() {
    try (PreparedStatement ps = holder(url).connection().prepareStatement(
        "SELECT * FROM category")) {
      ps.execute();
      List<CategoryEntity> result = new ArrayList<>();
      try (ResultSet rs = ps.getResultSet()) {
        while (rs.next()) {
          result.add(
              CategoryEntityRowMapper.instance.mapRow(rs, rs.getRow())
          );
        }
      }
      return result;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

    @Override
    public void deleteCategory(CategoryEntity category) {
        try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
            "DELETE FROM category WHERE id = ?;"
        )) {
            ps.setObject(1, category.getId());
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

  @Override
  public CategoryEntity update(CategoryEntity category) {
    try (PreparedStatement ps = holder(url).connection().prepareStatement(
        """
              UPDATE "category"
                SET name     = ?,
                    archived = ?
                WHERE id = ?
            """);
    ) {
      ps.setString(1, category.getName());
      ps.setBoolean(2, category.isArchived());
      ps.setObject(3, category.getId());
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return category;
  }
}
