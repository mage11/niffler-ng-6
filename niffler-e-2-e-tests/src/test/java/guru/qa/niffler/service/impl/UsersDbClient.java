package guru.qa.niffler.service.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.jdbc.DataSources;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.repository.impl.AuthUserRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.UserdataUserRepositoryHibernate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.rest.CurrencyValues;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.service.UsersClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class UsersDbClient implements UsersClient {

    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final AuthUserRepository authUserRepository = new AuthUserRepositoryHibernate();
    private final UserdataUserRepository userdataUserRepository = new UserdataUserRepositoryHibernate();

    private final TransactionTemplate txTemplate = new TransactionTemplate(
        new ChainedTransactionManager(
            new JdbcTransactionManager(
                DataSources.dataSource(CFG.authJdbcUrl())
            ),
            new JdbcTransactionManager(
                DataSources.dataSource(CFG.userdataJdbcUrl())
            )
        )
    );

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
        CFG.authJdbcUrl(),
        CFG.userdataJdbcUrl()
    );

    public UserJson createUser(@Nonnull String username,@Nonnull String password) {
        return xaTransactionTemplate.execute(() -> {
                AuthUserEntity authUser = authUserEntity(username, password);
                authUserRepository.create(authUser);
                return UserJson.fromEntity(
                    userdataUserRepository.create(userEntity(username)),
                    null
                ).addTestData(new TestData(password));
            }
        );
    }

    @Override
    public List<UserJson> addIncomeInvitation(@Nonnull UserJson targetUser, int count) {
        List<UserJson> incomeUsers = new ArrayList<>();
        if (count > 0) {
            UserEntity targetEntity = userdataUserRepository.findById(
                targetUser.id()
            ).orElseThrow();

            for (int i = 0; i < count; i++) {
                xaTransactionTemplate.execute(() -> {
                        String username = RandomDataUtils.randomName();
                        AuthUserEntity authUser = authUserEntity(username, "12345");
                        authUserRepository.create(authUser);
                        UserEntity adressee = userdataUserRepository.create(userEntity(username));
                        userdataUserRepository.addIncomeInvitation(targetEntity, adressee);
                        incomeUsers.add(UserJson.fromEntity(adressee, null));
                        return null;
                    }
                );
            }
        }
        return incomeUsers;
    }

    @Override
    public List<UserJson> addOutcomeInvitation(@Nonnull UserJson targetUser, int count) {
        List<UserJson> outcomeUsers = new ArrayList<>();
        if (count > 0) {
            UserEntity targetEntity = userdataUserRepository.findById(
                targetUser.id()
            ).orElseThrow();

            for (int i = 0; i < count; i++) {
                xaTransactionTemplate.execute(() -> {
                        String username = RandomDataUtils.randomName();
                        AuthUserEntity authUser = authUserEntity(username, "12345");
                        authUserRepository.create(authUser);
                        UserEntity adressee = userdataUserRepository.create(userEntity(username));
                        userdataUserRepository.addOutcomeInvitation(targetEntity, adressee);
                        outcomeUsers.add(UserJson.fromEntity(adressee, null));
                        return null;
                    }
                );
            }
        }
        return outcomeUsers;
    }

    @Override
    public void addFriend(@Nonnull UserJson required,@Nonnull UserJson addressee) {
        xaTransactionTemplate.execute(() -> {
                userdataUserRepository.addFriend(UserEntity.fromJson(required), UserEntity.fromJson(addressee));
                return null;
            }
        );
    }

    @Override
    public List<UserJson> addFriend(@Nonnull UserJson targetUser, int count) {
        List<UserJson> friends = new ArrayList<>();
        if (count > 0) {
            UserEntity targetEntity = userdataUserRepository.findById(
                targetUser.id()
            ).orElseThrow();

            for (int i = 0; i < count; i++) {
                xaTransactionTemplate.execute(() -> {
                        String username = RandomDataUtils.randomName();
                        AuthUserEntity authUser = authUserEntity(username, "12345");
                        authUserRepository.create(authUser);
                        UserEntity addressee = userdataUserRepository.create(userEntity(username));
                        userdataUserRepository.addFriend(targetEntity, addressee);
                        friends.add(UserJson.fromEntity(addressee, null));
                        return null;
                    }
                );
            }
        }
        return friends;
    }

  private UserEntity userEntity(@Nonnull String username) {
    UserEntity ue = new UserEntity();
    ue.setUsername(username);
    ue.setCurrency(CurrencyValues.RUB);
    return ue;
  }

  private AuthUserEntity authUserEntity(@Nonnull String username,@Nonnull String password) {
    AuthUserEntity authUser = new AuthUserEntity();
    authUser.setUsername(username);
    authUser.setPassword(pe.encode(password));
    authUser.setEnabled(true);
    authUser.setAccountNonExpired(true);
    authUser.setAccountNonLocked(true);
    authUser.setCredentialsNonExpired(true);
    authUser.setAuthorities(
        Arrays.stream(Authority.values()).map(
            e -> {
              AuthorityEntity ae = new AuthorityEntity();
              ae.setUser(authUser);
              ae.setAuthority(e);
              return ae;
            }
        ).toList()
    );
    return authUser;
  }

  public void addFriendJdbc(UserJson requester, UserJson addressee){
      xaTransactionTemplate.execute(() ->{
          userdataUserRepository.addFriend(UserEntity.fromJson(requester), UserEntity.fromJson(addressee));
          return null;
      });
  }

  public  void addIncomeInvitationJdbc(UserJson requester, UserJson addressee){
      xaTransactionTemplate.execute(() ->{
          userdataUserRepository.addIncomeInvitation(UserEntity.fromJson(requester), UserEntity.fromJson(addressee));
          return null;
      });
  }

    public  void addOutcomeInvitationJdbc(UserJson requester, UserJson addressee){
        xaTransactionTemplate.execute(() ->{
            userdataUserRepository.addOutcomeInvitation(UserEntity.fromJson(requester), UserEntity.fromJson(addressee));
            return null;
        });
    }
}
