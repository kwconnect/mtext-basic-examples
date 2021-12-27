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
Successfully connected to M/TEXT Client!
The connection is closed!
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
Successfully connected to M/TEXT Client!
Successfully created document: \home\seriem\mtext_example_0003
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
Successfully exported document to PDF file: C:\Users\flinstone\AppData\Local\Temp\Api_Export_Example.pdf
```

### PrintDocument
The class `mtext.examples.PrintDocument` demonstrates how to print an existing M/TEXT document to M/OMS and handle the print result.

Arguments
1. name of a technical user that is allowed to login to the M/TEXT server
2. password of the technical user
3. the fully qualified name of an existing M/TEXT document

```
java mtext.examples.PrintDocument mtextuser mtextpassword \home\seriem\mtext_example_0001
Successfully printed the document to M/OMS.
The M/OMS Input ID is 58
```
### CreateFolder
The class `mtext.examples.CreateFolder` demonstrates how to create a new folder in a specified parent folder.

Arguments
1. name of a technical user that is allowed to login to the M/TEXT server
2. password of the technical user
3. path of the parent folder (e. g. \home\mtextuser)
4. the name of the folder to be created

```
java mtext.examples.CreateFolder mtextuser mtextpassword \home\mtextuser newfolder
Successfully created new folder \home\mtextuser\newfolder
```
### DeleteFolder
The class `mtext.examples.DeleteFolder` demonstrates how to delete a folder.

Arguments
1. name of a technical user that is allowed to login to the M/TEXT server
2. password of the technical user
3. fully qualified path of the folder to be deleted (e. g. \home\mtextuser\newfolder)

```
java mtext.examples.DeleteFolder mtextuser mtextpassword \home\mtextuser newfolder
Successfully deleted new folder \home\mtextuser\newfolder
```
