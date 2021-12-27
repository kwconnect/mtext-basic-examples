# M/TEXT Java API - Basic Examples

This repository contains a Java project that provides simple application examples for the M/TEXT Java API.

## Build the project

The project is built with [Apache Maven](https://maven.apache.org/). To resolve the required dependencies, you will need access to kwsoft's Maven repository, which can be accessed here: [kwsoft Artifactory](https://artifactory.kw-data.de/artifactory/kwsoft). You need to modify the file `${HOME}/.m2/settings.xml` accordingly.

As a customer or partner of kwsoft you have the possibility to request access. For this purpose please contact the support team: [kwsoft Jira](https://jira.kwsoft.de/).

To trigger the build, you must specify a sepcific M/TEXT release number. The pom.xml contains a corresponding placeholder `${mtext.version}`.
As an example, run the following command to build the project:
```
mvn clean install -Dmtext.version=6.12.0.371-hotfix3
```
## Execute the Examples
To run the examples, the following requirements must be met:
* Access to a running M/TEXT server instance
* Make all Maven dependencies available in the classpath
* Provide a client.ini according to the documentation

```
java -Dkwsoft.env.globalcontext=/path/to/client.ini -classpath <CLASSPATH> mtext.examples.<EXAMPLE_CLASS_NAME> <ARGUMENTS>
```

## The Examples

The following sections briefly explain each example.

### CreateConnection
The class `mtext.examples.CreateConnection` will log in the given user to the M/TEXT client and closes the connection again.

Arguments:
1. name of a technical user that is allowed to login to the M/TEXT server
2. password of the technical user

```
java mtext.examples.CreateConnection mtextuser mtextpassword
```

### CreateDocument
The class `mtext.examples.CreateDocument` demonstrates how to create a persistent M/TEXT document based on a given data binding.

Arguments
1. name of a technical user that is allowed to login to the M/TEXT server
2. password of the technical user
3. the project that contains the template
4. the template url (e. g. \\bsp_Anschreiben\Vorlagen\Willkommen.template)
5. the name of the data source
6. the path to the data source file (XML or JSON)

```
java mtext.examples.CreateConnection mtextuser mtextpassword bsp_Anschreiben \\bsp_Anschreiben\Vorlagen\Willkommen.template Brief D:\workspace\bsp_Global\Testdaten\Daten_01.xml
```

### ExportPDF
The class `mtext.examples.ExportPDF` demonstrates how to export a temporary M/TEXT document based on a given data binding.

Arguments
1. name of a technical user that is allowed to login to the M/TEXT server
2. password of the technical user
3. the project that contains the template
4. the template url (e. g. \\bsp_Anschreiben\Vorlagen\Willkommen.template)
5. the name of the data source
6. the path to the data source file (XML or JSON)

```
java mtext.examples.ExportPDF mtextuser mtextpassword bsp_Anschreiben \\bsp_Anschreiben\Vorlagen\Willkommen.template Brief D:\workspace\bsp_Global\Testdaten\Daten_01.xml
```
