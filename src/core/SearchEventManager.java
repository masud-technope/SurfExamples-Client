package core;

import java.util.ArrayList;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;

import ca.usask.cs.srlab.surfexample.handlers.ViewContentProvider;
import ca.usask.cs.srlab.surfexample.views.SurfExampleClientView;
import query.MyQueryMaker;

public class SearchEventManager {

	String exceptionName;
	String searchQuery;
	String selectedText;
	String contextCode;
	int selectedLine;
	
	public SearchEventManager()
	{
		//default constructor
	}
	
	public void performSearch()
	{
		//code for performing the search
		try{
			this.collectTextSelection();
			this.collectContextCode();
			System.out.println("Selected target line:"+this.selectedLine);
			MyQueryMaker maker=new MyQueryMaker(this.exceptionName, this.contextCode,this.selectedLine);
			this.searchQuery=maker.getGitHubSearchQuery();
			//redirecting call to client
			MyClient myclient=new MyClient(this.exceptionName, this.searchQuery, this.contextCode,this.selectedLine);
			ArrayList<Result> collectedResults=myclient.collect_search_results();
			this.populateResultsToIDE(collectedResults);
			
		}catch(Exception exc){
			//handle the exception
		}
	}
	
	
	protected void populateResultsToIDE(ArrayList<Result> results)
	{
		//code for populating results to IDE
		try
		{
			IWorkbenchPage page =(IWorkbenchPage)PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage();
			String viewID="ca.usask.cs.srlab.surfexample.views.SurfExampleClientView";
			IViewPart vpart=page.findView(viewID);
			SurfExampleClientView myview=(SurfExampleClientView)vpart;
			//System.out.println(myview.viewer.toString());
			ViewContentProvider viewContentProvider=new ViewContentProvider(results);
			myview.viewer.setContentProvider(viewContentProvider);
			//myview.viewer.setSorter(new TableColumnSorter());
			//myview.viewer.setInput(this.getvi);
			//showing the view
			PlatformUI.getWorkbench()
			.getActiveWorkbenchWindow()
			.getActivePage().showView(viewID);
		} catch (Exception exc) {
			// System.err.println(exc.getMessage());
			// System.err.println("Failed to update Eclipse view"+exc.getMessage());
			// exc.printStackTrace();
			try {
				// String
				// message="Failed to collect search results. Please try again.";
				if (results == null) {
					String message = "Failed to collect any result. Please try again.";
					showMessageBox(message);
				}
			} catch (NullPointerException e) {
			}
		}
	}
	
	protected void showMessageBox(String message)
	{
		//code for showing message box
		try
		{
		Shell shell=Display.getDefault().getShells()[0];
		MessageDialog.openInformation(shell, "Information", message);
		}catch(Exception exc){}
	}
	
	
	protected void collectTextSelection() {
		// code for collecting the selected exception
		try {
			IWorkbenchPage page = (IWorkbenchPage) PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage();
			ISelection selection = page.getSelection();
			if (selection instanceof TextSelection) {
				this.selectedText = ((TextSelection) selection).getText();
			}
			// extracting the exception
//			this.exceptionName = RegexMatcher
//					.extractExceptionName(this.selectedText);
			String[] parts=this.selectedText.split("\\s+");
			for(String term:parts){
				if(term.endsWith("Exception")){
					this.exceptionName=term.trim();
					break;
				}
			}
		} catch (Exception exc) {
			// handle the exception
		}
	}
	
	protected void collectContextCode() {
		// code for collecting the context code
		try {
			IWorkbenchPage page = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage();
			ITextEditor editor = (ITextEditor) page.getActiveEditor();
			
			ISelection selection=page.getSelection();
			int offset=0;
			if(selection instanceof TextSelection){
				//selection offset
				offset=((TextSelection) selection).getOffset();
			}
			//extracting code fragment
			IDocument doc = editor.getDocumentProvider().getDocument(
					editor.getEditorInput());
			int lineNumber=doc.getLineOfOffset(offset);
			//get selected line number
			this.selectedLine=lineNumber;
			String fileContent = doc.get();
			this.contextCode = fileContent;
		} catch (Exception exc) {
			System.err.println(exc.getMessage());
		}
	}
	
	
	
	
	
}
