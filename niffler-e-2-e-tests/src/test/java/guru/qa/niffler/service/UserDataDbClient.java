package guru.qa.niffler.service;

import guru.qa.niffler.data.dao.impl.UserDaoJdbc;
import guru.qa.niffler.data.entity.user.UserEntity;

import java.util.Optional;
import java.util.UUID;

public class UserDataDbClient {
    private final UserDaoJdbc userDaoJdbc = new UserDaoJdbc();
    public UserEntity createUser(UserEntity user){
        return userDaoJdbc.createUser(user);
    }

    public Optional<UserEntity> findById(UUID id){
        return userDaoJdbc.findById(id);
    }

    public Optional<UserEntity> findByUsername(String username){
        return userDaoJdbc.findByUsername(username);
    }

    public void delete(UserEntity user){
        userDaoJdbc.delete(user);
    }

}
