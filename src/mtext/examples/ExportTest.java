package mtext.examples;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import de.kwsoft.mtext.api.ChangeableDocumentContent;
import de.kwsoft.mtext.api.Configuration;
import de.kwsoft.mtext.api.ConfigurationFactory;
import de.kwsoft.mtext.api.Document;
import de.kwsoft.mtext.api.DocumentContent;
import de.kwsoft.mtext.api.Folder;
import de.kwsoft.mtext.api.FolderNotFoundException;
import de.kwsoft.mtext.api.MText;
import de.kwsoft.mtext.api.PersistentDocument;
import de.kwsoft.mtext.api.server.MTextFactory;
import de.kwsoft.mtext.api.server.MTextServer;
import de.kwsoft.mtext.api.server.ServerJob;

public class ExportTest {
    public static void main(String[] args) throws Exception {
		final String folderNamePlain = "TestInclude";
        final String folderName = "\\" + folderNamePlain;
        final String documentName = "Document";
		final String includeDocumentName = "includeDocument";
        final String pdfName = "Document.pdf";
        final String user = "churavy";
        final String password = "churavy";
        MText mText = null;  
        
        ByteArrayOutputStream baos = null;  
        FileOutputStream fos = null;  
        File file = null;
          
        try {
            // Create the document and the included one
            mText = MTextFactory.connect(user, password, null);                                                         
            
            try {
                mText.getRootFolder().getFolderByName(folderName);
            }
            catch (FolderNotFoundException e) {
                mText.getRootFolder().createSubFolder(folderNamePlain);                
            }

            ServerJob job = ((MTextServer) mText).createJob();
            job.begin();                                                                                                
            Document document = job.createPersistentTextDocument(folderName + "\\" + documentName, "Test", null);                    
			Document includeDocument = job.createPersistentTextDocument(folderName + "\\" + includeDocumentName, "Test", null);                    
            job.execute();                                                                                              
            
            // Put some text into the documents
            job = ((MTextServer) mText).createJob();                                                                    
            job.begin();                                                                                                
            job.assignDocument(document);                                                                               
			job.assignDocument(includeDocument);                                                                               
            DocumentContent documentContent = (ChangeableDocumentContent) document.getDocumentContent();
			DocumentContent includeDocumentContent = (ChangeableDocumentContent) includeDocument.getDocumentContent();
			
			((ChangeableDocumentContent) documentContent).addTextLine(".INC " + folderName + "\\" +includeDocumentName);
            ((ChangeableDocumentContent) documentContent).addTextLine("Text ........................");

			((ChangeableDocumentContent) includeDocumentContent).addTextLine("Text from the included document");

			((PersistentDocument) document).save();
			((PersistentDocument) includeDocument).save();
			
			document.close();
			includeDocument.close();			
            job.execute();               
            
			// Check if the document exist
			Folder folder = (Folder) mText.getRootFolder().getFolderByName(folderName);
			System.out.println("Check document \"document\"       : " + folder.isDocumentExisting(documentName));
			System.out.println("Check document \"includeDocument\": " + folder.isDocumentExisting(includeDocumentName));
			
            // Export the document
            job = ((MTextServer) mText).createJob();                                                                    
            job.begin();                                                                                                
			document = job.openTextDocument(folderName + "\\" + documentName, de.kwsoft.mtext.api.DocumentAccessMode.SHARED_READ_ONLY, null);
            baos = new ByteArrayOutputStream();
            for (int i = 0; i < 5; i++) {
                Configuration configuration = job.getConfigurationFactory().newExportDocumentConfiguration();
                configuration.put(ConfigurationFactory.PRINT_JOB_PART, Boolean.TRUE);
                job.exportDocument(document, "application/pdf", baos, configuration);                                                           
            }
			document.close();
            job.execute();                                                                                              
            
            // Save the stream
            file = new File(pdfName);                                                                              
            fos = new FileOutputStream(file);                                                                      
            baos.writeTo(fos);                                                                                                 
            fos.flush();                                                                                                
			
			// Delete the documents
			job = ((MTextServer) mText).createJob();                                                                    
            job.begin();     
			job.deletePersistentTextDocument(folderName + "\\" + documentName, null);
			job.deletePersistentTextDocument(folderName + "\\" + includeDocumentName, null);
			job.execute();                   
			
			// Check if document are deleted
			folder = (Folder) mText.getRootFolder().getFolderByName(folderName);
			System.out.println("Check document \"document\"       : " + folder.isDocumentExisting(documentName));
			System.out.println("Check document \"includeDocument\": " + folder.isDocumentExisting(includeDocumentName));
        }
        catch(Exception e) {
            System.out.println("An error occured:");
            e.printStackTrace();
        }
        finally {
            if (mText != null) {
                if (!mText.isClosed()) {
                    mText.close();                                                                                      
                }
            }
            
            if (baos != null) {
                baos.close();                                                                                           
            }
            
            if (fos != null) {
                fos.close();                                                                                            
            }            
        }
    }
}
