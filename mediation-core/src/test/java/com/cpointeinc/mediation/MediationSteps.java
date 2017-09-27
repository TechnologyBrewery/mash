package com.cpointeinc.mediation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class MediationSteps {
	
	private MediationManager instance;
	
	private Object outputValue;
	
	private MediationException encounteredException;

	@Given("^the following mediation configurations:$")
	public void the_following_mediation_configurations(List<MediationConfiguration> configs) throws Throwable {
	    assertNotNull("No mediation configurations found!", configs);
	    
	    File directory = new File("./target/mediation-definitions");
	    directory.mkdirs();
	    File f = new File(directory, "cucumber-definitions.json");
	    if (!f.exists()) {
	    		ObjectMapper objectMapper = new ObjectMapper();
	    		objectMapper.writeValue(f, configs);
	    }
	    
	    outputValue = null;
	    encounteredException = null;
	    
	}

	@When("^mediation is configured for runtime$")
	public void mediation_is_configured_for_runtime() throws Throwable {
		instance = MediationManager.getInstance();
	}

	@Then("^a valid mediation routine is available for the intersection of \"([^\"]*)\" and \"([^\"]*)\"$")
	public void a_valid_mediation_routine_is_available_for_the_intersection_of_and(String inputType, String outputType) throws Throwable {
		findMediator(inputType, outputType);
	}

	private Mediator findMediator(String inputType, String outputType) {
		MediationContext context = new MediationContext(inputType, outputType);
	    Mediator mediator = instance.getMediator(context);
	    assertNotNull("Could not access mediator for context " + context, mediator);
	    return mediator;
	}
	
	@Then("^a valid mediation routine is NOT available for the intersection of \"([^\"]*)\" and \"([^\"]*)\"$")
	public void a_valid_mediation_routine_is_NOT_available_for_the_intersection_of_and(String inputType, String outputType) throws Throwable {
		MediationContext context = new MediationContext(inputType, outputType);
		Mediator mediator  = instance.getMediator(context);
	    assertNull("This mediator should not return a value! " + context, mediator);
	}
	
	@When("^the mediator is invoked for \"([^\"]*)\" and \"([^\"]*)\" values \"([^\"]*)\"$")
	public void the_mediator_is_invoked_for_and_values(String inputType, String outputType, String inputValue) throws Throwable {
		Mediator mediator  = findMediator(inputType, outputType);
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
