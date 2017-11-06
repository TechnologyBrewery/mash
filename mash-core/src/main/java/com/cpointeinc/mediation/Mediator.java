package com.cpointeinc.mediation;

import java.lang.reflect.Method;
import java.util.Properties;

/**
 * Defines the contract for a class that provides mediation capabilities.
 */
public abstract class Mediator {
    
    private Properties properties;

    /**
     * Performs mediation based on the input and associated mediation context.  This method will try to leverage the runtime
     * type of the input parameter to find the right method to invoke on the mediator.  If it cannot find such a method, it 
     * will use the required base performMetiation method that takes an Object.
     * 
     * For instance, if a String parameter is passed to this method and a performMediation(String, Properties) method exists, 
     * it will be invoked in favor of performMediation(Object, Properties).  However, if a Document instance is passed and a 
     * corresponding performMediation(Document, Properties) is not found, performMediation(Object, Properties) will be invoked
     * instead. 
     * 
     * @param input
     *            values to mediate
     * @return the mediated result
     */
    public Object mediate(Object input) {
        Object result = null;
        try {
            Class<?> inputClass = (input != null) ? input.getClass() : Object.class;
            Class<?>[] parameterTypes = {inputClass, Properties.class};
            try {
                Method method = this.getClass().getDeclaredMethod("performMediation", parameterTypes);
                method.setAccessible(true);
                Object[] parameters = {input, properties};
                result = method.invoke(this, parameters);
                
            } catch (NoSuchMethodException nsme) {
                result = performMediation(input, properties);
            }
            
            return result;

        } catch (Throwable t) {
            throw new MediationException("Issue encountered while performing mediation: ", t);
        }
    }
    
    void setProperties(Properties properties) {
        this.properties = properties;
    }

    protected abstract Object performMediation(Object input, final Properties properties);

}
