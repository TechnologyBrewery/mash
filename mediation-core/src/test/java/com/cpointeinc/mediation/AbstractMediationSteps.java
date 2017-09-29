package com.cpointeinc.mediation;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Contains steps that are commonly used (still need to be called from an annotated step method to be used).
 */
public abstract class AbstractMediationSteps {

    protected MediationManager instance;
    protected Object outputValue;
    protected MediationException encounteredException;

    protected void writeMediationConfiguration(List<MediationConfiguration> configs, String filename)
            throws IOException, JsonGenerationException, JsonMappingException {

        File directory = new File("./target/mediation-definitions");
        directory.mkdirs();
        File f = new File(directory, filename);
        if (!f.exists()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(f, configs);
        }
    }

    protected Mediator findMediator(String inputType, String outputType) {
        MediationContext context = new MediationContext(inputType, outputType);
        instance = MediationManager.getInstance();
        Mediator mediator = instance.getMediator(context);
        assertNotNull("Could not access mediator for context " + context, mediator);
        return mediator;
    }

    protected void invokeMediator(String inputType, String outputType, Object inputValue) {
        Mediator mediator = findMediator(inputType, outputType);
        try {
            outputValue = mediator.mediate(inputValue);
    
        } catch (MediationException e) {
            encounteredException = e;
        }
    }

}
