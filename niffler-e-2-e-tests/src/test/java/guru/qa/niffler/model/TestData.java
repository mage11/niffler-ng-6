package guru.qa.niffler.model;

import guru.qa.niffler.model.rest.CategoryJson;
import guru.qa.niffler.model.rest.SpendJson;
import guru.qa.niffler.model.rest.UserJson;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public record TestData(
    String password,
    List<CategoryJson> categories,
    List<SpendJson> spendings,
    List<UserJson> incomeInvitations,
    List<UserJson> outcomeInvitations,
    List<UserJson> friends) {

    public TestData(@Nonnull String password) {
        this(password, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    public @Nonnull String[] friendsUsernames() {
        return extractUsernames(friends);
    }

    public @Nonnull String[] incomeInvitationsUsernames() {
        return extractUsernames(incomeInvitations);
    }

    public @Nonnull String[] outcomeInvitationsUsernames() {
        return extractUsernames(outcomeInvitations);
    }

    public @Nonnull String[] categoryDescriptions() {
        return categories.stream().map(CategoryJson::name).toArray(String[]::new);
    }

    private @Nonnull String[] extractUsernames(List<UserJson> users) {
        return users.stream().map(UserJson::username).toArray(String[]::new);
    }
}
