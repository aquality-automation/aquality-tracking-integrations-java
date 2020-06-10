Feature: Test feature

  @Demo
  Scenario: Test passes
    Given I pass this scenario

  @Smoke @Priority1
  Scenario Outline: Test with the same names
    Given I pass this scenario with parameter <example value>

    Examples:
      | example value |
      | value1        |

  @Demo @Priority1
  Scenario Outline: Test with the same names
    Given I pass this scenario with parameter <example value>

    Examples:
      | example value |
      | value1        |
