package com.cpointeinc.mediation.example;

import java.util.Properties;

import com.cpointeinc.mediation.Mediator;

public class ExceptionalMediator extends Mediator {

    @Override
    protected Object performMediation(Object input, Properties properties) {
        throw new RuntimeException("***BOOM*** You asked for this exception!");
    }

}
