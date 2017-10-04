Feature: Configure and Execute Mediation

  Background: 
    Given the following mediation configurations:
      | inputType         | outputType         | className                                            |
      | json              | xml                | com.cpointeinc.mediation.example.StaticXmlMediator   |
      | json              | json               | com.cpointeinc.mediation.LoggingMediator             |
      | ucore-v2          | ucore-v3           | com.cpointeinc.mediation.LoggingMediator             |
      | ddms-v1           | ddms-v2            | com.cpointeinc.mediation.DoesNotExistMediator        |
      | mixed-case-string | lower-case-string  | com.cpointeinc.mediation.example.LowercaseMediator   |
      | anything          | never-gonna-happen | com.cpointeinc.mediation.example.ExceptionalMediator |

  Scenario Outline: Load multiple mediation routines
    When mediation is configured for runtime
    Then a valid mediation routine is available for the intersection of "<inputType>" and "<outputType>"

    Examples: 
      | inputType | outputType |
      | json      | xml        |
      | json      | json       |
      | ucore-v2  | ucore-v3   |

  Scenario: Non-existent class defined
    When mediation is configured for runtime
    Then a valid mediation routine is NOT available for the intersection of "ddms-v1" and "ddms-v2"

  Scenario: Non-existent mediation routine requested
    When mediation is configured for runtime
    Then a valid mediation routine is NOT available for the intersection of "foo" and "????"

  Scenario Outline: Validate mediation execution
    When mediation is configured for runtime
    And the mediator is invoked for "<inputType>" and "<outputType>" values "<inputValue>"
    Then the input is transformed to "<outputValue>"

    Examples: 
      | inputType         | outputType        | inputValue                        | outputValue                                   |
      | json              | xml               | Does not matter for this mediator | <staticXml>this will never change</staticXml> |
      | json              | json              | log this                          | log this                                      |
      | ucore-v2          | ucore-v3          | DO NOT CHANGE ME                  | DO NOT CHANGE ME                              |
      | mixed-case-string | lower-case-string | I LoVe mIxEd CAse                 | i love mixed case                             |
      | no match          | reflective        | pass this through                 | pass this through                             |

  Scenario: Exception Handling
    When mediation is configured for runtime
    And the mediator is invoked for "anything" and "never-gonna-happen" values "input theater"
    Then a graceful exception case is returned

  Scenario: Configure mediation routine with properties
    Given a mediator with "string", "string", and "com.cpointeinc.mediation.example.PropertyAwareMediator"
    And the following properties:
      | key       | value |
      | propertyA | foo   |
      | propertyB | bar   |
    When mediation is configured for runtime
    And the mediator is invoked for "string" and "string" values "SOMETHING"
    Then the input is transformed to "foo-SOMETHING-bar"
