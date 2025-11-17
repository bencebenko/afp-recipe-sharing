Feature: Recipe Management
  The user can manage recipes.

  Scenario: Successfully retrieving an existing recipe
    Given I am logged in with username "gyuri" and password "titok"
    Given there is a recipe in the database with title "Gulyasleves", servings "6 portions", duration "90 min"
    When I send a GET request to "/recipe/1"
    Then the response status is 200
    And the response JSON contains "Gulyasleves" with "6 portions" and duration "90 min"
    And the response JSON contains non-empty arrays "ingredients" and "steps"

  Scenario: Successfully retrieving all recipes
    Given I am logged in with username "gyuri" and password "titok"
    Given there is a recipe in the database with title "Gulyasleves", servings "6 portions", duration "90 min"
    When I send a GET request to "/recipe"
    Then the response status is 200
    And the response JSON is an array with at least 1 recipe

  Scenario: Successfully saving a new recipe
    Given I am logged in with username "gyuri" and password "titok"
    Given categories and materials exist in the database
    When I send a POST request to "/recipe" with recipe data
    Then the response status is 200
    And the saved recipe has a generated ID

  Scenario: Successfully deleting a recipe
    Given I am logged in with username "gyuri" and password "titok"
    Given there is a recipe in the database with title "Gulyasleves", servings "6 portions", duration "90 min"
    When I send a DELETE request to "/recipe/1"
    Then the response status is 200
