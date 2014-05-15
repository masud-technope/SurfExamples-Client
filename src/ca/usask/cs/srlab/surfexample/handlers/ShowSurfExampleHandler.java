package ca.usask.cs.srlab.surfexample.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.core.commands.AbstractHandler;

public class ShowSurfExampleHandler extends AbstractHandler {
	
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// code for event handling on the menu item
		try {
			//code for showing SurfClipse View
			String SEviewID="ca.usask.cs.srlab.surfexample.views.SurfExampleClientView";
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(SEviewID);
			//String SCBviewID="ca.usask.ca.srlab.surfclipse.client.views.SurfClipseBrowser";
			//PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(SCBviewID);
			
			System.out.println("SurfExample windows shown successfully");
			
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		return null;
	}

}
