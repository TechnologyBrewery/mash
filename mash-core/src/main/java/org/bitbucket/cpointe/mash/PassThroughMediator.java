package org.bitbucket.cpointe.mash;

import java.util.Properties;

/**
 * A simple mediator that simply passes values through. This can be useful if you want to stub in a mediator, but don't
 * have a real routine now (or potentially ever). It also serves at the default mediator should mach not be found for a
 * desired mediator lookup.
 */
public class PassThroughMediator extends Mediator {

    @Override
    protected Object performMediation(Object input, Properties properties) {
        return input;
    }

}
