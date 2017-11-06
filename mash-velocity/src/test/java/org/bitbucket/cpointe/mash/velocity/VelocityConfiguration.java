package org.bitbucket.cpointe.mash.velocity;

import org.bitbucket.cpointe.mash.MediationConfiguration;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class VelocityConfiguration extends MediationConfiguration {

    @JsonIgnore
    public String velocityTemplate;
    
    @JsonIgnore
    public String inputValidationClassType;
    
}
