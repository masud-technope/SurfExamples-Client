package core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.texteditor.ITextEditor;

public class CodeDisplayManager {
	
	String codeSnippet;
	int rank;
	public CodeDisplayManager(int rank,String codeSnippet)
	{
		//default constructor
		this.codeSnippet=codeSnippet;
		this.rank=rank;
	}
	
	public void displayCodeInEditor()
	{
		//code for displaying the code in the IDE
		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		
		String tempFilePath=saveTempFile();
		File fileToOpen=new File(tempFilePath);
		if(fileToOpen.isFile() && fileToOpen.exists()){
			IFileStore fileStore = EFS.getLocalFileSystem().getStore(fileToOpen.toURI());
			try {
				IDE.openEditorOnFileStore(page, fileStore);
			} catch (PartInitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//ITextEditor editor = (ITextEditor) page.getActiveEditor();
		//extracting code fragment
		//IDocument doc = editor.getDocumentProvider().getDocument(
		//		editor.getEditorInput());
		//doc.set(this.codeSnippet);
	}
	
	protected String saveTempFile()
	{
		//saving the file temporarily
		String outFile=System.getProperty("user.home")+"/surfexamples/Example"+this.rank+".java";
		try {
			FileWriter frwiter=new FileWriter(new File(outFile));
			frwiter.write("class Example"+this.rank+"\n{\n"+this.codeSnippet+"\n}");
		    frwiter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return outFile;
	}
	

}
