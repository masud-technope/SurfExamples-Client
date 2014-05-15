package query;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import visitor.CodeFragment;
import visitor.CustomSourceVisitor;
import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;

public class InputDocProcessor {
	
	String contextCode;
	CodeFragment inputFragment;
	String exceptionName;
	int index=0;
	int selectedLine;
	
	public InputDocProcessor(String exceptionName, String contextCode, int selectedLine)
	{
		//initialization
		this.contextCode=contextCode;
		this.exceptionName=exceptionName;
		this.selectedLine=selectedLine;
		inputFragment=new CodeFragment();
	}
	
	public CodeFragment extractInputDocumentInfo()
	{
		//code for extracting structural info of input document
		try{
			CompilationUnit cu=JavaParser.parse(new ByteArrayInputStream(this.contextCode.getBytes()));
			if(cu==null){
				System.err.println("The code block is not compilable");
			}
			CustomSourceVisitor visitor=new CustomSourceVisitor(exceptionName);
			visitor.visit(cu, null);
			ArrayList<CodeFragment> extracted=visitor.getExtractedFragments();
			if(extracted.size()>0){
				for(CodeFragment cf:extracted){
					if(selectedLine>cf.beginLine && selectedLine<cf.endLine){
						this.inputFragment=cf;// visitor.Fragments.get(index); //getting the first method involving the exception
						break;
					}
				}
			}
		}catch(Exception exc){
			exc.printStackTrace();
		}
		return this.inputFragment;
	}
	
	
	
}
