package hu.ekke.receptmegoszto.controller;

import hu.ekke.receptmegoszto.domain.RecipeUser;
import hu.ekke.receptmegoszto.repository.UserDetailsRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import java.security.Principal;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDetailsRepository repository;

    @MockBean
    private PasswordEncoder encoder;

    @Test
    void register_ShouldReturnError_WhenUserAlreadyExists() throws Exception {
        Mockito.when(repository.findByUserName("david"))
                .thenReturn(Optional.of(new RecipeUser()));

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "name": "Dávid",
                              "userName": "david",
                              "password": "1234",
                              "email": "test@vf.hu"
                            }
                            """))
                .andExpect(status().isOk())
                .andExpect(content().string("Hiba: ilyen felhasználó már létezik!"));
    }

    @Test
    void register_ShouldSaveAndReturnSuccess_WhenNewUser() throws Exception {
        Mockito.when(repository.findByUserName("ujuser"))
                .thenReturn(Optional.empty());
        Mockito.when(encoder.encode("titok")).thenReturn("ENCODED123");

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "name": "Új Felhasználó",
                              "userName": "ujuser",
                              "password": "titok",
                              "email": "uj@vf.hu"
                            }
                            """))
                .andExpect(status().isOk())
                .andExpect(content().string("Sikeres regisztráció!"));

        Mockito.verify(repository).save(any(RecipeUser.class));
    }

    @Test
    void getCurrentUser_ShouldReturnUserDto_WhenUserIsAuthenticated() throws Exception {
        String username = "testuser";
        RecipeUser user = new RecipeUser();
        user.setId(5);
        user.setName("Test User");
        user.setUserName(username);
        user.setEmail("user@example.com");

        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn(username);

        Mockito.when(repository.findByUserName(username)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/user").principal(mockPrincipal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5L))
                .andExpect(jsonPath("$.userName").value(username))
                .andExpect(jsonPath("$.email").value("user@example.com"));
    }


}
