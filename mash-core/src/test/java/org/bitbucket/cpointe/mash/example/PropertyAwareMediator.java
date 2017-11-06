package org.bitbucket.cpointe.mash.example;

import java.util.Properties;

import org.bitbucket.cpointe.mash.Mediator;

public class PropertyAwareMediator extends Mediator {

    @Override
    protected Object performMediation(Object input, Properties properties) {
        String propertyA = properties.getProperty("propertyA");
        String propertyB = properties.getProperty("propertyB");
        return propertyA + "-" + input + "-" + propertyB;
    }

}
