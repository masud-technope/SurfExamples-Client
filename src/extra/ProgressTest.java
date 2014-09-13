package extra;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;

public class ProgressTest {
	
	public static void main(String[] args){
		Action action=new Action() {
			
		};
		run(action);
		
	}

	protected static void run(Action action)
	{
		Job job = new Job("Test Job") {
			protected IStatus run(IProgressMonitor monitor) {
				// Set total number of work units
				monitor.beginTask("start task", 100);
 
				for (int i = 0; i < 10; i++) {
					try {
						Thread.sleep(5000);
						monitor.subTask("doing " + i);
						// Report that 10 units are done
						monitor.worked(10);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
				return Status.OK_STATUS;
			}
		};

		job.schedule();
	}
}
