@excelToTransferObjectMediation
Feature: Mediation -> Mediate XLSX to Transfer Object

  Background: 
    Given the following mediation configurations:
      | inputType | outputType      | className                                               |
      | excel     | transfer-object | org.bitbucket.cpointe.mash.XlsxToTransferObjectMediator |

  Scenario: Validate excel to transfer object mediation for GenericObject
    Given an Excel spreadsheet with the full filename "target/excelTestFiles/genericObject.xlsx" and a header of "org.bitbucket.cpointe.mash.objects.GenericObject"
    And headers for fields "name" and "id"
    And the following data in a table
      | name    | id |
      | object1 |  1 |
      | object2 |    |
      | object3 |  3 |
      |         |  4 |
    When mediation is configured for runtime
    And the mediator is invoked for input "excel" and output "transfer-object" values "target/excelTestFiles/genericObject.xlsx"
    Then the following GenericObject objects are returned
      | name    | id |
      | object1 |  1 |
      | object2 |    |
      | object3 |  3 |
      |         |  4 |
