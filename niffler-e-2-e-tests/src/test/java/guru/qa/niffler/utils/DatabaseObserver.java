package guru.qa.niffler.utils;

import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.impl.UserdataUserRepositoryHibernate;

import java.util.Optional;

public class DatabaseObserver {

    private static final long timeout = 5 * 1000;
    private static final long interval = 1 * 1000;

    private static UserdataUserRepositoryHibernate userRepository = new UserdataUserRepositoryHibernate();

    public static UserEntity getUserFromDbByUsername(String username) {

        long startTime = System.currentTimeMillis();
        Optional<UserEntity> userOptional;

        while (System.currentTimeMillis() - startTime < timeout) {
            userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                return userOptional.get();
            }

            try {
                Thread.sleep(interval);
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

}
