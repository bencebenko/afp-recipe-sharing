Feature: User Registration
  The user can register a new account.

  Scenario: Successfully registering a new user
    When I send a POST request to "/user/register" with user data
    Then the response status is 200
    And the response contains "Sikeres regisztráció!"

  Scenario: Registering with an existing username
    Given there is a user with username "testuser"
    When I send a POST request to "/user/register" with existing username
    Then the response status is 200
    And the response contains "Hiba: ilyen felhasználó már létezik!"

