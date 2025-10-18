Feature: Get a recipe
  The user can retrieve a recipe by its ID.

  Scenario: Successfully retrieving an existing recipe
    Given I am logged in with username "gyuri" and password "titok"
    Given there is a recipe in the database with title "Gulyasleves", servings "6 portions", duration "90 min"
    When I send a GET request to "/recipe/1"
    Then the response status is 200
    And the response JSON contains "Gulyasleves" with "6 portions" and duration "90 min"
    And the response JSON contains non-empty arrays "ingredients" and "steps"