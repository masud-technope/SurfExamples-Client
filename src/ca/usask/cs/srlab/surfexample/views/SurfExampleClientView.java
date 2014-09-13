package ca.usask.cs.srlab.surfexample.views;

import java.util.ArrayList;
import java.util.HashSet;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.bindings.keys.ParseException;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.SimpleContentProposalProvider;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import ca.usask.cs.srlab.surfexample.handlers.ViewContentProvider;
import query.MyQueryMaker;
import style.JavaLineStyler;
import core.CodeDisplayManager;
import core.Result;
import core.SearchEventManager;

public class SurfExampleClientView extends ViewPart {

	public TableViewer viewer;
	public static final String ID = "ca.usask.cs.srlab.surfexample.views.SurfExampleClientView";
	public Text input = null;
	GridLayout gridLayout = null;
	Button associateContext;
	StyledText codeViewer = null;
	SourceViewer sourceViewer = null;
	ArrayList<String> suggestions = new ArrayList<>();
	ContentProposalAdapter adapter = null;
	ArrayList<String> queryTokenList = new ArrayList<>();
	final int TEXT_MARGIN = 3;

	final Display currDisplay = Display.getCurrent();
	final TextLayout textLayout = new TextLayout(currDisplay);
	Font font1 = new Font(currDisplay, "Arial", 10, SWT.BOLD);
	Font font2 = new Font(currDisplay, "Arial", 10, SWT.NORMAL);
	Font font3 = new Font(currDisplay, "Arial", 10, SWT.NORMAL);
	Color blue = currDisplay.getSystemColor(SWT.COLOR_BLUE);
	Color green = currDisplay.getSystemColor(SWT.COLOR_DARK_GREEN);
	Color gray = currDisplay.getSystemColor(SWT.COLOR_DARK_GRAY);
	Color selected = currDisplay.getSystemColor(SWT.COLOR_YELLOW);
	
	TextStyle style1 = new TextStyle(font1, green, null);

	// TextStyle style2 = new TextStyle(font2, green, null);
	// TextStyle style3 = new TextStyle(font3, gray, null);

	public SurfExampleClientView() {
		// default handler
	}

	protected void addSearchPanel(Composite parent) {
		// adding the search panel
		final Composite composite = new Composite(parent, SWT.NONE);
		gridLayout = new GridLayout(4, false);
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 10;
		gridLayout.verticalSpacing = 5;
		gridLayout.horizontalSpacing = 5;
		composite.setLayout(gridLayout);

		GridData gridData = new GridData(SWT.CENTER, SWT.FILL, true, false);
		composite.setLayoutData(gridData);

		// gridData = new GridData(SWT.DEFAULT, SWT.FILL, false, false);
		GridData gdata2 = new GridData();
		gdata2.heightHint = 25;
		gdata2.widthHint = 600;
		gdata2.horizontalAlignment = SWT.BEGINNING;
		gdata2.verticalAlignment = SWT.CENTER;
		gdata2.grabExcessHorizontalSpace = false;

		Label keywordlabel = new Label(composite, SWT.NONE);
		keywordlabel.setText("Keywords: ");
		keywordlabel.setFont(new Font(composite.getDisplay(), "Arial", 11,
				SWT.BOLD));

		input = new Text(composite, SWT.SINGLE | SWT.BORDER);
		input.setEditable(true);
		input.setToolTipText("Press Ctrl+Space to Check Suggested Queries");
		Font myfont = new Font(composite.getDisplay(), "Arial", 11, SWT.NORMAL);
		input.setFont(myfont);
		input.setLayoutData(gdata2);
		FocusListener focusListener = new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				if (suggestions.size() > 0) {
					System.out.println("Input box got the focus");
					String[] proposals = suggestions
							.toArray(new String[suggestions.size()]);
					if (proposals.length > 0) {
						try {
							// ContentProposalAdapter adapter = null;
							SimpleContentProposalProvider scp = new SimpleContentProposalProvider(
									proposals);

							// setting filtering
							scp.setFiltering(true);
							String autoactive = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
							KeyStroke ks = KeyStroke.getInstance("Ctrl+Space");
							adapter = new ContentProposalAdapter(input,
									new TextContentAdapter(), scp, ks,
									autoactive.toCharArray());
							adapter.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_REPLACE);
						} catch (ParseException exc) {

						}
					}
				}
			}
		};
		if (!input.isListening(SWT.FOCUSED)) {
			input.addFocusListener(focusListener);
		}

		GridData gdata3 = new GridData();
		gdata3.heightHint = 30;
		gdata3.widthHint = 90;
		gdata3.horizontalAlignment = SWT.BEGINNING;
		// gdata2.grabExcessHorizontalSpace=true;

		Button searchButton = new Button(composite, SWT.PUSH);
		searchButton.setText("Search");
		searchButton.setToolTipText("Search with ExcClipse");
		searchButton.setFont(new Font(composite.getDisplay(), "Arial", 10,
				SWT.BOLD));
		searchButton.setImage(get_search_image());
		// System.out.println("Search Icon:"+Display.getDefault().getSystemImage(SWT.ICON_SEARCH));
		searchButton.setLayoutData(gdata3);
		// adding listener to search button
		searchButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				String query = input.getText();
				if (!query.trim().isEmpty()) {
					tokenizeQuery(query);
					SearchEventManager manager = new SearchEventManager(query);
					manager.performSearch();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		GridData gdata4 = new GridData();
		gdata4.heightHint = 30;
		gdata4.widthHint = 120;
		gdata4.horizontalAlignment = SWT.BEGINNING;

		Button refreshButton = new Button(composite, SWT.PUSH);
		refreshButton.setText("Get Queries");
		refreshButton.setToolTipText("Get Query Suggestions");
		refreshButton.setFont(new Font(parent.getDisplay(), "Arial", 10,
				SWT.BOLD));
		refreshButton.setImage(getRefreshImage());
		refreshButton.setLayoutData(gdata4);
		refreshButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				input.setText("");
				// populate the suggestion
				SearchEventManager manager = new SearchEventManager(true);
				MyQueryMaker qmaker = new MyQueryMaker(manager.exceptionName,
						manager.contextCode, manager.selectedLine);
				suggestions = qmaker.getRecommendedQueries();
				System.out.println("Queries collected:" + suggestions);
				input.setFocus();
				executeKeyEvent();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
		});
	}

	protected void executeKeyEvent() {
		try {
			Thread.sleep(10);
			Event event = new Event();
			event.type = SWT.KeyDown;
			event.stateMask = SWT.CTRL;
			event.keyCode = SWT.SPACE;
			// Display.getCurrent().post(event);
			input.notifyListeners(SWT.KeyDown, event);
			System.out.println("Event passed successfully.");

		} catch (Exception exc) {
			// handle the exception
		}

	}

	protected Image getRefreshImage() {
		return ImageDescriptor.createFromFile(SurfExampleClientView.class,
				"refresh.png").createImage();
	}

	protected Image get_search_image() {
		return ImageDescriptor.createFromFile(ViewLabelProvider.class,
				"searchbt16.gif").createImage();
	}

	protected void addResultTable(Composite parent) {
		// adding the result table
		// result panel display

		final Composite composite3 = new Composite(parent, SWT.NONE);
		GridLayout gridLayout2 = new GridLayout(2, false);
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		gridLayout.verticalSpacing = 3;
		gridLayout.horizontalSpacing = 0;
		composite3.setLayout(gridLayout2);

		GridData gridData2 = new GridData(SWT.FILL, SWT.FILL, true, true);
		composite3.setLayoutData(gridData2);

		final SashForm divider = new SashForm(composite3, SWT.HORIZONTAL
				| SWT.BORDER);
		divider.setLayout(gridLayout2);
		divider.setLayoutData(gridData2);

		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		viewer = new TableViewer(divider, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
		final Table table = viewer.getTable();
		table.setLayoutData(gridData);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		// viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL |
		// SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
		// String[]
		// columns={"Matched API Classes","Relevance","Structural Relevance",
		// "Content Relevance", "Handler Quality"};
		String[] columns = { "Matched API Classes", "Relevance" };
		int[] colWidth = { 600, 200 };
		int[] colAlignment = { SWT.LEFT, SWT.LEFT };
		for (int i = 0; i < columns.length; i++) {
			// stored for sorting
			final int columnNum = i;
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
				IStructuredSelection selection = (IStructuredSelection) event
						.getSelection();
				Result result = (Result) selection.getFirstElement();
				// System.out.println(result.completeCode);
				CodeDisplayManager manager = new CodeDisplayManager(
						result.rank, result.completeCode);
				manager.displayCodeInEditor();
			}
		});

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				// TODO Auto-generated method stub
				try{
				IStructuredSelection selection = (IStructuredSelection) event
						.getSelection();
				if(selection==null)return;
				Result result = (Result) selection.toList().get(0);
				codeViewer.setText(result.completeCode);
				selectHighlightCodeViewer();
				// transferring focus
				codeViewer.setFocus();
				}catch(Exception exc){
					//sometimes you have to swallow it.
				}
			}
		});

		// now add the editor
		// Composite composite4=new Composite(divider, SWT.NONE);
		codeViewer = new StyledText(divider, SWT.BORDER | SWT.READ_ONLY
				| SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		codeViewer.setFont(new Font(divider.getDisplay(), "Courier New", 10,
				SWT.NORMAL));
		codeViewer.addLineStyleListener(new JavaLineStyler());
		codeViewer.setMargins(0, 10,0,0);
	
		// adding the paint item
		setItemHeight(table);
		setPaintItem(table);
	}

	protected void selectHighlightCodeViewer() {
		// select and highlight code viewer
		try {
			String query = input.getText().trim();
			String[] queryTokens = query.split("\\s+");
			String content = codeViewer.getText();
			String[] lines = content.split("\n");
			ArrayList<Integer> linecol = new ArrayList<>();
			boolean handlerStarted=false;
			
			for (int i = 0; i < lines.length; i++) {
				String line = lines[i];
				
				//exception handler continuing
				if(handlerStarted){
					linecol.add(i);
					if(line.trim().equals("}") || line.trim().startsWith("}")){
						handlerStarted=false;
					}
				}
				for (String token : queryTokens) {
					if (line.contains(token)) {
						if(token.endsWith("Exception")&& !line.contains("throws") && line.trim().endsWith("{")){  //exception token
							//handler started
							handlerStarted=true;
							linecol.add(i);
						}
						else{
							linecol.add(i);
						}
						break;
					}
				}
			}
			//setting line background color
			for(int index:linecol){
				codeViewer.setLineBackground(index, 1, selected);
			}
			} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected ArrayList<Integer> getItemIndices(String item) {
		// getting item indices
		ArrayList<Integer> indices = new ArrayList<>();
		String code = codeViewer.getText();
		int start = 0;
		while (start < code.length()) {
			int index = code.indexOf(item, start);
			if (index > 0) {
				indices.add(index);
				start += item.length();
			} else
				break;
		}
		return indices;
	}

	protected void tokenizeQuery(String query) {
		// Tokenize the query
		this.queryTokenList.clear();
		String[] words = query.split("\\s+");
		for (String word : words) {
			String[] parts = StringUtils.splitByCharacterTypeCamelCase(word);
			int count = 0;
			while (count < parts.length) {
				if (!this.queryTokenList.contains(parts[count])) {
					this.queryTokenList.add(parts[count]);
				}
				count++;
			}
		}
	}

	protected void setItemHeight(Table table) {
		table.addListener(SWT.MeasureItem, new Listener() {
			@Override
			public void handleEvent(Event event) {
				// TODO Auto-generated method stub
				TableItem item = (TableItem) event.item;
				String text = item.getText(event.index);
				Point size = event.gc.textExtent(text);
				event.width = size.x + 2 * TEXT_MARGIN;
				event.height = 35;// Math.max(min, size.y + TEXT_MARGIN);
			}
		});
		table.addListener(SWT.EraseItem, new Listener() {

			@Override
			public void handleEvent(Event event) {
				// TODO Auto-generated method stub
				event.detail &= ~SWT.FOREGROUND;
			}
		});
	}

	protected void setPaintItem(Table table) {
		// adding paint item
		table.addListener(SWT.PaintItem, new Listener() {
			@Override
			public void handleEvent(Event event) {
				// TODO Auto-generated method stub
				if (event.index == 0) {
					// adding layout to code example title
					int index = event.index;
					TableItem item = (TableItem) event.item;
					String title = item.getText(index);
					textLayout.setText(title);
					// textLayout.setStyle(style1, 0, title.length());
					for (String token : queryTokenList) {
						int start = title.indexOf(token);
						if (start >= 0) {
							int end = start + token.length() - 1;
							if (start >= 0 && end > 0)
								textLayout.setStyle(style1, start, end);
							// break;
						}
					}
					textLayout.draw(event.gc, event.x, event.y);
				} else if (event.index == 1) {
					GC gc = event.gc;
					int index = event.index;
					TableItem item = (TableItem) event.item;
					int percent = (int) Double.parseDouble(item.getText(index));
					Color foreground = gc.getForeground();
					Color background = gc.getBackground();
					// gc.setForeground(new Color(null, 11, 59, 23));
					Color myforeground = new Color(null, 11, 97, 11);
					gc.setForeground(myforeground);
					gc.setBackground(new Color(null, 255, 255, 255));
					int col2Width = 100;
					int width = (col2Width - 1) * percent / 100;
					int height = 25;
					// gc.fillRectangle(event.x, event.y + 10, width,
					// height);
					gc.fillGradientRectangle(event.x, event.y, width, height,
							false);
					Rectangle rect2 = new Rectangle(event.x, event.y,
							width - 1, height - 1);
					gc.drawRectangle(rect2);
					gc.setForeground(new Color(null, 255, 255, 255));
					String text = percent + "%";
					Point size = event.gc.textExtent(text);
					int offset = Math.max(0, (height - size.y) / 2);
					gc.drawText(text, event.x + 2, event.y + offset, true);
					gc.setForeground(background);
					gc.setBackground(foreground);
				}
			}
		});

	}

	protected void addUtilityLayer(Composite parent) {
		// adding the utility layer
		final Composite composite2 = new Composite(parent, SWT.NONE);
		GridLayout gridLayout2 = new GridLayout(3, false);
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		gridLayout.verticalSpacing = 5;
		gridLayout.horizontalSpacing = 5;
		composite2.setLayout(gridLayout2);

		GridData gridData2 = new GridData(SWT.CENTER, SWT.FILL, true, false);
		composite2.setLayoutData(gridData2);

		// excclipseSuggest=new Button(composite2, SWT.CHECK);
		// excclipseSuggest.setText("ExcClipse Suggest");
		// googleSuggest=new Button(composite2, SWT.RADIO);
		// googleSuggest.setText("Google Suggest");

		Label blank = new Label(composite2, SWT.NONE);
		Label info = new Label(composite2, SWT.NONE);
		info.setText("Press Ctrl+Space to Check Suggested Queries.");

		// associateContext=new Button(composite2, SWT.CHECK);
		// final Button confirm = new Button(composite2, SWT.CHECK);
		// associateContext.setText("Associate context");
		final Button clearButton = new Button(composite2, SWT.CHECK);
		clearButton.setText("Reset search");

		// adding listener to clear button
		clearButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				try {
					input.setText("");
					viewer.setContentProvider(new ViewContentProvider());
					codeViewer.setText("");
				} catch (Exception exc3) {

				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
			}
		});

	}

	@Override
	public void createPartControl(Composite parent) {

		GridLayout glayout = new GridLayout();
		glayout.marginWidth = 15;
		glayout.marginHeight = 10;
		parent.setLayout(glayout);

		GridData gdata = new GridData(SWT.FILL, SWT.FILL, true, true);
		parent.setLayoutData(gdata);

		addSearchPanel(parent);
		addUtilityLayer(parent);
		addResultTable(parent);
		// Create the help context id for the viewer's control
		PlatformUI
				.getWorkbench()
				.getHelpSystem()
				.setHelp(viewer.getControl(),
						"ca.usask.cs.srlab.excclipse.viewer");
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
					return String.format("%.2f", myresult.totalScore * 100);
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
			// return null;
			Image image = null;
			if (index == 0) {
				image = getImage(obj);
			}
			return image;
		}

		public Image getImage(Object obj) {
			return ImageDescriptor.createFromFile(ViewLabelProvider.class,
					"code.png").createImage();
		}
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
