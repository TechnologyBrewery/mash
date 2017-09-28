package com.cpointeinc.mediation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class MediationSteps {

    private MediationManager instance;

    private Object outputValue;

    private MediationException encounteredException;
    
    private MediationConfiguration newConfiguration;

    @Given("^the following mediation configurations:$")
    public void the_following_mediation_configurations(List<MediationConfiguration> configs) throws Throwable {
        assertNotNull("No mediation configurations found!", configs);        
        writeMediationConfiguration(configs, "cucumber-definitions.json");

    }

    private void writeMediationConfiguration(List<MediationConfiguration> configs, String filename)
            throws IOException, JsonGenerationException, JsonMappingException {
        
        File directory = new File("./target/mediation-definitions");
        directory.mkdirs();
        File f = new File(directory, filename);
        if (!f.exists()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(f, configs);
        }

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

    private Mediator findMediator(String inputType, String outputType) {
        MediationContext context = new MediationContext(inputType, outputType);
        Mediator mediator = instance.getMediator(context);
        assertNotNull("Could not access mediator for context " + context, mediator);
        return mediator;
    }

    @Then("^a valid mediation routine is NOT available for the intersection of \"([^\"]*)\" and \"([^\"]*)\"$")
    public void a_valid_mediation_routine_is_NOT_available_for_the_intersection_of_and(String inputType,
            String outputType) throws Throwable {
        MediationContext context = new MediationContext(inputType, outputType);
        Mediator mediator = instance.getMediator(context);
        assertEquals("The default pass through mediator should have been returned! ", mediator.getClass(),
                PassThroughMediator.class);
    }

    @When("^the mediator is invoked for \"([^\"]*)\" and \"([^\"]*)\" values \"([^\"]*)\"$")
    public void the_mediator_is_invoked_for_and_values(String inputType, String outputType, String inputValue)
            throws Throwable {
        Mediator mediator = findMediator(inputType, outputType);
        try {
            outputValue = mediator.mediate(inputValue);

        } catch (MediationException e) {
            encounteredException = e;
        }
    }

    @Then("^the input is transformed to \"([^\"]*)\"$")
    public void the_input_is_transformed_to(String expectedValue) throws Throwable {
        assertEquals("Output value was not what was anticipated!", expectedValue, outputValue);
        assertNull("Should not have encountered a MediationException!", encounteredException);
    }

    @Then("^a graceful exception case is returned$")
    public void a_graceful_exception_case_is_returned() throws Throwable {
        assertNull("Should not have returned an output!", outputValue);
        assertNotNull("Should have encountered MediationException!", encounteredException);
    }

}
