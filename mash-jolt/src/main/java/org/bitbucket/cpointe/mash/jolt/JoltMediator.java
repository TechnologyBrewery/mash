package org.bitbucket.cpointe.mash.jolt;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.bitbucket.cpointe.mash.MediationException;
import org.bitbucket.cpointe.mash.Mediator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bazaarvoice.jolt.Chainr;
import com.bazaarvoice.jolt.JsonUtils;

/**
 * A mediator that uses Jolt to transform json from one format to another. This
 * class supports two inbound data representations of json. The first is a
 * Jackson object. The second is a String.
 * 
 * Example usage:
 * [{
 * 	"inputType": "string",
 * 	"outputType": "string",
 * 	"className": "org.bitbucket.cpointe.mash.jolt.JoltMediator",
 * 	"properties": [{
 * 		"key": "jolt-specification",
 * 		"value": "/somepath/my-jolt-specification.json"
 * 	}]
 * }]	
 */
public class JoltMediator extends Mediator {

    private static final Logger logger = LoggerFactory.getLogger(JoltMediator.class);
    
	/**
	 * The file path to the jolt-specification to be used in this mediator.
	 */
	public static final String JOLT_MEDIATION_SPECIFICATION = "jolt-specification";

	/**
	 * Runs Jolt transform on a Jackson object.
	 * 
	 * @param input
	 *            a Jackson object
	 * @param properties
	 *            registered to this {@link Mediator} instance
	 * @return the transformed input value as a Jackson object
	 */
	@Override
	protected Object performMediation(Object input, Properties properties) {
		Object output = null;

		if (input != null) {
			Chainr chainr = getExecutableJoltTransformSchema(properties);
			output = chainr.transform(input);

		}

		return output;
	}

	/**
	 * Runs Jolt transform on a json string.
	 * 
	 * @param input
	 *            a valid json string
	 * @param properties
	 *            registered to this {@link Mediator} instance
	 * @return the transformed input value as a json string
	 */
	protected String performMediation(String input, Properties properties) {
		String output = null;

		if (input != null) {
			Chainr chainr = getExecutableJoltTransformSchema(properties);
			Object result = chainr.transform(JsonUtils.jsonToObject(input));

			// transform:
			output = JsonUtils.toJsonString(result);

		}

		return output;
	}

	private Chainr getExecutableJoltTransformSchema(Properties properties) {
		String joltSpecification = properties.getProperty(JOLT_MEDIATION_SPECIFICATION);

		if (StringUtils.isBlank(joltSpecification)) {
			throw new MediationException(JOLT_MEDIATION_SPECIFICATION + " was not specified!");
		}

		// load jolt specification:
		URL url = this.getClass().getClassLoader().getResource(joltSpecification);
		
		String decodedPath = null;
		try {
            decodedPath = URLDecoder.decode(url.getPath(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
		
		logger.debug("Using jolt specification: {}", decodedPath);
		List<Object> specAsJson = JsonUtils.filepathToList(decodedPath);
		Chainr chainr = Chainr.fromSpec(specAsJson);
		return chainr;
	}

}
