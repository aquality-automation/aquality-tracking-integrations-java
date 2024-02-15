@FeatureTag
Feature: Test feature

  @Demo
  Scenario: Test passes
    Given I pass this scenario

  @Smoke @Priority1
  Scenario Outline: Test with the same names
    Given I pass this scenario with <example value> parameter

    Examples:
      | example value |
      | value1        |

  @Priority1
  Scenario Outline: Test with the same names
    Given I pass this scenario with <example value> parameter

    @Demo
    Examples:
      | example value |
      | value1        |
      | value2        |

    Examples:
      | example value |
      | value3        |
      | value4        |

  @Rule1
  Rule: Rule 1

  Example: Rule 1 example
    Given I pass this scenario with 'Rule 1 value' parameter

  @Rule2 @Demo
  Rule: Rule 2

  Example: Rule 2 example
    Given I pass this scenario with 'Rule 2 value' parameter
