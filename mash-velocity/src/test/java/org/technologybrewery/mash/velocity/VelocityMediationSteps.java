package org.technologybrewery.mash.velocity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.technologybrewery.mash.AbstractMediationSteps;
import org.technologybrewery.mash.MediationConfiguration;
import org.technologybrewery.mash.MediationProperty;

import com.fasterxml.jackson.databind.ObjectMapper;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class VelocityMediationSteps extends AbstractMediationSteps {	

	@Given("^the following mediation configurations with properties:$")
	public void the_following_mediation_configurations_with_properties(List<VelocityConfiguration> backgroundData)
			throws Throwable {
		List<MediationConfiguration> formalConfiguraiton = new ArrayList<>();		
		
		for (VelocityConfiguration config : backgroundData) {
			List<MediationProperty> properties = new ArrayList<>();
			
			MediationProperty template = new MediationProperty();
			template.setKey(VelocityMediator.VELOCITY_MEDIATION_TEMPLATE);
			template.setValue(config.velocityTemplate);
			properties.add(template);
			
			if (StringUtils.isNotBlank(config.inputValidationClassType)) {
				MediationProperty inputClass = new MediationProperty();
				inputClass.setKey(VelocityMediator.VELOCITY_INPUT_CLASS);
				inputClass.setValue(config.inputValidationClassType);
				properties.add(inputClass);
			}
						
			config.setProperties(properties);

			formalConfiguraiton.add(config);

		}

		writeMediationConfiguration(formalConfiguraiton, "velocity-mediation-configuration.json");

		encounteredException = null;
		outputValue = null;

	}

	@When("^the mediator is invoked for \"([^\"]*)\" and \"([^\"]*)\" with a property set of \"([^\"]*)\" and \"([^\"]*)\"$")
	public void the_mediator_is_invoked_for_and_with_a_property_set_of_and(String inputType, String outputType, String name,
			String value) throws Throwable {
		TestProperty property = new TestProperty(name, value);
		invokeMediator(inputType, outputType, property);
		
	}

	@Then("^the input is transformed to a property xml element with a name of \"([^\"]*)\" and a value of \"([^\"]*)\"$")
	public void the_input_is_transformed_to_a_property_xml_element_with_a_name_of_and_a_value_of(String name,
			String value) throws Throwable {
		//this is cheap trick, but we just care that the values have been pushed via a velocity template here, not about
		//format details:
		ensureNoException();
		String outputAsString = (String)outputValue;
		assertTrue("name not transformed corrected!", outputAsString.contains("name=\"" + name + "\""));
		assertTrue("value not transformed corrected!", outputAsString.contains("value=\"" + value + "\""));
		
	}

	@Then("^the input is transformed to a json element with a name of \"([^\"]*)\" and a value of \"([^\"]*)\"$")
	public void the_input_is_transformed_to_a_json_element_with_a_name_of_and_a_value_of(String name, String value)
			throws Throwable {		
		ensureNoException();
		
		ObjectMapper mapper = new ObjectMapper();
		TestProperty outputProperty = mapper.readValue((String)outputValue, TestProperty.class);
		assertEquals("name not transformed corrected!", name, outputProperty.getName());
		assertEquals("value not transformed corrected!", value, outputProperty.getValue());
		
		
	}

	@Then("^a graceful exception case is returned from the velocity mediator$")
	public void a_graceful_exception_case_is_returned_from_the_velocity_mediator() throws Throwable {
        assertNull("Should not have returned an output!", outputValue);
        assertNotNull("Should have encountered MediationException!", encounteredException);
    }

}
