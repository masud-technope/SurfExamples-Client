package visitor;

import java.io.Serializable;
import java.util.HashSet;

public class CodeObject implements Serializable{

	public String className;
	public String canonicalClassName;
	public HashSet<String> fields;
	public HashSet<String> methods;
	
	public CodeObject(String className)
	{
		//initialization
		this.className=className;
		this.fields=new HashSet<>();
		this.methods=new HashSet<>();
	}
	
}
