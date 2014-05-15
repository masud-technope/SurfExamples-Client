package visitor;

import java.io.Serializable;



public class Dependence implements Serializable {
	
	//dependency relations
	public CodeObject fromObject;
	public CodeObject destObject;
	public String dependenceName;
}
