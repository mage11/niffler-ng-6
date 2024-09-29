package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.UserDaoJdbc;
import guru.qa.niffler.data.entity.user.UserEntity;

import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.transaction;

public class UserDataDbClient {
    private static final Config CFG = Config.getInstance();
    public UserEntity createUser(UserEntity user){
        return transaction(connection -> {
            return new UserDaoJdbc(connection).createUser(user);
        }, CFG.userdataJdbcUrl());
    }

    public Optional<UserEntity> findById(UUID id){
        return transaction(connection -> {
            return new UserDaoJdbc(connection).findById(id);
        }, CFG.userdataJdbcUrl());
    }

    public Optional<UserEntity> findByUsername(String username){
        return transaction(connection -> {
            return new UserDaoJdbc(connection).findByUsername(username);
        }, CFG.userdataJdbcUrl());
    }

    public void delete(UserEntity user){
        transaction(connection -> {
             new UserDaoJdbc(connection).delete(user);
             return null;
        }, CFG.userdataJdbcUrl());
    }

}
