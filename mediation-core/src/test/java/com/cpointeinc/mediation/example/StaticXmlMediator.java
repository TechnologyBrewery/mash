package com.cpointeinc.mediation.example;

import java.util.Properties;

import com.cpointeinc.mediation.Mediator;

public class StaticXmlMediator extends Mediator {

    public static final String HARDCODED_RESPONSE = "<staticXml>this will never change</staticXml>";

    @Override
    protected Object performMediation(Object input, Properties properties) {
        return HARDCODED_RESPONSE;
    }

}
