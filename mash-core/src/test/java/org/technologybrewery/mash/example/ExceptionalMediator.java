package org.technologybrewery.mash.example;

import java.util.Properties;

import org.technologybrewery.mash.Mediator;

public class ExceptionalMediator extends Mediator {

    @Override
    protected Object performMediation(Object input, Properties properties) {
        throw new RuntimeException("***BOOM*** You asked for this exception!");
    }

}
