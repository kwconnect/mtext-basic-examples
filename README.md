# M/TEXT Java API - Basic Examples

This repository contains a Java project that provides simple application examples for the M/TEXT Java API.

## Build and execute

The project is built with [Apache Maven](https://maven.apache.org/). To resolve the required dependencies, you will need access to kwsoft's Maven repository, which can be accessed here: [kwsoft Artifactory](https://artifactory.kw-data.de/artifactory/kwsoft).

As a customer or partner of kwsoft you have the possibility to request access. For this purpose please contact the support team: [kwsoft Jira](https://jira.kwsoft.de/).

To trigger the build, you must specify a sepcific M/TEXT release number. The pom.xml contains a corresponding placeholder `${mtext.version}`.
As an example, run the following command to build the project:
```
mvn clean install -Dmtext.version=6.12.0.371-hotfix3
```
