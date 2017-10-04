package com.cpointeinc.mediation.jolt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cpointeinc.mediation.AbstractMediationSteps;
import com.cpointeinc.mediation.MediationConfiguration;
import com.cpointeinc.mediation.MediationProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class JoltMediationSteps extends AbstractMediationSteps {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(JoltMediationSteps.class);
    
    ObjectMapper objectMapper = new ObjectMapper();
    
    private final String inputJson = "{\n" + 
            "    \"rating\": {\n" + 
            "        \"primary\": {\n" + 
            "            \"value\": 3\n" + 
            "        },\n" + 
            "        \"quality\": {\n" + 
            "            \"value\": 3\n" + 
            "        }\n" + 
            "    }\n" + 
            "}";
    
    @Given("^the following mediation configurations with properties:$")
    public void the_following_mediation_configurations_with_properties(List<JoltConfiguration> backgroundData) throws Throwable {
        List<MediationConfiguration> formalConfiguraiton = new ArrayList<>();
        
        for (JoltConfiguration config : backgroundData) {
            MediationProperty property = new MediationProperty();
            property.setKey(JoltMediator.JOLT_MEDIATION_SPECIFICATION);
            property.setValue(config.joltSpecification);
            List<MediationProperty> properties = new ArrayList<>();
            properties.add(property);
            config.setProperties(properties);
            
            formalConfiguraiton.add(config);
            
        }
        
        writeMediationConfiguration(formalConfiguraiton, "jolt-mediation-configuration.json");
        
        encounteredException = null;
        outputValue = null;
        
    }    

    @When("^the mediator is invoked for \"([^\"]*)\" and \"([^\"]*)\" without a range value$")
    public void the_mediator_is_invoked_for_and_without_a_range_value(String inputType, String outputType) throws Throwable {
        invokeMediator(inputType, outputType, inputJson);
    }

    @Then("^the input is transformed to include two default range values$")
    public void the_input_is_transformed_to_include_two_default_range_values() throws Throwable {
        validateTransformedJson((String)outputValue);
    }

    private void validateTransformedJson(String response) {
        if (encounteredException != null) {
            throw encounteredException;
        }
        assertNotNull("Mediated output was expected!", response);
        assertEquals("Expected two instances of \"Range\":5", 2, StringUtils.countMatches(response.toString(), "\"Range\":5"));
    }
    
    @When("^the mediator is invoked with Jackson objects for \"([^\"]*)\" and \"([^\"]*)\" without a range value$")
    public void the_mediator_is_invoked_with_Jackson_objects_for_and_without_a_range_value(String inputType, String outputType) throws Throwable {        
        Object inputJackson = objectMapper.readValue(inputJson, Object.class);
        invokeMediator(inputType, outputType, inputJackson);
    }

    @Then("^the input is transformed to include two default range values and returned as a Jackson object$")
    public void the_input_is_transformed_to_include_two_default_range_values_and_returned_as_a_Jackson_object() throws Throwable {
        String outputAsJson = objectMapper.writeValueAsString(outputValue);
        validateTransformedJson(outputAsJson);
    }    
    
}
