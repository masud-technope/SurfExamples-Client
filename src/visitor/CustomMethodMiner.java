package visitor;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.TypeDeclaration;

public class CustomMethodMiner {

	String fullCode;
	String contextCode;
	int cursorLine;

	public CustomMethodMiner(String fullCode, int targetLine) {
		// initializer
		this.fullCode = fullCode;
		this.contextCode = new String();
		this.cursorLine = targetLine;
	}

	public String getContextCode() {
		// collecting the context code
		try {
			CompilationUnit cu = JavaParser.parse(new ByteArrayInputStream(
					this.fullCode.getBytes()));
			if (cu != null) {
				List<TypeDeclaration> types = cu.getTypes();
				for (TypeDeclaration type : types) {
					List<BodyDeclaration> members = type.getMembers();
					ArrayList<BodyDeclaration> blacklist=new ArrayList<>();
					for (BodyDeclaration body : members) {
						if (body instanceof MethodDeclaration) {
							MethodDeclaration m = (MethodDeclaration) body;
							if (this.cursorLine > 0) {
								if (this.cursorLine < m.getBeginLine()
										|| this.cursorLine > m.getEndLine()) {
									// delete the particular method
									blacklist.add(body);
								}
							}	
						}
					}
					//removing irrelevant methods
					members.removeAll(blacklist);
				}
				// now collect the one method class
				this.contextCode = cu.toString();
				// updated context code
				System.out.println(this.contextCode);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// returning the context code
		return this.contextCode;
	}

}
