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

      |column_1|column_2|column_3|column_4|
      |Val A   |Val B   |Val C   |Val D   |
      |Val W   |Val X   |Val Y   |Val Z   |
      |Val 1   |Val 2   |Val 3   |Val 4   |