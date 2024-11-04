package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.UserApiClient;
import guru.qa.niffler.jupiter.annotation.meta.User;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UsersClient;
import guru.qa.niffler.service.impl.UsersDbClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.ArrayList;
import java.util.List;

public class UserExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserExtension.class);
    private static final String defaultPassword = "12345";

    private final UsersClient usersClient = new UserApiClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
            .ifPresent(userAnno -> {
                UserJson testUser;
                if ("".equals(userAnno.username())) {
                    String username = RandomDataUtils.randomUserName();
                    testUser = usersClient.createUser(username, defaultPassword);
                } else {
                    testUser = usersClient.createUser(userAnno.username(), defaultPassword);
                }

                List<UserJson> incomeUsers = new ArrayList<>();
                List<UserJson> outcomeUsers= new ArrayList<>();
                List<UserJson> friends= new ArrayList<>();
                if (userAnno.incomeFriend()){
                    incomeUsers = usersClient.addIncomeInvitation(testUser, 1);
                }

                if (userAnno.outcomeFriend()){
                    outcomeUsers = usersClient.addOutcomeInvitation(testUser, 1);
                }

                if (userAnno.friends()){
                    friends = usersClient.addOutcomeInvitation(testUser, 1);
                }

                context.getStore(NAMESPACE).put(
                    context.getUniqueId(),
                    testUser.addTestData(
                        new TestData(
                            defaultPassword,
                            new ArrayList<>(),
                            new ArrayList<>(),
                            incomeUsers,
                            outcomeUsers,
                            friends
                        )
                    )
                );

            });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class);
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), UserJson.class);
    }
}
