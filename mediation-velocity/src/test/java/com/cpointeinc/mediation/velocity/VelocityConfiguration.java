package com.cpointeinc.mediation.velocity;

import com.cpointeinc.mediation.MediationConfiguration;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class VelocityConfiguration extends MediationConfiguration {

    @JsonIgnore
    public String velocityTemplate;
    
    @JsonIgnore
    public String inputValidationClassType;
    
}
