package hu.ekke.receptmegoszto.controller;

import hu.ekke.receptmegoszto.domain.RecipeUser;
import hu.ekke.receptmegoszto.repository.UserDetailsRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        when(repository.findByUserName("david"))
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

        verify(repository, times(1)).findByUserName("david");
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(encoder);
    }

    @Test
    void register_ShouldSaveAndReturnSuccess_WhenNewUser() throws Exception {
        when(repository.findByUserName("ujuser")).thenReturn(Optional.empty());
        when(encoder.encode("titok")).thenReturn("ENCODED123");

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

        ArgumentCaptor<RecipeUser> captor = ArgumentCaptor.forClass(RecipeUser.class);
        verify(repository, times(1)).findByUserName("ujuser");
        verify(encoder, times(1)).encode("titok");
        verify(repository, times(1)).save(captor.capture());

        RecipeUser saved = captor.getValue();
        assertEquals("Új Felhasználó", saved.getName());
        assertEquals("ujuser", saved.getUserName());
        assertEquals("ENCODED123", saved.getPasswordEncoded());
        assertEquals("uj@vf.hu", saved.getEmail());

        verifyNoMoreInteractions(repository, encoder);
    }

    @Test
    void getCurrentUser_ShouldReturnUnauthorized_WhenPrincipalIsNull() throws Exception {
        mockMvc.perform(get("/user"))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(repository, encoder);
    }

    @Test
    void getCurrentUser_ShouldReturnUnauthorized_WhenPrincipalNameIsNull() throws Exception {
        Principal principal = () -> null;

        mockMvc.perform(get("/user").principal(principal))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(repository, encoder);
    }

    @Test
    void getCurrentUser_ShouldReturnNotFound_WhenUserNotInRepository() throws Exception {
        String username = "ghostuser";
        Principal principal = () -> username;

        when(repository.findByUserName(username)).thenReturn(Optional.empty());

        mockMvc.perform(get("/user").principal(principal))
                .andExpect(status().isNotFound());

        verify(repository, times(1)).findByUserName(username);
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(encoder);
    }

    @Test
    void getCurrentUser_ShouldReturnUserDto_WhenUserIsAuthenticated() throws Exception {
        String username = "testuser";
        RecipeUser user = new RecipeUser();
        user.setId(5);
        user.setName("Test User");
        user.setUserName(username);
        user.setEmail("user@example.com");
        user.setProfileImageRef("imgref");

        Principal principal = () -> username;

        when(repository.findByUserName(username)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/user").principal(principal))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(5L))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.userName").value(username))
                .andExpect(jsonPath("$.email").value("user@example.com"))
                .andExpect(jsonPath("$.profileImageRef").value("imgref"));

        verify(repository, times(1)).findByUserName(username);
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(encoder);
    }
}
