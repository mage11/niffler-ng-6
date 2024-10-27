package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.jdbc.DataSources;

import guru.qa.niffler.data.mapper.UserdataUserEntityRowMapper;
import guru.qa.niffler.data.repository.UserdataUserRepository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;
import java.util.UUID;

public class UserdataUserRepositorySpringJdbc implements UserdataUserRepository {

    private static final Config CFG = Config.getInstance();
    private final String url = CFG.userdataJdbcUrl();
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(url));

    @Override
    public UserEntity create(UserEntity user) {
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO \"user\" (username, currency, firstname, surname, photo, photo_small, full_name) " +
                    "VALUES (?,?,?,?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getCurrency().name());
            ps.setString(3, user.getFirstname());
            ps.setString(4, user.getSurname());
            ps.setBytes(5, user.getPhoto());
            ps.setBytes(6, user.getPhotoSmall());
            ps.setString(7, user.getFullname());
            return ps;
        }, kh);

        final UUID generatedKey = (UUID) kh.getKeys().get("id");
        user.setId(generatedKey);
        return user;
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        return Optional.ofNullable(
            jdbcTemplate.queryForObject(
                "SELECT * FROM \"user\" WHERE id = ?",
                UserdataUserEntityRowMapper.instance,
                id
            )
        );
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return Optional.ofNullable(
            jdbcTemplate.queryForObject(
                "SELECT * FROM \"user\" WHERE username = ?",
                UserdataUserEntityRowMapper.instance,
                username
            )
        );
    }

    @Override
    public void addIncomeInvitation(UserEntity requester, UserEntity addressee) {
        jdbcTemplate.update(
            "INSERT INTO \"friendship\" (requester_id, addressee_id, status) " +
                "VALUES (?,?,?)",
            requester.getId(),
            addressee.getId(),
            FriendshipStatus.PENDING.toString());
    }

    @Override
    public void addOutcomeInvitation(UserEntity requester, UserEntity addressee) {
        jdbcTemplate.update(
            "INSERT INTO \"friendship\" (requester_id, addressee_id, status) " +
                "VALUES (?,?,?)",
            addressee.getId(),
            requester.getId(),
            FriendshipStatus.PENDING.toString());
    }

    @Override
    public void addFriend(UserEntity requester, UserEntity addressee) {
        String sql = "INSERT INTO \"friendship\" (requester_id, addressee_id, status) " +
            "VALUES (?,?,?)";
        jdbcTemplate.update(sql,
            requester.getId(),
            addressee.getId(),
            FriendshipStatus.ACCEPTED.toString());
        jdbcTemplate.update(sql,
            addressee.getId(),
            requester.getId(),
            FriendshipStatus.ACCEPTED.toString());
    }

    @Override
    public UserEntity update(UserEntity user) {
        jdbcTemplate.update("UPDATE \"user\" SET currency = ?, firstname = ?, surname = ?, photo = ?, " +
                "photo_small = ?, full_name = ? WHERE id = ?",
            user.getCurrency().name(),
            user.getFirstname(),
            user.getSurname(),
            user.getPhoto(),
            user.getPhotoSmall(),
            user.getFullname(),
            user.getId()
        );
        return user;
    }

    @Override
    public void remove(UserEntity user) {
        jdbcTemplate.update("DELETE FROM \"user\" WHERE id = ?", user.getId());
    }
}
