package cs131.pa1.filter.concurrent;

import cs131.pa1.filter.concurrent.ConcurrentFilter;

/**
 * The filter for printing in the console
 *
 */
public class PrintFilter extends ConcurrentFilter {
	public PrintFilter() {
		super();
		interruptControl = false;
	}
	
	public void process() {
		while(!isDone()) {
			try{
				if (input.peek() != null && !input.peek().equals(POISONPILL)) {
					String line = input.take();
					processLine(line);
				}
			}catch(InterruptedException e) {
				interruptControl = true;
			}
		}
	}
	
	public String processLine(String line) {
		System.out.println(line);
		return null;
	}

	@Override
	public void run() {
		process();
	}
}
