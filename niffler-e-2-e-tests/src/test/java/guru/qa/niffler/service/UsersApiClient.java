package guru.qa.niffler.service;

import guru.qa.niffler.model.UserJson;

public class UsersApiClient implements UsersClient{

    @Override
    public UserJson createUser(String username, String password) {
        return null;
    }

    @Override
    public void addIncomeInvitation(UserJson targetUser, int count) {

    }

    @Override
    public void addOutcomeInvitation(UserJson targetUser, int count) {

    }

    @Override
    public void addFriend(UserJson required, UserJson addressee) {

    }

    @Override
    public void addFriend(UserJson targetUser, int count) {

    }
}
