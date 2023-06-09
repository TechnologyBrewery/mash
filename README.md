# Mash Data Mediation #

[![Maven Central](https://img.shields.io/maven-central/v/org.technologybrewery.mash/mash-parent.svg)](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.technologybrewery.mash%22%20AND%20a%3A%22mash-parent%22)
[![License](https://img.shields.io/github/license/mashape/apistatus.svg)](https://opensource.org/licenses/mit)

In brewing, mashing passes raw ingredients through hot water to activate, hydrate, and convert them for fermentation. Mash, the open source project, takes raw data payloads and provides a generic mediation process to translate the payloads into a new output. This allows configuration-driven mediation to be plugged into your application, which is especially important when dealing with ad-hoc tweaks that need to occur to payloads to add, remove, or alter them to conform to changing service payloads to/from other systems.

This project provides an embeddable mediation component that supports data transformation needs.  The key benefit to the approach taken in this library is the ability to dynamically configure and look up mediation routines.  By adding inherent support for runtime lookups, this approach allows mediation to be stubbed in or dynamically changed WITHOUT the need for a new deployment.  This is especially useful in environments where there is a high (or at least slow) IA hurdle along the path to production.  It also can insulate your system from changes with integration partners by allowing you to quickly respond with a mediation configuration update that obviates the need to perform a release every time an integration partner makes an unexpected change to their data structure.

This core library has the following sub-components:

* **Jolt**: support for json mediation.  This component solely driven via configured via json schema files that define the transformation rules from one json format to another.
* **Velocity**: support for generic, template-driven mediation via the tried and true Velocity library.

This project will soon have additional sub-components that implement additional configurable mediation options (e.g., XSLT).  You can always author custom mediation routines as well for use cases that don't fit well with existing approaches.

# Krausening Configuration#
A mediation.properties file is expected in your Krausening configuration.  It requires:

* mediation-definition-location - the path to a json file containing your mediation configuration

# Writing or Reusing Mediators #
You can author a custom mediator by extending a single method in Mediator.java:
```
#!java
public class LoggingMediator extends Mediator {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingMediator.class);

    @Override
    protected Object performMediation(Object input) {
        LOGGER.debug(input.toString());
        return input;
    }

}
```  

There are a few simple, but commonly used mediators included within this library:

* PassThroughMediator - simply echos the inbound value when invoked
* LoggingMediator - logs the inbound value at debug-level by echoing back the value when invoked

# Configuring Mediators to Runtime Lookup #
As previously mentioned, a key aspect to this approach is that mediators are not hard-coded into your codebase.  You must first configure each mediator to a specific input and output type.  This is done by specifying a simple json file(s) that is located within the directory specified in your mediation-definition-location Krausening properties.
```
#!json
[{
	"inputType": "json",
	"outputType": "xml",
	"className": "org.technologybrewery.mash.example.mash.StaticXmlMediator"
}, {
	"inputType": "json",
	"outputType": "json",
	"className": "org.technologybrewery.mash.LoggingMediator"
}, {
	"inputType": "ucore-v2",
	"outputType": "ucore-v3",
	"className": "org.technologybrewery.mash.LoggingMediator"
}]
```

# Performing a Runtime Lookup #
At runtime, you simply request a mediator by providing the input and output types for the mediation routine in question.  The library will return you either an instance of your requested mediator or a PassThroughMediator, if no match is found.  The later option allows you to plug mediation into your logic without having to have a pre-defined mediation routine.  Should your current transformation assumptions change, you can then specify a new mediator via configuration.  This will be more impactful as the extensions of this library come online (e.g., jolt, XSLT) such that whole new mediation routines can be created and applied via relatively simple configuration changes. 
```
#!java
MediationContext context = new MediationContext(inputType, outputType);
Mediator mediator = instance.getMediator(context);
outputValue = mediator.mediate(inputValue);
```

# Error Handling #
All exceptions encountered at runtime will be caught and wrapped in a runtime exception called MediationExeption.

If you have logging enabled at the debug level for org.technologybrewery.mash, you'll also get some potentially useful debugging info this component is bootstrapped:
```
#!bash
26 Sep 2017 22:06:07 DEBUG MediationManager - Loading mediation configuration from ./target/mediation-definitions...
26 Sep 2017 22:06:07  WARN MediationManager - The specified class org.technologybrewery.mash.DoesNotExistMediator was not found in the classpath!
26 Sep 2017 22:06:07 DEBUG MediationManager - Loaded mediation 5 configurations in 35ms
```

# Maven Configuration #
```
#!xml
<dependency>
    <groupId>org.technologybrewery.mash</groupId>
    <artifactId>mash-core</artifactId>
    <version>LATEST-MASH-VERSION</version>
</dependency>
```