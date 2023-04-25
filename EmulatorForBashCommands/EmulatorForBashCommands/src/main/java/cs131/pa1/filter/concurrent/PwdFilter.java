package cs131.pa1.filter.concurrent;

import cs131.pa1.filter.concurrent.ConcurrentFilter;
import cs131.pa1.filter.concurrent.ConcurrentREPL;

/**
 * The filter for pwd command
 *
 */
public class PwdFilter extends ConcurrentFilter {
	public PwdFilter() {
		super();
	}
	
	public void process() {
		output.add(processLine(""));
	}
	
	public String processLine(String line) {
		return ConcurrentREPL.currentWorkingDirectory;
	}

	@Override
	public void run() {
		process();
		output.add(POISONPILL);// tell the printer filter, the process is done
		
	}
}
