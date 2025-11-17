package hu.ekke.receptmegoszto.stepdef;

import io.cucumber.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CommonSteps {

    @Autowired
    private RecipeSteps recipeSteps;

    @Autowired
    private UserRegistrationSteps userRegistrationSteps;

    @Then("the response status is {int}")
    public void theResponseStatusIs(int statusCode) throws Exception {
        ResultActions resultActions = getResultActions();
        if (resultActions != null) {
            resultActions.andExpect(status().is(statusCode));
        }
    }

    @Then("the response contains {string}")
    public void theResponseContains(String expectedText) throws Exception {
        ResultActions resultActions = getResultActions();
        if (resultActions != null) {
            resultActions.andExpect(content().string(containsString(expectedText)));
        }
    }

    private ResultActions getResultActions() {
        if (recipeSteps.getResultActions() != null) {
            return recipeSteps.getResultActions();
        }
        if (userRegistrationSteps.getResultActions() != null) {
            return userRegistrationSteps.getResultActions();
        }
        return null;
    }
}

