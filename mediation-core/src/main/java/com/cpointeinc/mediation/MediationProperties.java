package com.cpointeinc.mediation;

import org.aeonbits.owner.KrauseningConfig;
import org.aeonbits.owner.KrauseningConfig.KrauseningSources;

@KrauseningSources("mediation.properties")
public interface MediationProperties extends KrauseningConfig {
	
	@Key("mediation-definition-location")
	String getMediationDefinitionLocation(); 
	
}
