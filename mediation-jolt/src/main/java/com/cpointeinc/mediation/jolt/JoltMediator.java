package com.cpointeinc.mediation.jolt;

import java.net.URL;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.bazaarvoice.jolt.Chainr;
import com.bazaarvoice.jolt.JsonUtils;
import com.cpointeinc.mediation.MediationException;
import com.cpointeinc.mediation.Mediator;

/**
 * A mediator that uses Jolt to transform json from one format to another.  This class supports two inbound data representations 
 * of json.  The first is a Jackson object.  The second is a String.  Anything else will result in an attempt to turn the object
 * into a json String.
 */
public class JoltMediator extends Mediator {
    
    public static final String JOLT_MEDIATION_SPECIFICATION = "jolt-specification";

    @Override
    protected Object performMediation(Object input, Properties properties) {
        //TODO: update input types
        Object output = null;
        
        if (input != null) {
            String joltSpecification = properties.getProperty(JOLT_MEDIATION_SPECIFICATION);
            
            if (StringUtils.isBlank(joltSpecification)) {
                throw new MediationException(JOLT_MEDIATION_SPECIFICATION + " was not specified!");
            }
            
            //load jolt specification:
            URL url = this.getClass().getClassLoader().getResource(joltSpecification);
            List<Object> specAsJson = JsonUtils.filepathToList(url.getPath());
            Chainr chainr = Chainr.fromSpec(specAsJson);
            
            //transform:
            output = JsonUtils.toJsonString(chainr.transform(JsonUtils.jsonToObject((String)input)));
        
        }
        
        return output;
    }
    
}
