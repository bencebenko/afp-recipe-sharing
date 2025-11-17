package hu.ekke.receptmegoszto.stepdef;

import hu.ekke.receptmegoszto.domain.RecipeUser;
import hu.ekke.receptmegoszto.repository.UserDetailsRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserRegistrationSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserDetailsRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private ResultActions resultActions;

    public ResultActions getResultActions() {
        return resultActions;
    }

    @Given("there is a user with username {string}")
    public void thereIsAUserWithUsername(String username) {
        if (userRepo.findByUserName(username).isEmpty()) {
            RecipeUser user = new RecipeUser();
            user.setName("Test User");
            user.setUserName(username);
            user.setEmail("test@example.com");
            user.setPasswordEncoded(passwordEncoder.encode("password123"));
            userRepo.save(user);
        }
    }

    @When("I send a POST request to {string} with user data")
    public void iSendAPostRequestToWithUserData(String endpoint) throws Exception {
        String userJson = """
                {
                    "name": "New User",
                    "userName": "newuser",
                    "email": "newuser@example.com",
                    "password": "password123"
                }
                """;

        resultActions = mockMvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson));
    }

    @When("I send a POST request to {string} with existing username")
    public void iSendAPostRequestToWithExistingUsername(String endpoint) throws Exception {
        String userJson = """
                {
                    "name": "Test User",
                    "userName": "testuser",
                    "email": "another@example.com",
                    "password": "password123"
                }
                """;

        resultActions = mockMvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson));
    }
}

