package extra;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import style.JavaLineStyler;

public class StyledTextTest {

	public static void main(String[] args){
	
		Display display=new Display();// Display.getDefault();
		Shell shell=new Shell(display); //display.getActiveShell();
		shell.setLayout(new FillLayout());
		shell.setText("SourceViewer Test");
		
		String content="Display display=new Display();\n"+
		"Shell shell=new Shell(display);\n"+
		"shell.setLayout(new FillLayout());\n"+
		"shell.setText(\"SourceViewer Test\");";
		
		
		StyledText editor=new StyledText(shell, SWT.MULTI |SWT.BORDER |SWT.V_SCROLL);
		editor.addLineStyleListener(new JavaLineStyler());
		editor.setText(content);
		
		editor.setLineBackground(0, 4, Display.getDefault().getSystemColor(SWT.COLOR_YELLOW));
	
		
		 shell.open();
	        while(!shell.isDisposed()){
	        	if(!display.readAndDispatch()){
	        		display.sleep();
	        	}
	        }
			display.dispose();
		
		
		
	}
	
}
