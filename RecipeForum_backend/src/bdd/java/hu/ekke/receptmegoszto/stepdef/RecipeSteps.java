package hu.ekke.receptmegoszto.stepdef;

import hu.ekke.receptmegoszto.domain.*;
import hu.ekke.receptmegoszto.repository.*;
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

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RecipeSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserDetailsRepository userRepo;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private RequestPostProcessor auth;
    private ResultActions resultActions;
    private RecipeUser testUser;

    public ResultActions getResultActions() {
        return resultActions;
    }

    @Before
    public void setupUser() {
        if (userRepo.findByUserName("gyuri").isEmpty()) {
            RecipeUser user = new RecipeUser();
            user.setUserName("gyuri");
            user.setEmail("gyuri@test.com");
            user.setPasswordEncoded(passwordEncoder.encode("titok"));
            testUser = userRepo.save(user);
        } else {
            testUser = userRepo.findByUserName("gyuri").get();
        }
    }

    @Given("I am logged in with username {string} and password {string}")
    public void iAmLoggedIn(String user, String pass) {
        this.auth = httpBasic(user, pass);
    }

    @Given("there is a recipe in the database with title {string}, servings {string}, duration {string}")
    public void thereIsARecipeInTheDatabase(String title, String servings, String duration) {
        Category category = new Category();
        category.setName("Hungarian");
        category = categoryRepository.save(category);

        Material beef = new Material();
        beef.setName("Beef");
        beef.setUnitOfMeasure("g");
        beef = materialRepository.save(beef);

        Material onion = new Material();
        onion.setName("Onion");
        onion.setUnitOfMeasure("pcs");
        onion = materialRepository.save(onion);

        Material paprika = new Material();
        paprika.setName("Paprika");
        paprika.setUnitOfMeasure("tbsp");
        paprika = materialRepository.save(paprika);

        Recipe recipe = new Recipe();
        recipe.setName(title);
        recipe.setPortion(Integer.parseInt(servings.split(" ")[0]));
        recipe.setPrepTime(15);
        recipe.setCookTime(Integer.parseInt(duration.split(" ")[0]));
        recipe.setDescription("Traditional Hungarian goulash soup");
        recipe.setPreparationSteps("Chop onions\nFry onions\nAdd beef and cook 1 hour");
        recipe.setUser(testUser);
        recipe.setCategory(category);

        recipe = recipeRepository.save(recipe);

        List<Ingredient> ingredients = new ArrayList<>();

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setRecipe(recipe);
        ingredient1.setMaterial(beef);
        ingredient1.setQuantity("500");
        ingredients.add(ingredient1);

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setRecipe(recipe);
        ingredient2.setMaterial(onion);
        ingredient2.setQuantity("2");
        ingredients.add(ingredient2);

        Ingredient ingredient3 = new Ingredient();
        ingredient3.setRecipe(recipe);
        ingredient3.setMaterial(paprika);
        ingredient3.setQuantity("2");
        ingredients.add(ingredient3);

        recipe.setIngredients(ingredients);
        recipeRepository.save(recipe);
    }

    @When("I send a GET request to {string}")
    public void iSendAGetRequestTo(String url) throws Exception {
        resultActions = mockMvc.perform(get(url).with(auth).accept(MediaType.APPLICATION_JSON));
    }

    @When("I send a POST request to {string} with recipe data")
    public void iSendAPostRequestToWithRecipeData(String endpoint) throws Exception {
        String recipeJson = """
                {
                    "name": "Pörkölt",
                    "prepTime": 15,
                    "cookTime": 60,
                    "portion": 4,
                    "description": "Hungarian stew",
                    "preparationSteps": "Cut meat\\nFry onions\\nAdd paprika\\nCook until tender",
                    "categoryId": 1,
                    "ingredients": [
                        {
                            "materialId": 1,
                            "quantity": "800"
                        },
                        {
                            "materialId": 2,
                            "quantity": "3"
                        }
                    ]
                }
                """;

        resultActions = mockMvc.perform(post(endpoint)
                .with(httpBasic("gyuri", "titok"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(recipeJson));
    }

    @When("I send a DELETE request to {string}")
    public void iSendADeleteRequestTo(String endpoint) throws Exception {
        resultActions = mockMvc.perform(delete(endpoint)
                .with(httpBasic("gyuri", "titok")));
    }

    @Then("the response JSON contains {string} with {string} and duration {string}")
    public void theResponseJsonContains(String title, String portions, String duration) throws Exception {
        int portionNumber = Integer.parseInt(portions.split(" ")[0]);
        int cookTimeNumber = Integer.parseInt(duration.split(" ")[0]);

        resultActions.andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(title)))
                .andExpect(jsonPath("$.portion", is(portionNumber)))
                .andExpect(jsonPath("$.cookTime", is(cookTimeNumber)))
                .andDo(MockMvcResultHandlers.print());
    }

    @Then("the response JSON contains non-empty arrays {string} and {string}")
    public void theResponseJsonContainsArrays(String _arr1, String _arr2) throws Exception {
        resultActions
                .andExpect(jsonPath("$.ingredients").isArray())
                .andExpect(jsonPath("$.ingredients.[0]").exists())
                .andExpect(jsonPath("$.preparationSteps").isString())
                .andExpect(jsonPath("$.preparationSteps").isNotEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Then("the response JSON is an array with at least {int} recipe")
    public void theResponseJsonIsAnArrayWithAtLeastRecipe(int minCount) throws Exception {
        resultActions
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(greaterThanOrEqualTo(minCount)));
    }

    @Then("the saved recipe has a generated ID")
    public void theSavedRecipeHasAGeneratedId() throws Exception {
        resultActions
                .andExpect(jsonPath("$.id", notNullValue()));
    }
}

