@web
Feature: Web Demo
  In order to ...
  As a ...
  I want to ...

  @wip
  Scenario Outline: Sample scenario outline 1
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

  @regression
  Scenario Outline: Sample scenario outline 2
    Given I have details of a 'new' user
    When I fill in the form with user details
    Then I fill in the form with user details <"column_4">
    And I click on the Clear button

    Examples:

      |column_1|column_2|column_3|column_4|
      |Val 99  |Val 98  |Val 97  |Val 96  |
      |Val 95  |Val 94  |Val 93  |Val 92  |
      |Val 91  |Val 90  |Val 89  |Val 88  |