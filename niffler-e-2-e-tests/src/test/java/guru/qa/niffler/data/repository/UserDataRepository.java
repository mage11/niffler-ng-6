package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.userdata.UserEntity;

public interface UserDataRepository {
    UserEntity create(UserEntity user);
    void addFriend(UserEntity requester, UserEntity addressee);
    void addIncomeInvitation(UserEntity requester, UserEntity addressee);
    void addOutcomeInvitation(UserEntity requester, UserEntity addressee);

}
