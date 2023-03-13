@web
Feature: Web Demo
  In order to ...
  As a ...
  I want to ...

  Background:
    Given I have details of a 'new' user
    And my application opens

  @wip
  Scenario: Registration scenario
    When I fill in the registration section with my personal details
    And I provide my username and memorable keyword
      | username       | keyword       |
      | abc01          | Erik Larson   |
      | def02          | Charles Lewis |
      | ghi03          | Peter Thomas  |

  @wip
  Scenario Outline: Basket scenario
    When I fill in the Basket textfield with details <"column_4">

    Examples:

|column_1|column_2|column_3|column_4|
|Column 1| Column 2| Column 3| Column 4|
