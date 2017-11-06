package org.bitbucket.cpointe.mash.jolt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.bitbucket.cpointe.mash.AbstractMediationSteps;
import org.bitbucket.cpointe.mash.MediationConfiguration;
import org.bitbucket.cpointe.mash.MediationProperty;
import org.bitbucket.cpointe.mash.jolt.JoltMediator;

import com.fasterxml.jackson.databind.ObjectMapper;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class JoltMediationSteps extends AbstractMediationSteps {
    
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
    		ensureNoException();
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
    		ensureNoException();
        String outputAsJson = objectMapper.writeValueAsString(outputValue);
        validateTransformedJson(outputAsJson);
    }    
    
}
