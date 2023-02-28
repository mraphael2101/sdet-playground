@web
Feature: Web Demo
  In order to ...
  As a ...
  I want to ...

  @wip
  Scenario Outline: Sample scenario
    Given I have details of a 'new' user
    And my application opens
    And I have launched a 'Chrome' browser
    When I fill in the form with user details
    Then I fill in the form with user details <"column_4">
    And I click on the Clear button

    Examples:

