package ca.usask.cs.srlab.surfexample.handlers;

import java.util.ArrayList;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import query.MyQueryMaker;
import core.SearchEventManager;

public class SurfExampleHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		System.out.println("I am clicked");
		//perform the search
		try{
			SearchEventManager manager = new SearchEventManager(true);
			MyQueryMaker qmaker = new MyQueryMaker(manager.exceptionName,
					manager.contextCode, manager.selectedLine);
			ArrayList<String> queries=qmaker.getRecommendedQueries();
			if(queries.size()>0){
				String searchQuery=queries.get(0);
				manager=new SearchEventManager(searchQuery);
				//performing the search
				manager.performSearch();
			}
		}catch(Exception exc){
			//handle the exception
		}
		return null;
	}
}
