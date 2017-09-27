package com.cpointeinc.mediation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingMediator extends Mediator {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LoggingMediator.class);

	@Override
	protected Object performMediation(Object input) {
		LOGGER.debug(input.toString());
		return input;
	}

}
