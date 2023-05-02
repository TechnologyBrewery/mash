@excelToJsonObjectMediation
Feature: Mediation -> Mediate XLSX to JSON

  Background: 
    Given the following mediation configurations:
      | inputType | outputType | className                                     |
      | excel     | json       | org.technologybrewery.mash.XlsxToJsonMediator |

  Scenario: Validate excel to json mediation for GenericObject
    Given an Excel spreadsheet with the full filename "target/excelTestFiles/genericObject.xlsx" and a header of "org.technologybrewery.mash.objects.GenericObject"
    And headers for fields "name" and "id"
    And the following data in a table
      | name    | id |
      | object1 |  1 |
      | object2 |    |
      | object3 |  3 |
      |         |  4 |
    When mediation is configured for runtime
    And the mediator is invoked for input "excel" and output "json" values "target/excelTestFiles/genericObject.xlsx"
    Then the following json is returned
      | [{"name":"object1","id":"1"},{"name":"object2","id":""},{"name":"object3","id":"3"},{"name":"","id":"4"}] |
