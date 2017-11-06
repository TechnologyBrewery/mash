package org.bitbucket.cpointe.mash.example;

import java.util.Properties;

import org.bitbucket.cpointe.mash.Mediator;

public class ExceptionalMediator extends Mediator {

    @Override
    protected Object performMediation(Object input, Properties properties) {
        throw new RuntimeException("***BOOM*** You asked for this exception!");
    }

}
