package ca.usask.cs.srlab.surfexample.views;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import core.CodeDisplayManager;
import core.Result;
import ca.usask.cs.srlab.surfexample.handlers.ViewContentProvider;

public class SurfExampleClientView extends ViewPart{

	
	public TableViewer viewer;
	public static final String ID = "ca.usask.cs.srlab.surfexample.views.SurfExampleClientView";
	
	public SurfExampleClientView()
	{
		//default handler
	}
	
	@Override
	public void createPartControl(Composite parent) {
		
		//result panel display 
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
		final Table table=viewer.getTable();
		table.setLayoutData(gridData);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		//viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
		String[] columns={"Matched API Objects","Score","Structural Relevance", "Content Relevance", "Handler Quality"};
		int[] colWidth={600,200,200,200, 200};
		int[] colAlignment={SWT.LEFT,SWT.LEFT, SWT.LEFT,SWT.LEFT,SWT.LEFT};
		for(int i=0;i<columns.length;i++){
			//stored for sorting
			final int columnNum=i;
			TableColumn col = new TableColumn(table, colAlignment[i]);
			col.setText(columns[i]);
			col.setWidth(colWidth[i]);
		}
		
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setInput(getViewSite());
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			
			@Override
			public void doubleClick(DoubleClickEvent event) {
				// TODO Auto-generated method stub
				IStructuredSelection selection=(IStructuredSelection) event.getSelection();
				Result result =(Result)selection.getFirstElement();
				//System.out.println(result.completeCode);
				CodeDisplayManager manager=new CodeDisplayManager(result.rank, result.completeCode);
				manager.displayCodeInEditor();
			}
		});
		
		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "ca.usask.cs.srlab.SurfExample.viewer");
	}
	
	
	
	class ViewLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			Result myresult = (Result) obj;
			switch (index) {
			case 0:
				if (myresult.matchedKeyWords != null)
					return myresult.matchedKeyWords;
				return "";
			case 1:
				if (myresult.totalScore > 0)
					return myresult.totalScore + "";
				return "0";
			case 2:
				if (myresult.structural_relevance > 0)
					return myresult.structural_relevance + "";
				return "0";
			case 3:
				if (myresult.content_relevance > 0)
					return myresult.content_relevance + "";
				return "0";
			case 4:
				if (myresult.handler_quality > 0)
					return myresult.handler_quality + "";
				return "0";
			default:
				return "";
			}
		}
		public Image getColumnImage(Object obj, int index) {
			Image image=null;
			if(index==0){
				image=getImage(obj);
			}
			return image;
		}
		public Image getImage(Object obj) {
			return ImageDescriptor.createFromFile(ViewLabelProvider.class, "code.png").createImage();
		}
	}
	
	

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

}
