package visitor;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.visitor.VoidVisitorAdapter;
import java.util.ArrayList;

public class ImportVisitor extends VoidVisitorAdapter {

	public ArrayList<String> imports;
	public ArrayList<String> fieldDecs;
	public ImportVisitor()
	{
		//initialization
		imports=new ArrayList<>();
		fieldDecs=new ArrayList<>();
	}
	
	@Override
	public void visit(ImportDeclaration impStmt, Object obj){
		imports.add(impStmt.toString());
	}
	
	@Override
	public void visit(FieldDeclaration fielddec, Object obj)
	{
		fieldDecs.add(fielddec.toString());
	}
	
	
}
