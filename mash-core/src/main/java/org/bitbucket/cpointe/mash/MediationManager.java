package org.bitbucket.cpointe.mash;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;

import org.aeonbits.owner.KrauseningConfigFactory;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Singleton mediation manager for loading mediation configurations defined in
 * the mediation definition location.
 */
public class MediationManager extends BaseMediationManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(MediationManager.class);

    private static MediationProperties properties = KrauseningConfigFactory.create(MediationProperties.class);

    private static MediationManager instance = new MediationManager();

    private ObjectMapper objectMapper = new ObjectMapper();

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
            loadFiles(mediationDefinitionLocation);

        }

        long stop = System.currentTimeMillis();
        LOGGER.debug("Loaded mediation " + mediationOptionMap.size() + " configurations in " + (stop - start) + "ms");
    }

    private void loadFiles(String mediationDefinitionLocation) {
        File locationAsFile = new File(mediationDefinitionLocation);
        if (locationAsFile.exists()) {
            File[] files = locationAsFile.listFiles((FilenameFilter) new SuffixFileFilter(".json"));

            if ((files == null) || (files.length == 0)) {
                LOGGER.warn("No files were found within: " + locationAsFile.getAbsolutePath());

            } else {
                List<MediationConfiguration> mediationConfigurations;

                for (File file : files) {
                    try {
                        mediationConfigurations = objectMapper.readValue(file, objectMapper.getTypeFactory()
                                .constructCollectionType(List.class, MediationConfiguration.class));

                        for (MediationConfiguration mediationConfiguration : mediationConfigurations) {
                            validateAndAddMediator(mediationConfigurations, mediationConfiguration);
                        }

                    } catch (IOException e) {
                        LOGGER.error("Could not read mediation configuration file: " + file.getName());
                    }
                }
            }
        }
    }

    static void resetMediationManager() {
        instance = new MediationManager();
    }

}
