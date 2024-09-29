package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.UserDao;
import guru.qa.niffler.data.entity.user.UserEntity;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.UUID;

public class UserDaoJdbc implements UserDao {

    private final Connection connection;

    public UserDaoJdbc(Connection connection){
        this.connection = connection;
    }

    @Override
    public UserEntity createUser(UserEntity user) {
            try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO user (username, firstname, surname, full_name, currency, photo, photo_small) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
            )) {
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getFirstname());
                ps.setString(2, user.getSurname());
                ps.setString(2, user.getFullname());
                ps.setString(3, user.getCurrency().name());
                ps.setBytes(4, user.getPhoto());
                ps.setBytes(5, user.getPhotoSmall());

                ps.executeUpdate();

                final UUID generatedKey;
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedKey = rs.getObject("id", UUID.class);
                    } else {
                        throw new SQLException("Can`t find id in ResultSet");
                    }
                }
                user.setId(generatedKey);
                return user;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        Optional<UserEntity> userEntity;
        try (PreparedStatement ps = connection.prepareStatement(
            "SELECT * " +
                "FROM user " +
                "WHERE id = ?;",
            Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setObject(1, id);
            ps.execute();

            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    UserEntity user = new UserEntity();
                    user.setId(rs.getObject("id", UUID.class));
                    user.setFirstname(rs.getString("firstname"));
                    user.setCurrency(rs.getObject("currency", CurrencyValues.class));
                    user.setSurname(rs.getString("surname"));
                    user.setFullname(rs.getString("full_name"));
                    user.setPhoto(rs.getBytes("photo"));
                    user.setPhotoSmall(rs.getBytes("photo_small"));

                    userEntity = Optional.of(user);
                }
                else {
                    return Optional.empty();
                }
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userEntity;
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        Optional<UserEntity> userEntity;
        try (PreparedStatement ps = connection.prepareStatement(
            "SELECT * " +
                "FROM user " +
                "WHERE username = ?",
            Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setObject(1, username);
            ps.execute();

            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    UserEntity user = new UserEntity();
                    user.setId(rs.getObject("id", UUID.class));
                    user.setFirstname(rs.getString("firstname"));
                    user.setCurrency(rs.getObject("currency", CurrencyValues.class));
                    user.setSurname(rs.getString("surname"));
                    user.setFullname(rs.getString("full_name"));
                    user.setPhoto(rs.getBytes("photo"));
                    user.setPhotoSmall(rs.getBytes("photo_small"));

                    userEntity = Optional.of(user);
                }
                else {
                    return Optional.empty();
                }
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userEntity;
    }

    @Override
    public void delete(UserEntity user) {
        try (PreparedStatement ps = connection.prepareStatement(
            "DELETE " +
                "FROM user " +
                "WHERE id = ?",
            Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setObject(1, user.getId());
            ps.execute();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
