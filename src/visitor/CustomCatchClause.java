package visitor;
import java.io.Serializable;

public class CustomCatchClause implements Serializable {
	// the only final member
	public String exceptionParams;
	public String catchBlock;
	public CustomCatchClause(String exceptionParams, String catchBlock) {
		// initialization
		this.exceptionParams=exceptionParams;
		this.catchBlock=catchBlock;
	}
}
