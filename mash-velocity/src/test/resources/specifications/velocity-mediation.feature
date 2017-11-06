Feature: Configure and Execute Mediation - Jolt

  Background: 
    Given the following mediation configurations with properties:
      | inputType   | outputType    | className                                          | velocityTemplate            | inputValidationClassType                       |
      | Foo.class   | foo-v1.xml    | com.cpointeinc.mediation.velocity.VelocityMediator | templates/to-xml.vm         |                                                |
      | Foo.class   | foo-v1.json   | com.cpointeinc.mediation.velocity.VelocityMediator | templates/to-json.vm        |                                                |
      | simple.json | will-fail     | com.cpointeinc.mediation.velocity.VelocityMediator |                             |                                                |
      | simple.json | will-fail-too | com.cpointeinc.mediation.velocity.VelocityMediator | templates/does-not-exist.vm |                                                |
      | Foo.class   | foo-v2.json   | com.cpointeinc.mediation.velocity.VelocityMediator | templates/to-json.vm        | com.cpointeinc.mediation.velocity.TestProperty |
      | Bar.class   | foo-v3.json   | com.cpointeinc.mediation.velocity.VelocityMediator | templates/to-json.vm        | java.lang.Boolean                                               |

  Scenario: null input is provided to the mediator
    When mediation is configured for runtime
    And the mediator is invoked for "Foo.class" and "foo-v1.json" a null value
    Then the input is returned as null

  Scenario: Invalid velocity specification provided to the mediator
    When mediation is configured for runtime
    And the mediator is invoked for "simple.json" and "will-fail-too" with a property set of "foo" and "bar"
    Then a graceful exception case is returned from the velocity mediator

  Scenario: No velocity specification provided to the mediator
    When mediation is configured for runtime
    And the mediator is invoked for "simple.json" and "will-fail" with a property set of "foo" and "bar"
    Then a graceful exception case is returned from the velocity mediator

  Scenario: Simple velocity mediation performed to create XML
    When mediation is configured for runtime
    And the mediator is invoked for "Foo.class" and "foo-v1.xml" with a property set of "foo" and "bar"
    Then the input is transformed to a property xml element with a name of "foo" and a value of "bar"

  Scenario: Simple velocity mediation performed to create JSON
    When mediation is configured for runtime
    And the mediator is invoked for "Foo.class" and "foo-v1.json" with a property set of "foo" and "bar"
    Then the input is transformed to a json element with a name of "foo" and a value of "bar"

  Scenario: Valid class validation type provided
    When mediation is configured for runtime
    And the mediator is invoked for "Foo.class" and "foo-v2.json" with a property set of "foo" and "bar"
    Then the input is transformed to a json element with a name of "foo" and a value of "bar"

  Scenario: An invalid class validation type is provided
    When mediation is configured for runtime
    And the mediator is invoked for "Bar.class" and "foo-v3.json" with a property set of "foo" and "bar"
    Then a graceful exception case is returned from the velocity mediator
