# Aquality Tracking Java Integrations

The repository contains adaptors for JVM-based test frameworks.

## How to use

1. Add [aqualityTracking.json](./aquality-tracking-integrations-core/src/main/resources/aqualityTracking.json) file with corresponding values to `src/test/resources`.
2. Define dependencies according to the selected test framework (see below).
3. Run tests with the following parameters:

```bash
mvn clean test -Daquality.isEnabled={true/false} -Daquality.suiteName={test_suite_name} -Daquality.buildName={build_name} -Daquality.environment={execution_env} -Daquality.ciBuild={link_to_ci_build} -Daquality.debug={1/0}
```  

Mandatory parameters: `aquality.suiteName`, `aquality.buildName`.

## Cucumber 5

To use this adaptor with Cucumber 5 you have to add the following dependency:

```xml
<dependency>
    <groupId>com.github.aquality-automation</groupId>
    <artifactId>aquality-tracking-cucumber5-jvm</artifactId>
    <version>$LATEST_VERSION</version>
</dependency>
```

Also you have to add plugin `aquality.tracking.integrations.cucumber5jvm.AqualityTrackingCucumber5Jvm` to the Cucumber Test Runner. 