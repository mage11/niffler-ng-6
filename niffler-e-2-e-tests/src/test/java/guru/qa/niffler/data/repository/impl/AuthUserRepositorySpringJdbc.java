package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.mapper.AuthUserEntityRowMapper;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;
import java.util.UUID;

public class AuthUserRepositorySpringJdbc implements AuthUserRepository {

    private static final Config CFG = Config.getInstance();
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));

    @Override
    public AuthUserEntity create(AuthUserEntity user) {
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO \"user\" (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
                    "VALUES (?,?,?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setBoolean(3, user.getEnabled());
            ps.setBoolean(4, user.getAccountNonExpired());
            ps.setBoolean(5, user.getAccountNonLocked());
            ps.setBoolean(6, user.getCredentialsNonExpired());
            return ps;
        }, kh);

        final UUID generatedKey = (UUID) kh.getKeys().get("id");
        user.setId(generatedKey);
        return user;
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
            ( ps,  authority) -> {
                ps.setObject(1, user.getId());
                ps.setString(2, authority.getAuthority().name());
            });

        return user;
    }

    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        return Optional.ofNullable(
            jdbcTemplate.queryForObject(
                "SELECT * FROM \"user\" WHERE id = ?",
                AuthUserEntityRowMapper.instance,
                id
            )
        );
    }

    @Override
    public Optional<AuthUserEntity> findByUsername(String username) {
        return Optional.ofNullable(
            jdbcTemplate.queryForObject(
                "SELECT * FROM \"user\" WHERE username = ?",
                AuthUserEntityRowMapper.instance,
                username
            )
        );
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
