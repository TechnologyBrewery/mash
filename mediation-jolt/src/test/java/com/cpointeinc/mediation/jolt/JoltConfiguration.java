package com.cpointeinc.mediation.jolt;

import com.cpointeinc.mediation.MediationConfiguration;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class JoltConfiguration extends MediationConfiguration {

    @JsonIgnore
    public String joltSpecification;
    
}
