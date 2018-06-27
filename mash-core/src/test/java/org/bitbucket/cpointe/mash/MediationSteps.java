package org.bitbucket.cpointe.mash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bitbucket.cpointe.mash.objects.GenericObject;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class MediationSteps extends AbstractMediationSteps {

    private MediationConfiguration newConfiguration;
    private static String fullFilePath = null;
    private static final int numberOfExcelHeaderRows = 2;

    @Given("^the following mediation configurations:$")
    public void the_following_mediation_configurations(List<MediationConfiguration> configs) throws Throwable {
        assertNotNull("No mediation configurations found!", configs);
        writeMediationConfiguration(configs, "cucumber-definitions.json");

        outputValue = null;
        encounteredException = null;

    }

    @Given("^a mediator with \"([^\"]*)\", \"([^\"]*)\", and \"([^\"]*)\"$")
    public void a_mediator_with_and(String inputType, String outputType, String className) throws Throwable {
        newConfiguration = new MediationConfiguration();
        newConfiguration.setInputType(inputType);
        newConfiguration.setOutputType(outputType);
        newConfiguration.setClassName(className);
    }

    @Given("^the following properties:$")
    public void the_following_properties(List<MediationProperty> properties) throws Throwable {
        newConfiguration.setProperties(properties);
        List<MediationConfiguration> propertyAwareConfigurations = new ArrayList<>();
        propertyAwareConfigurations.add(newConfiguration);
        writeMediationConfiguration(propertyAwareConfigurations, "cucumber-definitions-with-properties.json");
    }

    @When("^mediation is configured for runtime$")
    public void mediation_is_configured_for_runtime() throws Throwable {
        MediationManager.resetMediationManager();
        instance = MediationManager.getInstance();
    }

    @Then("^a valid mediation routine is available for the intersection of \"([^\"]*)\" and \"([^\"]*)\"$")
    public void a_valid_mediation_routine_is_available_for_the_intersection_of_and(String inputType, String outputType)
            throws Throwable {
        findMediator(inputType, outputType);
    }

    @Then("^a valid mediation routine is NOT available for the intersection of \"([^\"]*)\" and \"([^\"]*)\"$")
    public void a_valid_mediation_routine_is_NOT_available_for_the_intersection_of_and(String inputType,
            String outputType) throws Throwable {
        MediationContext context = new MediationContext(inputType, outputType);
        Mediator mediator = instance.getMediator(context);
        assertEquals("The default pass through mediator should have been returned! ", mediator.getClass(),
                PassThroughMediator.class);
    }

    @When("^the mediator is invoked for input \"([^\"]*)\" and output \"([^\"]*)\" values \"([^\"]*)\"$")
    public void the_mediator_is_invoked_for_and_values(String inputType, String outputType, String inputValue)
            throws Throwable {
        invokeMediator(inputType, outputType, inputValue);
    }

    @When("^the mediator is invoked for input \"([^\"]*)\" and output \"([^\"]*)\" a null value$")
    public void the_mediator_is_invoked_for_and_a_null_value(String inputType, String outputType) throws Throwable {
        invokeMediator(inputType, outputType, null);
    }

    @Then("^the input is transformed to \"([^\"]*)\"$")
    public void the_input_is_transformed_to(String expectedValue) throws Throwable {
        if (encounteredException != null) {
            throw encounteredException;
        }
        assertEquals("Output value was not what was anticipated!", expectedValue, outputValue);
        assertNull("Should not have encountered a MediationException!", encounteredException);
    }

    @Then("^a graceful exception case is returned$")
    public void a_graceful_exception_case_is_returned() throws Throwable {
        assertNull("Should not have returned an output!", outputValue);
        assertNotNull("Should have encountered MediationException!", encounteredException);
    }

    @Then("^the input is returned as null$")
    public void the_input_is_returned_as_null() throws Throwable {
        assertNull("Null output was expected!", outputValue);
    }

    /* ----- Additions for excel transformations ----- */

    @Given("^an Excel spreadsheet with the full filename \"([^\"]*)\" and a header of \"([^\"]*)\"$")
    public void an_Excel_spreadsheet_with_the_full_filename_and_a_header_of(String fileName, String fileHeader)
            throws Throwable {

        File directory = new File("./target/excelTestFiles");
        directory.mkdirs();

        File fileCheck = new File(fileName);

        // delete if exists to ensure we have a clean run
        FileUtils.deleteQuietly(fileCheck);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("GenericObject");

        Row headerRow = sheet.createRow(0);
        Cell headerCell = headerRow.createCell(0);
        headerCell.setCellValue(fileHeader);

        FileOutputStream fileOut = new FileOutputStream(fileName);
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();

        fullFilePath = fileName;
    }

    @Given("^headers for fields \"([^\"]*)\" and \"([^\"]*)\"$")
    public void headers_for_fields_and(String fieldOneHeader, String fieldTwoHeader) throws Throwable {
        Workbook workbook = constructWorkbook(fullFilePath);
        Sheet sheet = workbook.getSheet("GenericObject");
        Row newRow = sheet.createRow(1);
        Cell fieldOne = newRow.createCell(0);
        fieldOne.setCellValue(fieldOneHeader);
        Cell fieldTwo = newRow.createCell(1);
        fieldTwo.setCellValue(fieldTwoHeader);

        FileOutputStream fileOut = new FileOutputStream(fullFilePath);
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
    }

    @Given("^the following data in a table$")
    public void the_following_data_in_a_table(List<GenericObject> genericObjectInputList) throws Throwable {
        Workbook workbook = constructWorkbook(fullFilePath);
        Sheet sheet = workbook.getSheet("GenericObject");

        for (int i = 0; i < genericObjectInputList.size(); i++) {
            GenericObject genericObject = genericObjectInputList.get(i);
            Row newRow = sheet.createRow(numberOfExcelHeaderRows + i);
            Cell fieldOne = newRow.createCell(0);
            Cell fieldTwo = newRow.createCell(1);
            if (genericObject.getName() != null) {
                fieldOne.setCellValue(genericObject.getName());
            } else {
                fieldOne.setCellValue("");
            }
            if (genericObject.getId() != null) {
                fieldTwo.setCellValue(genericObject.getId());
            } else {
                fieldTwo.setCellValue("");
            }
        }

        FileOutputStream fileOut = new FileOutputStream(fullFilePath);
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
    }

    @Then("^the following json is returned$")
    public void the_following_json_is_returned(List<String> expectedJsonObject) throws Throwable {
        assertEquals("Returned JSON did not match expected", expectedJsonObject.get(0), outputValue);
    }

    @Then("^the following GenericObject objects are returned$")
    public void the_following_GenericObject_objects_are_returned(List<GenericObject> expectedObjectList)
            throws Throwable {
        @SuppressWarnings("unchecked")
        ArrayList<GenericObject> retrievedList = (ArrayList<GenericObject>) outputValue;
        for (int i = 0; i < retrievedList.size(); i++) {
            assertEquals("Names are not equal", expectedObjectList.get(i).getName(), retrievedList.get(i).getName());
            assertEquals("Ids are not equal", expectedObjectList.get(i).getId(), retrievedList.get(i).getId());
        }
    }

    public Workbook constructWorkbook(String filePath) {
        File file = new File(filePath);
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(inputStream);
        } catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
            e.printStackTrace();
        }

        return workbook;
    }
}
