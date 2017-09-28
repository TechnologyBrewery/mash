package com.cpointeinc.mediation;

import java.util.Properties;

/**
 * Defines the contract for a class that provides mediation capabilities.
 */
public abstract class Mediator {
    
    private Properties properties;

    /**
     * Performs mediation based on the input and associated mediation context.
     * 
     * @param input
     *            values to mediate
     * @return the mediated result
     */
    public Object mediate(Object input) {
        try {
            return performMediation(input, properties);

        } catch (Throwable t) {
            throw new MediationException("Issue encountered while performing mediation: ", t);
        }
    }
    
    void setProperties(Properties properties) {
        this.properties = properties;
    }

    protected abstract Object performMediation(Object input, final Properties properties);

}
