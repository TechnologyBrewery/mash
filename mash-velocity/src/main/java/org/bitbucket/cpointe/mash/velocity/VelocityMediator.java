package org.bitbucket.cpointe.mash.velocity;

import java.io.StringWriter;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.bitbucket.cpointe.mash.MediationException;
import org.bitbucket.cpointe.mash.Mediator;

/**
 * A mediator that uses Velocity to transform some input object to a String. The
 * velocity template to use should be specified in your mediation definition's
 * property with the name "velocity-template".  
 * 
 * Optionally, you can include a velocity-input-class parameter that will validate
 * the input type to this mediation, throwing a {@link MediationException} if the
 * expected type does not match the passed type.
 * 
 * For example:
 * [{
 * 	"inputType": "string",
 * 	"outputType": "string",
 * 	"className": "org.bitbucket.cpointe.mash.velocity.VelocityMediator",
 * 	"properties": [{
 * 		"key": "velocity-template",
 * 		"value": "/somepath/my-transformation-template.vm"
 * 	},
 *	{
 *		"key": "velocity-input-class",
 *		"value":"org.bitbucket.cpointe.mash.some.package.ExpectedInputClass"}
 *  ]
 * }]	
 * 
 * You can extend this class to add additional values to the
 * {@link VelocityContext} by overriding getContext().
 */
public class VelocityMediator extends Mediator {

	/**
	 * The file path to the jolt-specification to be used in this mediator.
	 */
	public static final String VELOCITY_MEDIATION_TEMPLATE = "velocity-template";
	
	/**
	 * Provides the fully qualified class name that a class must be an instance of to be processed.
	 */
	public static final String VELOCITY_INPUT_CLASS = "velocity-input-class";

	/**
	 * The variable in the context that contains the input to the mediation routine.
	 */
	public static final String INPUT = "input";

	private String templatePath;
	
	public VelocityMediator() {
		Properties props = new Properties();
        String className = ClasspathResourceLoader.class.getName();
        props.setProperty("classpath." + VelocityEngine.RESOURCE_LOADER + ".class", className);
        props.setProperty(VelocityEngine.RESOURCE_LOADER, "classpath");
		Velocity.init(props);
	}

	@Override
	protected Object performMediation(Object input, Properties properties) {
		Object output = null;
		if (input != null) {
			StringWriter writer = null;
	
			try {
				validateInputClass(input, properties);
				
				templatePath = properties.getProperty(VELOCITY_MEDIATION_TEMPLATE);
				Template template = getTemplate();
				VelocityContext context = getContext();
				context.put(INPUT, input);
				writer = new StringWriter();
				template.merge(context, writer);
	
				output = writer.toString();
	
			} finally {
				IOUtils.closeQuietly(writer);
	
			}
		}
		
		return output;
	}

	protected Template getTemplate() {
		if (StringUtils.isBlank(templatePath)) {
			throw new MediationException(VELOCITY_MEDIATION_TEMPLATE + " was not specified!");
		}
		
		Template template = Velocity.getTemplate(templatePath);
		
		if (template == null) {
			throw new MediationException("Template '" + templatePath +"' could not be found!");
		}

		return template;

	}
	
	protected void validateInputClass(Object input, Properties properties) {
		String className = properties.getProperty(VELOCITY_INPUT_CLASS, Object.class.getName());
		
		Class<?> expectedInputClass;
		try {
			expectedInputClass = Class.forName(className);

		} catch (ClassNotFoundException e) {
			throw new MediationException("Could not find mediator input class type!", e);
		}
		
		if (!expectedInputClass.isInstance(input)) {
			throw new MediationException("Input (" + input.getClass().getName() + ") is not an instance of " 
					+ expectedInputClass.getName() + "!");
		}
		
	}

	/**
	 * Extension point opportunity to add more information to the context for use in
	 * your velocity template.
	 * 
	 * @return {@link VelocityContext}
	 */
	protected VelocityContext getContext() {
		return new VelocityContext();
	}

}