package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;

import java.util.Optional;
import java.util.UUID;

public class AuthUserRepositoryJdbc implements AuthUserRepository {

  private final AuthUserDao authUserDao = new AuthUserDaoJdbc();
  private final AuthAuthorityDao authAuthorityDao = new AuthAuthorityDaoJdbc();

  @Override
  public AuthUserEntity create(AuthUserEntity user) {
    authUserDao.create(user);
    authAuthorityDao.create(user.getAuthorities().toArray(new AuthorityEntity[0]));
    return user;
  }

  @Override
  public AuthUserEntity update(AuthUserEntity user) {
    try (PreparedStatement userPs = holder(CFG.authJdbcUrl()).connection().prepareStatement(
        "UPDATE \"user\" SET password = ?, enabled = ?, " +
            "account_non_expired = ?, account_non_locked = ?, credentials_non_expired = ? " +
            "WHERE id = ?");
         PreparedStatement authorityDeletePs = holder(CFG.authJdbcUrl()).connection().prepareStatement(
             "DELETE FROM \"authority\" WHERE user_id = ?");
         PreparedStatement authorityInsertPs = holder(CFG.authJdbcUrl()).connection().prepareStatement(
             "INSERT INTO \"authority\" (user_id, authority) VALUES (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
      authorityDeletePs.setObject(1, user.getId());
      authorityDeletePs.executeUpdate();

      for (AuthorityEntity a : user.getAuthorities()) {
        authorityInsertPs.setObject(1, user.getId());
        authorityInsertPs.setString(2, a.getAuthority().name());
        authorityInsertPs.addBatch();
        authorityInsertPs.clearParameters();
      }
      authorityInsertPs.executeBatch();

      userPs.setString(1, user.getPassword());
      userPs.setBoolean(2, user.getEnabled());
      userPs.setBoolean(3, user.getAccountNonExpired());
      userPs.setBoolean(4, user.getAccountNonLocked());
      userPs.setBoolean(5, user.getCredentialsNonExpired());
      userPs.setObject(6, user.getId());

      userPs.executeUpdate();

      return user;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<AuthUserEntity> findById(UUID id) {
    Optional<AuthUserEntity> userEntity = authUserDao.findById(id);
    userEntity
        .ifPresent(authUserEntity ->
            authUserEntity.addAuthorities(
                authAuthorityDao.findAllByUserId(authUserEntity.getId()).toArray(new AuthorityEntity[0])
            )
        );
    return userEntity;
  }

  @Override
  public Optional<AuthUserEntity> findByUsername(String username) {
    Optional<AuthUserEntity> userEntity = authUserDao.findByUsername(username);
    userEntity.ifPresent(authUserEntity ->
        authUserEntity.addAuthorities(
            authAuthorityDao.findAllByUserId(authUserEntity.getId()).toArray(new AuthorityEntity[0])
        )
    );
    return userEntity;
  }

  @Override
  public void remove(AuthUserEntity user) {
    try (PreparedStatement userPs = holder(CFG.authJdbcUrl()).connection().prepareStatement(
        "DELETE FROM \"user\"" +
            "WHERE id = ?");
         PreparedStatement authorityDeletePs = holder(CFG.authJdbcUrl()).connection().prepareStatement(
             "DELETE FROM \"authority\" WHERE user_id = ?")) {
      authorityDeletePs.setObject(1, user.getId());
      authorityDeletePs.executeUpdate();

      userPs.setObject(1, user.getId());
      userPs.executeUpdate();

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
