package guru.qa.niffler.jupiter.extension;

import io.qameta.allure.Allure;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class UsersQueueExtension implements
    BeforeTestExecutionCallback,
    AfterTestExecutionCallback,
    ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UsersQueueExtension.class);

    public record StaticUser(String username,
                             String password,
                             String friend,
                             String income,
                             String outcome) {

    }

    private static final Queue<StaticUser> EMPTY_QUEUE = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_FRIENDS_QUEUE = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_INCOME_REQUEST_QUEUE = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_OUTCOME_REQUEST_QUEUE = new ConcurrentLinkedQueue<>();


    static {
        EMPTY_QUEUE.add(new StaticUser("empty_user", "123", null, null, null));
        WITH_FRIENDS_QUEUE.add(new StaticUser("with_friend_user", "123", "friend", null, null));
        WITH_INCOME_REQUEST_QUEUE.add(new StaticUser("with_income_user", "123", null, "with_outcome_user", null));
        WITH_OUTCOME_REQUEST_QUEUE.add(new StaticUser("with_outcome_user", "123", null, null, "with_income_user"));
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface UserType {

        Type value() default Type.EMPTY;

        enum Type {
            EMPTY, WITH_FRIENDS, WITH_INCOME_REQUEST, WITH_OUTCOME_REQUEST
        }
    }

    public Queue<StaticUser> getQueueByUserType(UserType.Type type) {
        switch (type) {
            case EMPTY:
                return EMPTY_QUEUE;
            case WITH_FRIENDS:
                return WITH_FRIENDS_QUEUE;
            case WITH_INCOME_REQUEST:
                return WITH_INCOME_REQUEST_QUEUE;
            case WITH_OUTCOME_REQUEST:
                return WITH_OUTCOME_REQUEST_QUEUE;
            default:
                return EMPTY_QUEUE;
        }

    }

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        Map<UserType, StaticUser> userMap = new HashMap<>();
        Arrays.stream(context.getRequiredTestMethod().getParameters())
            .filter(p -> AnnotationSupport.isAnnotated(p, UserType.class))
            .forEach(p -> {
                UserType userType = p.getAnnotation(UserType.class);
                Optional<StaticUser> user = Optional.empty();
                Queue<StaticUser> queue = getQueueByUserType(userType.value());
                StopWatch sw = StopWatch.createStarted();

                while (user.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {
                    user = Optional.ofNullable(queue.poll());
                }
                Allure.getLifecycle().updateTestCase(testCase ->
                    testCase.setStart(new Date().getTime()));

                user.ifPresentOrElse(
                    u -> userMap.put(userType, u),
                    () -> {
                        throw new IllegalStateException("Can't obtain user after 30s");
                    }
                );
            });
        getUserMap(context).putAll(userMap);
    }

    private Map<UserType, StaticUser> getUserMap(ExtensionContext context) {
        return (Map<UserType, StaticUser>) context.getStore(NAMESPACE)
            .getOrComputeIfAbsent(context.getUniqueId(), key -> new HashMap<>());
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        Map<UserType, StaticUser> map = getUserMap(context);
        for (Map.Entry<UserType, StaticUser> entry : map.entrySet()) {
            Queue<StaticUser> queue = getQueueByUserType(entry.getKey().value());
            queue.add(entry.getValue());
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class)
            && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
    }

    @Override
    public StaticUser resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Map<UserType, StaticUser> map = getUserMap(extensionContext);
        Optional<UserType> userType = parameterContext.findAnnotation(UserType.class);
        StaticUser staticUser = map.get(userType.get());
        return staticUser;
    }
}
