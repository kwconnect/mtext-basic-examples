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
java mtext.examples.CreateDocument mtextuser mtextpassword bsp_Anschreiben \\bsp_Anschreiben\Vorlagen\Willkommen.template Brief D:\workspace\bsp_Global\Testdaten\Daten_01.xml
Successfully connected to M/TEXT Client!
Successfully created document: \home\mtextuser\mtext_example_0003
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
java mtext.examples.PrintDocument mtextuser mtextpassword \home\mtextuser\mtext_example_0001
Successfully printed the document to M/OMS.
The M/OMS Input ID is 58
```

### PrintDocumentJobPart
The class `mtext.examples.PrintDocumentJobPart` demonstrates how to print more than one document with a single job. 

The example makes use of the Configuration Parameter `de.kwsoft.mtext.api.ConfigurationFactory.PRINT_JOB_PART`. One concatenated print stream will be produced from all the documents.

Arguments
1. name of a technical user that is allowed to login to the M/TEXT server
2. password of the technical user
3. the fully qualified name of an existing M/TEXT document
4. the name of the print destination

```
java mtext.examples.PrintDocumentJobPart mtextuser mtextpassword \home\mtextuser\mtext_example_0001 OMS
Added document to job \home\mtextuser\mtext_example_0001
Added document to job \home\mtextuser\mtext_example_0001
Added document to job \home\mtextuser\mtext_example_0001
Added document to job \home\mtextuser\mtext_example_0001
Added document to job \home\mtextuser\mtext_example_0001
Successfully executed print job.
```

### PrintDocumentMultithreaded
The class `mtext.examples.PrintDocumentMultithreaded` demonstrates how to print documents in parallel. It uses a given number of threads and jobs to control the throughput.

Arguments
1. name of a technical user that is allowed to login to the M/TEXT server
2. password of the technical user
3. the fully qualified name of an existing M/TEXT document
4. the name of the print destination
5. the number of jobs per thread
6. the number of threads

```
java mtext.examples.PrintDocumentMultithreaded mtextuser mtextpassword \home\mtextuser\mtext_example_0001 OMS 2 3
Thread: 12, job: 0
Thread: 13, job: 0
Thread: 11, job: 0
Successfully printed document \home\mtextuser\mtext_example_0001
Thread: 11, job: 1
Successfully printed document \home\mtextuser\mtext_example_0001
Thread: 13, job: 1
Successfully printed document \home\mtextuser\mtext_example_0001
Thread: 12, job: 1
Successfully printed document \home\mtextuser\mtext_example_0001
Successfully printed document \home\mtextuser\mtext_example_0001
Successfully printed document \home\mtextuser\mtext_example_0001
```

### GetDocumentInformation
The class `mtext.examples.GetDocumentInformation` demonstrates how to get DocumentInformation for Documents and Models by iterating over 
all DocumentIformations of the given folder.

Arguments
1. name of a technical user that is allowed to login to the M/TEXT server
2. password of the technical user
3. folder name

```
java mtext.examples.GetDocumentInformation mtextuser mtextpassword \home\mtextuser
Dumping DocumentInformation for folder \home\mtextuser
Document: mtext_example_0001
         Qualified Name : \home\mtextuser\Wilkommen_0001
         Description    : 
         Created        :2021-12-15 10:44:03.94
         Created by     :mtextuser
         Modified       :2021-12-15 10:44:06.878
         Modified by    :mtextuser
         Printed        :1970-01-01 01:00:00.0
         Printed by     :
         Metadata       :
                  METADATA.STATE = DELETED
```
### GetMetadata
The class `mtext.examples.GetMetadata` demonstrates how to access searchable Metadata of a given M/TEXT document.

Searchable Metadata is stored in a separate database table. You can access it via the DocumentInformation object. That access is faster, as it does not require to read the whole content of the document.

Arguments
1. name of a technical user that is allowed to login to the M/TEXT server
2. password of the technical user
3. folder path of the document
4. name of the document
5. name of the Metadata

```
java mtext.examples.GetMetadata mtextuser mtextpassword \home\mtextuser mtext_example_0001 METADATA.STATE
METADATA.STATE = Created
```
### GetMetadataNotSearchable
The class `mtext.examples.GetMetadataNotSearchable` demonstrates how to access both types of Metadata (searchable and not searchable) of a given M/TEXT document.

Not searchable Metadata is stored in the document itself. To get access you have to open the document in a first step. That access is more expensive than reading searchable Metadata.

Arguments
1. name of a technical user that is allowed to login to the M/TEXT server
2. password of the technical user
3. folder path of the document
4. name of the document
5. name of the Metadata

```
java mtext.examples.GetMetadataNotSearchable mtextuser mtextpassword \home\mtextuser mtext_example_0001 METADATA.DEFAULTOUTPUTTARGET
METADATA.DEFAULTOUTPUTTARGET = PRINT
```

### SetMetadata
The class `mtext.examples.SetMetadata` demonstrates how to set searchable Metadata of a given M/TEXT document.

Arguments
1. name of a technical user that is allowed to login to the M/TEXT server
2. password of the technical user
3. folder path of the document
4. name of the document
5. name of the Metadata
6. new value of the Metadata

```
java mtext.examples.SetMetadata mtextuser mtextpassword \home\mtextuser mtext_example_0001 METADATA.STATE Deleted
Successfully set Metadata METADATA.STATE = Deleted
```

### DeleteMetadata
The class `mtext.examples.DeleteMetadata` demonstrates how to delete a specific Metadata from a given M/TEXT document.

Arguments
1. name of a technical user that is allowed to login to the M/TEXT server
2. password of the technical user
3. folder path of the document
4. name of the document
5. name of the Metadata

```
java mtext.examples.DeleteMetadata mtextuser mtextpassword \home\mtextuser mtext_example_0001 METADATA.DEFAULTOUTPUTTARGET
Successfully removed Metadata METADATA.DEFAULTOUTPUTTARGET
```

### ImportDocument
The class `mtext.examples.DeleteMetadata` demonstrates how to import a mtxz file to the M/TEXT Server.

Arguments
1. name of a technical user that is allowed to login to the M/TEXT server
2. password of the technical user
3. path to the mtxz file
4. name of the document to be created

```
java mtext.examples.ImportDocument mtextuser mtextpassword \home\mtextuser D:\temp\exported.mtxz imported_0001
Successfully imported document \home\mtextuser\imported_0001
```

### OpenTemplateSelectionDialog
The class `mtext.examples.OpenTemplateSelectionDialog` demonstrates how to show a Template Selection Dialog.

Arguments
1. name of a technical user that is allowed to login to the M/TEXT server
2. password of the technical user
3. **optional** filter key, which is used to restrict the template selection according to the Metadata 'Metadata.Sparte'

```
java mtext.examples.OpenTemplateSelectionDialog mtextuser mtextpassword 
```

![image](https://user-images.githubusercontent.com/8683821/147480261-cff0feb9-32c4-45bc-8a3b-9ea23506b9b2.png)


### Working with Folders

#### CreateFolder
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
#### DeleteFolder
The class `mtext.examples.DeleteFolder` demonstrates how to delete a folder.

Arguments
1. name of a technical user that is allowed to login to the M/TEXT server
2. password of the technical user
3. fully qualified path of the folder to be deleted (e. g. \home\mtextuser\newfolder)

```
java mtext.examples.DeleteFolder mtextuser mtextpassword \home\mtextuser newfolder
Successfully deleted new folder \home\mtextuser\newfolder
```

#### GetAllFolders
The class `mtext.examples.GetAllFolders` recursively outputs all folders in the system.

Arguments
1. name of a technical user that is allowed to login to the M/TEXT server
2. password of the technical user

```
java mtext.examples.GetAllFolders mtextuser mtextpassword \home\mtextuser newfolder
   home
      MASTER
      flinstone
      mtextuser
         subfolder1
```

### M/TEXT Classic Examples

#### OpenClassicEditor
**Pleas note:** This example is not applicable for TONIC documents!

The class `mtext.examples.OpenClassicEditor` demonstrates how to edit an existing M/TEXT Classic document with the Java Swing client.

Arguments
1. name of a technical user that is allowed to login to the M/TEXT server
2. password of the technical user
3. fully qualified name of the M/TEXT document that is to be edited

```
java mtext.examples.OpenClassicEditor mtextuser mtextpassword \home\mtextuser\mtext_example_0001
```

![image](https://user-images.githubusercontent.com/8683821/147468627-65b6dcb2-17b3-46dd-bf3c-52c9b7e3c139.png)

#### OpenClassicPreview
**Pleas note:** This example is not applicable for TONIC documents!

The class `mtext.examples.OpenClassicPreview` demonstrates how to show the preview for an existing M/TEXT Classic document with the Java Swing client.

Arguments
1. name of a technical user that is allowed to login to the M/TEXT server
2. password of the technical user
3. fully qualified name of the M/TEXT document that is to be edited

```
java mtext.examples.OpenClassicPreview mtextuser mtextpassword \home\mtextuser\Willkommen_0002
```

![image](https://user-images.githubusercontent.com/8683821/147477743-2e50d818-c8ef-4437-a33c-b528b45098ed.png)

#### GetClassicDocumentContent
**Pleas note:** This example is not applicable for TONIC documents!

The class `mtext.examples.GetClassicDocumentContent` demonstrates how to access the document content of a given M/TEXT document.

Arguments
1. name of a technical user that is allowed to login to the M/TEXT server
2. password of the technical user
3. folder path of the document
4. name of the document

```
java mtext.examples.GetClassicDocumentContent mtextuser mtextpassword \home\mtextuser mtext_example_0001
TextLine: .BASE bsp_ClassicAnschreiben
TextLine: .DSC 
TextLine: .USES Datenmodelle\Adresse AS $ADRESSE
TextLine: .USES Datenmodelle\Sachbearbeiter AS $SB
TextLine: .USES Datenmodelle\Briefdaten AS $BD
ModelLine: \library\bsp_ClassicCommon\Bausteine\Adresse
ModelLine: \\bsp_ClassicCommon\Bausteine\Betreff
        Parameter1: 
TextLine: .PAR
TextLine: <F>Datum<N>: *MT-SDATE
TextLine: .PAR
TextLine: <F>Regressionstest<N>: *MT-REGRESSIONTEST
ModelLine: \\bsp_ClassicCommon\Bausteine\Grussformel
        Parameter1: 
ModelLine: Bausteine\Bankeinzug
ModelLine: Bausteine\AGB
TextLine: 
```
