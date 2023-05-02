package org.technologybrewery.mash;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains all registered mediation options, performing lookups to find the
 * appropriate mediation option for a given input and output type combination.
 */
public class BaseMediationManager {

    private static final Logger logger = LoggerFactory.getLogger(BaseMediationManager.class);

    private static final PassThroughMediator defaultPassThroughMediator = new PassThroughMediator();

    protected Map<MediationContext, Class<? extends Mediator>> mediationOptionMap = new HashMap<>();
    protected Map<MediationContext, Properties> mediationPropertyMap = new HashMap<>();

    /**
     * Validates and adds a mediator to this mediation manager.
     * 
     * @param mediationConfigurations
     *            list of all mediation configurations
     * @param mediationConfiguration
     *            the mediation configuration of the mediator to add
     */
    public void validateAndAddMediator(List<MediationConfiguration> mediationConfigurations,
            MediationConfiguration mediationConfiguration) {
        Class<? extends Mediator> priorInstance;
        MediationContext context;
        Class<? extends Mediator> mediator;
        try {
            mediator = (Class<? extends Mediator>) Class.forName(mediationConfiguration.getClassName());
            context = new MediationContext(mediationConfiguration.getInputType(),
                    mediationConfiguration.getOutputType());
            priorInstance = mediationOptionMap.put(context, mediator);
            if (priorInstance != null) {
                logger.warn("Duplicate mediation definitions specified for " + mediationConfigurations);

            }

            addMediatorProperties(mediationConfiguration, context);

        } catch (ClassNotFoundException e) {
            logger.warn("The specified class " + mediationConfiguration.getClassName()
                    + " was not found in the classpath!");
        }
    }

    private void addMediatorProperties(MediationConfiguration mediationConfiguration, MediationContext context) {
        if (mediationConfiguration.getProperties() != null) {
            Properties mediatorProperties = new Properties();
            for (MediationProperty property : mediationConfiguration.getProperties()) {
                mediatorProperties.put(property.getKey(), property.getValue());

            }

            mediationPropertyMap.put(context, mediatorProperties);
        }
    }

    /**
     * Returns an instance of the mediator that is mapped to the input type and
     * output type within the passed {@link MediationContext}.This will return a
     * pass-through (i.e., no-op) mediator if a match is not found so that users
     * do not have to worry about checking for a valid mediator - they can just
     * invoke it without worry.
     * 
     * @param context
     *            context information against which the mediator lookup is
     *            performed
     * @return instance of the {@link Mediator} or null, depending on the
     *         configuration
     */
    public Mediator getMediator(MediationContext context) {
        Class<? extends Mediator> clazz = mediationOptionMap.get(context);
        Mediator mediator = null;
        if (clazz != null) {
            try {
                mediator = clazz.newInstance();
                mediator.setProperties(mediationPropertyMap.get(context));

            } catch (InstantiationException | IllegalAccessException e) {
                throw new MediationException("Could not create class " + clazz.getName(), e);
            }
        }

        if (mediator == null) {
            if (logger.isWarnEnabled()) {
                logger.warn("Could not find mediator for " + context.getInputType() + ":" + context.getOutputType()
                        + " - using PassThroughMediator instead!");
            }
            mediator = defaultPassThroughMediator;
        }

        return mediator;

    }

}
