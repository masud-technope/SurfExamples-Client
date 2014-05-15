package visitor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class CodeFragment implements Serializable {
	
	public int FragmentID;
	public String ExceptionName;
	public String htmlFileURL=new String();
	public String rawFileURL=new String();
	public String sourceFileID=new String();
	
	public String HandlingCode;
	public String CompleteCode=new String();
	public double GitHubScore=0;
	
	public HashMap<String,CodeObject> codeObjectMap;
	public ArrayList<Dependence> dependencies;
	public ArrayList<CustomCatchClause> handlers;
	
	public CodeFragment()
	{
		//initialization 
		codeObjectMap=new HashMap<>();
		dependencies=new ArrayList<>();
		handlers=new ArrayList<>();
	}
	
	//scores
	//lexical scores
	public double LexicalSimilarityScore=0;
	public double CosineSimilarityScore=0;
	public double CodeCloneScore=0;
	
	//structural scores
	public double StructuralSimilarityScore=0;
	public double MethodMatchScore=0;
	public double FieldMatchScore=0;
	public double CodeObjectMatchScore=0;
	public double DependencyMatchScore=0;
	
	//code example quality
	public double ReadabilityScore=0;
	
	//handler quality estimates
	public double StatementCountScore=0;
	public double HandlerCountScore=0;
	public double HandlerToCodeRatio=0;
	public double HandlerQualityScore=0;
	
	//total scores
	public double total_lexical_structural_score=0;
	public double total_lexical_readability_score=0;
	public double total_lexical_handlerquality_score=0;
	public double total_structural_readability_score=0;
	public double total_structural_handlerquality_score=0;
	public double total_lexical_structural_readability_score=0;
	public double total_lexical_structural_handlerquality_score=0;
	public double total_lexical_structural_readability_handlerquality_score=0;
	
	//scope in the document
	public int beginLine;
	public int endLine;
	
	
	
}
