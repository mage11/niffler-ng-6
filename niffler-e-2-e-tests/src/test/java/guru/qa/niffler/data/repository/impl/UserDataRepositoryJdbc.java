package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.UserDataRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class UserDataRepositoryJdbc implements UserDataRepository {

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
