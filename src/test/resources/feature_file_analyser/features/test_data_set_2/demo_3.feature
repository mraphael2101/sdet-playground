@web
Feature: Web Demo
  In order to ...
  As a ...
  I want to ...

  Background:
    Given I have launched an 'Edge' browser

  @wip
  Scenario: Sample scenario
    When I fill in the form with user details
    Then I fill in the form with user details <"column_4">
    And I click on the Clear button

  @wip
  Scenario Outline: Sample scenario 2
    Given I have details of a 'new' user
    And my application opens
    And I have launched a 'Chrome' browser
    When I fill in the form with user details
    Then I fill in the form with user details <"column_4">
    And I click on the Clear button

    Examples:

      |column_1|column_2|column_3|column_4|
      |Val L   |Val M   |Val N   |Val O   |
      |Val P   |Val Q   |Val R   |Val S   |