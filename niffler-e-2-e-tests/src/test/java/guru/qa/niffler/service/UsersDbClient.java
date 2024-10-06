package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.UserDataRepository;
import guru.qa.niffler.data.repository.impl.AuthUserRepositoryJdbc;
import guru.qa.niffler.data.repository.impl.UserDataRepositoryJdbc;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.UserJson;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;


public class UsersDbClient {

  private static final Config CFG = Config.getInstance();
  private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  private final AuthUserRepository authUserRepository = new AuthUserRepositoryJdbc();
  //private final UdUserDao udUserDao = new UdUserDaoSpringJdbc();
    private final UserDataRepository udUserDao = new UserDataRepositoryJdbc();

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

  public UserJson createUser(UserJson user) {
    return xaTransactionTemplate.execute(() -> {
          AuthUserEntity authUser = new AuthUserEntity();
          authUser.setUsername(user.username());
          authUser.setPassword(pe.encode("12345"));
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
          authUserRepository.create(authUser);
          return UserJson.fromEntity(
              udUserDao.create(UserEntity.fromJson(user)),
              null
          );
        }
    );
  }

  public void addFriend(UserJson requester, UserJson addressee){
      xaTransactionTemplate.execute(() ->{
          udUserDao.addFriend(UserEntity.fromJson(requester), UserEntity.fromJson(addressee));
          return null;
      });
  }

  public  void addIncomeInvitation(UserJson requester, UserJson addressee){
      xaTransactionTemplate.execute(() ->{
          udUserDao.addIncomeInvitation(UserEntity.fromJson(requester), UserEntity.fromJson(addressee));
          return null;
      });
  }

    public  void addOutcomeInvitation(UserJson requester, UserJson addressee){
        xaTransactionTemplate.execute(() ->{
            udUserDao.addOutcomeInvitation(UserEntity.fromJson(requester), UserEntity.fromJson(addressee));
            return null;
        });
    }
}
