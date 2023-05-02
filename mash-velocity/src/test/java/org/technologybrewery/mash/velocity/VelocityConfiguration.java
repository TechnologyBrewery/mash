package org.technologybrewery.mash.velocity;

import org.technologybrewery.mash.MediationConfiguration;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class VelocityConfiguration extends MediationConfiguration {

    @JsonIgnore
    public String velocityTemplate;
    
    @JsonIgnore
    public String inputValidationClassType;
    
}
