package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.model.rest.CurrencyValues;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.jdbc.Connections.holder;

public class UserdataUserRepositoryJdbc implements UserdataUserRepository {

    private static final Config CFG = Config.getInstance();

    @Override
    public UserEntity create(UserEntity user) {
        try (PreparedStatement userPs = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
            "INSERT INTO \"user\" (username, currency, firstname, surname, photo, photo_small, full_name) " +
                "VALUES (?,?,?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            userPs.setString(1, user.getUsername());
            userPs.setString(2, user.getCurrency().name());
            userPs.setString(3, user.getFirstname());
            userPs.setString(4, user.getSurname());
            userPs.setBytes(5, user.getPhoto());
            userPs.setBytes(6, user.getPhotoSmall());
            userPs.setString(7, user.getFullname());

            userPs.executeUpdate();

            final UUID generatedKey;
            try (ResultSet rs = userPs.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedKey = rs.getObject("id", UUID.class);
                }
                else {
                    throw new SQLException("Can`t find id in ResultSet");
                }
            }
            user.setId(generatedKey);
            return user;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
            "SELECT * FROM \"user\" WHERE id = ?")) {
            ps.setObject(1, id);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    UserEntity user = new UserEntity();
                    user.setId(rs.getObject("id", UUID.class));
                    user.setUsername(rs.getString("username"));
                    user.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                    user.setFirstname(rs.getString("firstname"));
                    user.setSurname(rs.getString("surname"));
                    user.setFullname(rs.getString("full_name"));
                    user.setPhoto(rs.getBytes("photo"));
                    user.setPhotoSmall(rs.getBytes("photo_small"));
                    return Optional.of(user);
                }
                else {
                    return Optional.empty();
                }
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
            "SELECT * FROM \"user\" WHERE username = ?")) {
            ps.setObject(1, username);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    UserEntity user = new UserEntity();
                    user.setId(rs.getObject("id", UUID.class));
                    user.setUsername(rs.getString("username"));
                    user.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                    user.setFirstname(rs.getString("firstname"));
                    user.setSurname(rs.getString("surname"));
                    user.setFullname(rs.getString("full_name"));
                    user.setPhoto(rs.getBytes("photo"));
                    user.setPhotoSmall(rs.getBytes("photo_small"));
                    return Optional.of(user);
                }
                else {
                    return Optional.empty();
                }
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addFriend(UserEntity requester, UserEntity addressee) {
        try (PreparedStatement requesterPs = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
            "INSERT INTO \"friendship\" (requester_id, addressee_id, status) " +
                "VALUES (?,?,?)");
             PreparedStatement addresseePs = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                 "INSERT INTO \"friendship\" (requester_id, addressee_id, status) " +
                     "VALUES (?,?,?)")) {

            requesterPs.setObject(1, requester.getId());
            requesterPs.setObject(2, addressee.getId());
            requesterPs.setString(3, FriendshipStatus.ACCEPTED.toString());
            requesterPs.executeUpdate();

            addresseePs.setObject(1, addressee.getId());
            addresseePs.setObject(2, requester.getId());
            addresseePs.setString(3, FriendshipStatus.ACCEPTED.toString());
            addresseePs.executeUpdate();

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserEntity update(UserEntity user) {
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
            "UPDATE \"user\" SET currency = ?, firstname = ?, surname = ?, photo = ?, " +
                "photo_small = ?, full_name = ? WHERE id = ?")) {
            ps.setString(1, user.getCurrency().name());
            ps.setString(2, user.getFirstname());
            ps.setString(3, user.getSurname());
            ps.setBytes(4, user.getPhoto());
            ps.setBytes(5, user.getPhotoSmall());
            ps.setString(6, user.getFullname());
            ps.setObject(7, user.getId());
            ps.executeUpdate();
            return user;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(UserEntity user) {
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
            "DELETE FROM \"user\" WHERE id = ?")) {
            ps.setObject(1, user.getId());
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addIncomeInvitation(UserEntity requester, UserEntity addressee) {
        try (PreparedStatement requesterPs = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
            "INSERT INTO \"friendship\" (requester_id, addressee_id, status) " +
                "VALUES (?,?,?)")) {

            requesterPs.setObject(1, requester.getId());
            requesterPs.setObject(2, addressee.getId());
            requesterPs.setString(3, FriendshipStatus.PENDING.toString());
            requesterPs.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addOutcomeInvitation(UserEntity requester, UserEntity addressee) {
        try (PreparedStatement requesterPs = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
            "INSERT INTO \"friendship\" (requester_id, addressee_id, status) " +
                "VALUES (?,?,?)")) {

            requesterPs.setObject(1, addressee.getId());
            requesterPs.setObject(2, requester.getId());
            requesterPs.setString(3, FriendshipStatus.PENDING.toString());
            requesterPs.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
