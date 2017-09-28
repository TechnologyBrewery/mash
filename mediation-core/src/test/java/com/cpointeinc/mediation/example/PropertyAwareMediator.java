package com.cpointeinc.mediation.example;

import java.util.Properties;

import com.cpointeinc.mediation.Mediator;

public class PropertyAwareMediator extends Mediator {

    @Override
    protected Object performMediation(Object input, Properties properties) {
        String propertyA = properties.getProperty("propertyA");
        String propertyB = properties.getProperty("propertyB");
        return propertyA + "-" + input + "-" + propertyB;
    }

}
