[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=aquality-automation_aquality-tracking-integrations-java&metric=alert_status)](https://sonarcloud.io/dashboard?id=https://sonarcloud.io/dashboard?id=aquality-automation_aquality-tracking-integrations-java)

# Aquality Tracking Java Integrations [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.aquality-automation/aquality-tracking-integrations-core/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.aquality-automation/aquality-tracking-integrations-core)

The repository contains adaptors for JVM-based test frameworks.

## How to use

1. Add [aqualityTracking.json](./aquality-tracking-integrations-core/src/main/resources/aqualityTracking.json) file with corresponding values to `src/test/resources`.
2. Define dependencies according to the selected test framework (see below).
3. Run tests with the following parameters:

```bash
mvn clean test -Daquality.enabled={true/false} -Daquality.suiteName={test_suite_name} -Daquality.buildName={build_name} -Daquality.environment={execution_env} -Daquality.ciBuild={link_to_ci_build} -Daquality.debug={true/false}
```  

Mandatory parameters: `aquality.suiteName`, `aquality.buildName`.

## Cucumber 5 [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.aquality-automation/aquality-tracking-cucumber5-jvm/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.aquality-automation/aquality-tracking-cucumber5-jvm)

To use this adaptor with Cucumber 5 you have to add the following dependency:

```xml
<dependency>
    <groupId>com.github.aquality-automation</groupId>
    <artifactId>aquality-tracking-cucumber5-jvm</artifactId>
    <version>$LATEST_VERSION</version>
</dependency>
```

Also you have to add plugin `aquality.tracking.integrations.cucumber5jvm.AqualityTrackingCucumber5Jvm` to the Cucumber Test Runner. 