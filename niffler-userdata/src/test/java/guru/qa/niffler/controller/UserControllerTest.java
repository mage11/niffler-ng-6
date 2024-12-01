package guru.qa.niffler.controller;

import guru.qa.niffler.data.CurrencyValues;
import guru.qa.niffler.data.UserEntity;
import guru.qa.niffler.data.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository usersRepository;

  @Test
  void currentUserEndpoint() throws Exception {
    UserEntity userDataEntity = new UserEntity();
    userDataEntity.setUsername("dima");
    userDataEntity.setCurrency(CurrencyValues.RUB);
    usersRepository.save(userDataEntity);

    mockMvc.perform(get("/internal/users/current")
            .contentType(MediaType.APPLICATION_JSON)
            .param("username", "dima")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("dima"));
  }

  @Test
  void allUsersEndpoint() throws Exception {
    UserEntity user1 = new UserEntity();
    UserEntity user2 = new UserEntity();
    user1.setUsername("userName1");
    user1.setCurrency(CurrencyValues.RUB);
    user2.setUsername("userName2");
    user2.setCurrency(CurrencyValues.EUR);

    usersRepository.save(user1);
    usersRepository.save(user2);

    mockMvc.perform(get("/internal/users/all")
            .contentType(MediaType.APPLICATION_JSON)
            .param("username", "userName1")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].username").value("userName2"));
  }

  @Test
  void updateEndpoint() throws Exception {
    UserEntity userDataEntity = new UserEntity();
    userDataEntity.setUsername("userNameRng");
    userDataEntity.setCurrency(CurrencyValues.RUB);
    usersRepository.save(userDataEntity);

    mockMvc.perform(post("/internal/users/update")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\":\"userNameRng\", \"currency\":\"EUR\"}")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.currency").value("EUR"));
  }
}
