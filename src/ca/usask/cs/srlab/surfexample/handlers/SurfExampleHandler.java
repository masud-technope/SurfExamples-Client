package ca.usask.cs.srlab.surfexample.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import core.SearchEventManager;

public class SurfExampleHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		System.out.println("I am clicked");
		//perform the search
		try{
			SearchEventManager manager=new SearchEventManager();
			manager.performSearch();
		}catch(Exception exc){
			//handle the exception
		}
		return null;
	}
	
	
}
