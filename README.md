[![Build Status](https://dev.azure.com/aquality-automation/aquality-automation/_apis/build/status/aquality-automation.aquality-tracking-integrations-java?branchName=master)](https://dev.azure.com/aquality-automation/aquality-automation/_build/latest?definitionId=14&branchName=master)
[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=aquality-automation_aquality-tracking-integrations-java&metric=alert_status)](https://sonarcloud.io/dashboard?id=aquality-automation_aquality-tracking-integrations-java)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.aquality-automation/aquality-tracking-integrations-core/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.aquality-automation/aquality-tracking-integrations-core)

# Aquality Tracking Java Integrations 

The repository contains adaptors for JVM-based test frameworks.

## How to use

1. Define dependencies according to the selected test framework (see below).
2. Add [aqualityTracking.json](./aquality-tracking-integrations-core/src/main/resources/aqualityTracking.json) file with corresponding values to `src/test/resources`.

You are able to override these values from CI build using Maven properties:

```bash
mvn clean test -Daquality.enabled={true/false} 
    -Daquality.host={aquality_tracking_address}
    -Daquality.token={api_token}
    -Daquality.projectId={project_id}
    -Daquality.executor={name_of_executor}
    -Daquality.suiteName={test_suite_name} 
    -Daquality.buildName={build_name} 
    -Daquality.environment={execution_env} 
    -Daquality.ciBuild={link_to_ci_build} 
    -Daquality.debug={true/false}
    -Daquality.attachmentsDirectory={path_to_directory_with_attachments}
```  
## Cucumber 7 [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.aquality-automation/aquality-tracking-cucumber7-jvm/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.aquality-automation/aquality-tracking-cucumber7-jvm)

To use this adaptor with Cucumber 7 you have to add the following dependency:

```xml
<dependency>
    <groupId>com.github.aquality-automation</groupId>
    <artifactId>aquality-tracking-cucumber7-jvm</artifactId>
    <version>$LATEST_VERSION</version>
</dependency>
```

Also, you have to add plugin `aquality.tracking.integrations.cucumber7jvm.AqualityTrackingCucumber7Jvm` to the Cucumber Test Runner.

## Cucumber 6 [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.aquality-automation/aquality-tracking-cucumber6-jvm/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.aquality-automation/aquality-tracking-cucumber6-jvm)

To use this adaptor with Cucumber 6 you have to add the following dependency:

```xml
<dependency>
    <groupId>com.github.aquality-automation</groupId>
    <artifactId>aquality-tracking-cucumber6-jvm</artifactId>
    <version>$LATEST_VERSION</version>
</dependency>
```

Also, you have to add plugin `aquality.tracking.integrations.cucumber6jvm.AqualityTrackingCucumber6Jvm` to the Cucumber Test Runner.

## Cucumber 5 [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.aquality-automation/aquality-tracking-cucumber5-jvm/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.aquality-automation/aquality-tracking-cucumber5-jvm)

To use this adaptor with Cucumber 5 you have to add the following dependency:

```xml
<dependency>
    <groupId>com.github.aquality-automation</groupId>
    <artifactId>aquality-tracking-cucumber5-jvm</artifactId>
    <version>$LATEST_VERSION</version>
</dependency>
```

Also, you have to add plugin `aquality.tracking.integrations.cucumber5jvm.AqualityTrackingCucumber5Jvm` to the Cucumber Test Runner. 

## Cucumber 4 [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.aquality-automation/aquality-tracking-cucumber4-jvm/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.aquality-automation/aquality-tracking-cucumber4-jvm)

To use this adaptor with Cucumber 4 you have to add the following dependency:

```xml
<dependency>
    <groupId>com.github.aquality-automation</groupId>
    <artifactId>aquality-tracking-cucumber4-jvm</artifactId>
    <version>$LATEST_VERSION</version>
</dependency>
```

Also, you have to add plugin `aquality.tracking.integrations.cucumber4jvm.AqualityTrackingCucumber4Jvm` to the Cucumber Test Runner.

#### How to increase version for all modules

```bash
mvn versions:set -DnewVersion={new_version}
``` 