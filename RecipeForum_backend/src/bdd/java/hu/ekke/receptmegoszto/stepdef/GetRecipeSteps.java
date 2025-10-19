package hu.ekke.receptmegoszto.stepdef;

import hu.ekke.receptmegoszto.domain.RecipeUser;
import hu.ekke.receptmegoszto.domain.Recipe;
import hu.ekke.receptmegoszto.repository.RecipeRepository;
import hu.ekke.receptmegoszto.repository.UserDetailsRepository;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class GetRecipeSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserDetailsRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private RequestPostProcessor auth;
    private ResultActions resultActions;

    @Before
    public void setupUser() {
        RecipeUser user = new RecipeUser();
        user.setUserName("gyuri");
        user.setPassword(passwordEncoder.encode("titok"));
        user.setRoles("ROLE_USER");
        userRepo.save(user);
    }

    @Given("I am logged in with username {string} and password {string}")
    public void iAmLoggedIn(String user, String pass) {
        this.auth = httpBasic(user, pass);
    }

    @Given("there is a recipe in the database with title {string}, servings {string}, duration {string}")
    public void thereIsARecipeInTheDatabase(String title, String servings, String duration) {
        Recipe recipe = new Recipe();
        recipe.setTitle(title);
        recipe.setServings(servings);
        recipe.setDuration(duration);
        recipe.setIngredients(List.of("500 g beef", "2 onions", "2 tbsp paprika"));
        recipe.setSteps(List.of("Chop onions", "Fry onions", "Add beef and cook 1 hour"));
        recipeRepository.save(recipe);
    }

    @When("I send a GET request to {string}")
    public void iSendAGetRequestTo(String url) throws Exception {
        resultActions = mockMvc.perform(get(url).with(auth).accept(MediaType.APPLICATION_JSON));
    }

    @Then("the response status is {int}")
    public void theResponseStatusIs(int status) throws Exception {
        resultActions.andExpect(status().is(status));
    }

    @Then("the response JSON contains {string} with {string} and duration {string}")
    public void theResponseJsonContains(String title, String portions, String duration) throws Exception {
        resultActions.andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is(title)))
                .andExpect(jsonPath("$.servings", is(portions)))
                .andExpect(jsonPath("$.duration", is(duration)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Then("the response JSON contains non-empty arrays {string} and {string}")
    public void theResponseJsonContainsArrays(String arr1, String arr2) throws Exception {
        resultActions
                .andExpect(jsonPath("$." + arr1).isArray())
                .andExpect(jsonPath("$." + arr1 + ".[0]").exists())
                .andExpect(jsonPath("$." + arr2).isArray())
                .andExpect(jsonPath("$." + arr2 + ".[0]").exists())
                .andDo(MockMvcResultHandlers.print());
    }
}
