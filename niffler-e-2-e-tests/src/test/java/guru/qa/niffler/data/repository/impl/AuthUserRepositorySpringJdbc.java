package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.extractor.AuthUserEntityExtractor;
import guru.qa.niffler.data.jdbc.DataSources;
import guru.qa.niffler.data.repository.AuthUserRepository;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Optional;
import java.util.UUID;

public class AuthUserRepositorySpringJdbc implements AuthUserRepository {

    private static final Config CFG = Config.getInstance();

    private final String url = CFG.authJdbcUrl();
    private final AuthUserDao authUserDao = new AuthUserDaoSpringJdbc();
    private final AuthAuthorityDao authAuthorityDao = new AuthAuthorityDaoSpringJdbc();
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));

    @Override
    public AuthUserEntity create(AuthUserEntity user) {
        authUserDao.create(user);
        authAuthorityDao.create(user.getAuthorities().toArray(new AuthorityEntity[0]));
        return user;
    }

    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(url));
        return Optional.ofNullable(
            jdbcTemplate.query(
                """
                        SELECT a.id as authority_id,
                       authority,
                       user_id as id,
                       u.username,
                       u.password,
                       u.enabled,
                       u.account_non_expired,
                       u.account_non_locked,
                       u.credentials_non_expired
                       FROM "user" u join authority a on u.id = a.user_id WHERE u.id = ?
                    """,
                AuthUserEntityExtractor.instance,
                id
            )
        );
    }

    @Override
    public Optional<AuthUserEntity> findByUsername(String username) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(url));
        return Optional.ofNullable(
            jdbcTemplate.query(
                """
                        SELECT a.id as authority_id,
                       authority,
                       user_id as id,
                       u.username,
                       u.password,
                       u.enabled,
                       u.account_non_expired,
                       u.account_non_locked,
                       u.credentials_non_expired
                       FROM "user" u join authority a on u.id = a.user_id WHERE u.username = ?
                    """,
                AuthUserEntityExtractor.instance,
                username
            )
        );
    }

    @Override
    public AuthUserEntity update(AuthUserEntity user) {
        jdbcTemplate.update(
            "DELETE FROM \"authority\" WHERE user_id = ?",
            user.getId());
        jdbcTemplate.update(
            "UPDATE \"user\" SET username = ?, password = ?, account_non_expired = ?, account_non_locked = ?, " +
                "credentials_non_expired = ? WHERE id = ?",
            user.getUsername(),
            user.getPassword(),
            user.getAccountNonExpired(),
            user.getAccountNonLocked(),
            user.getCredentialsNonExpired(),
            user.getId()
        );
        jdbcTemplate.batchUpdate(
            "INSERT INTO \"authority\" (user_id, authority) VALUES (?, ?)",
            user.getAuthorities(),
            user.getAuthorities().size(),
            (ps, authority) -> {
                ps.setObject(1, user.getId());
                ps.setString(2, authority.getAuthority().name());
            });

        return user;
    }

    @Override
    public void remove(AuthUserEntity user) {
        jdbcTemplate.update(
            "DELETE FROM \"authority\" WHERE user_id = ?",
            user.getId());
        jdbcTemplate.update(
            "DELETE FROM \"user\" WHERE id = ?",
            user.getId());
    }
}
