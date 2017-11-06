package org.bitbucket.cpointe.mash;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bitbucket.cpointe.mash.MediationConfiguration;
import org.bitbucket.cpointe.mash.MediationContext;
import org.bitbucket.cpointe.mash.MediationException;
import org.bitbucket.cpointe.mash.MediationManager;
import org.bitbucket.cpointe.mash.Mediator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Contains steps that are commonly used. These methods still need to be called from an annotated step method to be
 * leveraged.
 */
public abstract class AbstractMediationSteps {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractMediationSteps.class);

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

	protected void ensureNoException() {
		if (encounteredException != null) {
			LOGGER.error("Unexpected exception:", encounteredException);
			assertNull("Should NOT have encountered MediationException! ", encounteredException);
		}
	}

}
