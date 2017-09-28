Feature: Configure and Execute Mediation - Jolt

  Background: 
    Given the following mediation configurations with properties:
      | inputType        | outputType       | className                                  | joltSpecification      |
      | foo-v1.json      | foo-v2.json      | com.cpointeinc.mediation.jolt.JoltMediator | not-applicable.json    |
      | foo-v3.json      | foo-v4.json      | com.cpointeinc.mediation.jolt.JoltMediator |                        |
      | jolt-simple.json | jolt-rating.json | com.cpointeinc.mediation.jolt.JoltMediator | example-jolt-spec.json |

  Scenario: null input is provided to the mediator
    When mediation is configured for runtime
    And the mediator is invoked for "foo-v1.json" and "foo-v2.json" a null value
    Then the input is returned as null

  Scenario: Invalid jolt specification provided to the mediator
    When mediation is configured for runtime
    And the mediator is invoked for "foo-v1.json" and "foo-v2.json" values ""
    Then a graceful exception case is returned

  Scenario: No jolt specification provided to the mediator
    When mediation is configured for runtime
    And the mediator is invoked for "foo-v3.json" and "foo-v4.json" values "does not matter"
    Then a graceful exception case is returned
    
  Scenario: Simple jolt mediation performed
    When mediation is configured for runtime
    And the mediator is invoked for "jolt-simple.json" and "jolt-rating.json" without a range value
    Then the input is transformed to include two default range values
