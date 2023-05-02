package org.technologybrewery.mash.example;

import java.util.Properties;

import org.technologybrewery.mash.Mediator;

public class StaticXmlMediator extends Mediator {

    public static final String HARDCODED_RESPONSE = "<staticXml>this will never change</staticXml>";

    @Override
    protected Object performMediation(Object input, Properties properties) {
        return HARDCODED_RESPONSE;
    }

}
