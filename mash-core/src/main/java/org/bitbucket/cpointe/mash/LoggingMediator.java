package org.bitbucket.cpointe.mash;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A pass-through mediator that logs output at a debug level.
 */
public class LoggingMediator extends Mediator {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingMediator.class);

    @Override
    protected Object performMediation(Object input, Properties properties) {
        LOGGER.debug((input != null) ? input.toString() : "no data to mediate!");
        return input;
    }

}
