package com.cpointeinc.mediation;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aeonbits.owner.KrauseningConfigFactory;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Contains all registered mediation options, performing lookups to find the
 * appropriate mediation option for a given input and output type combination.
 */
public class MediationManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(MediationManager.class);

	private static MediationProperties properties = KrauseningConfigFactory.create(MediationProperties.class);

	private static MediationManager instance = new MediationManager();

	private Map<MediationContext, Class<? extends Mediator>> mediationOptionMap = new HashMap<>();

	/**
	 * Prevent instantiation of this singleton instance.
	 */
	private MediationManager() {
		loadConfiguration();
	}

	/**
	 * Returns the singleton instance of the Mediation Manager.
	 * 
	 * @return single instance
	 */
	public static final MediationManager getInstance() {
		return instance;
	}

	private void loadConfiguration() {
		long start = System.currentTimeMillis();
		String mediationDefinitionLocation = properties.getMediationDefinitionLocation();
		LOGGER.debug("Loading mediation configuration from " + mediationDefinitionLocation + "...");

		if (StringUtils.isBlank(mediationDefinitionLocation)) {
			LOGGER.warn("No mediation definitions have been defined!");

		} else {
			File locationAsFile = new File(mediationDefinitionLocation);
			if (locationAsFile.exists()) {
				File[] files = locationAsFile.listFiles((FilenameFilter) new SuffixFileFilter(".json"));

				if ((files == null) || (files.length == 0)) {
					LOGGER.warn("No files were found within: " + locationAsFile.getAbsolutePath());

				} else {
					Class<? extends Mediator> priorInstance;
					List<MediationConfiguration> mediationConfigurations;
					ObjectMapper objectMapper = new ObjectMapper();
					MediationContext context;
					for (File file : files) {
						try {
							mediationConfigurations = objectMapper.readValue(file, objectMapper.getTypeFactory()
									.constructCollectionType(List.class, MediationConfiguration.class));
							
							for (MediationConfiguration mediationConfiguration : mediationConfigurations) {
								Class<? extends Mediator> mediator;
								try {
									mediator = (Class<? extends Mediator>) Class
											.forName(mediationConfiguration.getClassName());
									context = new MediationContext(mediationConfiguration.getInputType(),
											mediationConfiguration.getOutputType());
									priorInstance = mediationOptionMap.put(context, mediator);
									if (priorInstance != null) {
										LOGGER.warn("Duplicate mediation definitions specified for "
												+ mediationConfigurations);

									}
								} catch (ClassNotFoundException e) {
									LOGGER.warn("The specified class " + mediationConfiguration.getClassName()
											+ " was not found in the classpath!");
								}
							}

						} catch (IOException e) {
							LOGGER.error("Could not read mediation configuration file: " + file.getName());
						}
					}
				}
			}

		}

		long stop = System.currentTimeMillis();
		LOGGER.debug("Loaded mediation " + mediationOptionMap.size() + " configurations in " + (stop - start) + "ms");
	}

	public Mediator getMediator(MediationContext context) {
		Class<? extends Mediator> clazz = mediationOptionMap.get(context);
		try {
			return (clazz != null) ? clazz.newInstance() : null;
		} catch (InstantiationException | IllegalAccessException e) {
			throw new MediationException("Could not create class " + clazz.getName(), e);
		}
	}

}
