package org.bitbucket.cpointe.mash;

import org.aeonbits.owner.KrauseningConfig;
import org.aeonbits.owner.KrauseningConfig.KrauseningSources;

/**
 * Configuration properties for mediation.
 */
@KrauseningSources("mediation.properties")
public interface MediationProperties extends KrauseningConfig {

    @Key("mediation-definition-location")
    String getMediationDefinitionLocation();

}
