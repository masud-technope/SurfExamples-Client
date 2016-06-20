package extra;



import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.javaeditor.JavaSourceViewer;
import org.eclipse.jdt.ui.text.IJavaPartitions;
import org.eclipse.jdt.ui.text.JavaSourceViewerConfiguration;
import org.eclipse.jdt.ui.text.JavaTextTools;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.LineNumberRulerColumn;
import org.eclipse.jface.text.source.OverviewRuler;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.jface.text.source.VerticalRuler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.editors.text.TextEditor;

public class SourceViewerTest {
	
	@SuppressWarnings("restriction")
	public static void main(String[] args){
		
		Display display=new Display();
		Shell shell=new Shell(display);
		
		shell.setLayout(new FillLayout());
		shell.setText("SourceViewer Test");
		String startText="void execute(){" +
				"Display display=new Display();" +
				"}";
		shell.setSize(500, 400);
		
		//Composite composite=new Composite(shell, SWT.NONE);
		//composite.setLayout(new FillLayout());
		
		IPreferenceStore mystore=new PreferenceStore();
		JavaTextTools tools=new JavaTextTools(mystore);
		
//		@SuppressWarnings("restriction")
//		JavaTextTools tools=JavaPlugin.getDefault().getJavaTextTools();
//		@SuppressWarnings("restriction")
//		IPreferenceStore mystore=JavaPlugin.getDefault().getPreferenceStore();
		
		
		CompositeRuler compRuler=new CompositeRuler();
		LineNumberRulerColumn lineCol = new LineNumberRulerColumn();
	    lineCol.setBackground( display.getSystemColor( SWT.COLOR_GRAY ) );
	    lineCol.setForeground( display.getSystemColor( SWT.COLOR_BLUE ) );
	    compRuler.addDecorator( 0, lineCol );
	    
	    Document document=new Document(startText);
	    SourceViewer sv = new SourceViewer( shell, compRuler, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL );
	    SourceViewerConfiguration config=new JavaSourceViewerConfiguration(tools.getColorManager(), mystore, new CustomEditor(), IJavaPartitions.JAVA_PARTITIONING);
	    
	    VerticalRuler vruler=new VerticalRuler(10);
	    
	    
	    @SuppressWarnings("restriction")
		JavaSourceViewer sv2=new JavaSourceViewer(shell, vruler, null, false, SWT.NONE, mystore);
	    
	    //MySourceViewerConfiguration config=new MySourceViewerConfiguration();
	    //ContentAssistant assistant=(ContentAssistant) config.getContentAssistant(sv);
	    //IContentAssistant assistant=config.getContentAssistant(sv);
        
        //javaSourceViewer.setDocument(document);
	    sv2.configure(config);
        sv2.setDocument(document);
        
        shell.open();
        while(!shell.isDisposed()){
        	if(!display.readAndDispatch()){
        		display.sleep();
        	}
        }
		display.dispose();
		
	}
}
