package cs131.pa1.filter.concurrent;

import java.util.concurrent.LinkedBlockingQueue;

import cs131.pa1.filter.Filter;

/**
 * An abstract class that extends the Filter and implements the basic functionality of all filters. Each filter should
 * extend this class and implement functionality that is specific for that filter.
 *
 */
public abstract class ConcurrentFilter extends Filter implements Runnable{
	
	/**
	 * The input queue for this filter
	 */
	protected LinkedBlockingQueue<String> input;
	/**
	 * The output queue for this filter
	 */
	protected LinkedBlockingQueue<String> output;
	protected static final String POISONPILL = "THIS IS A POISON PILL"; //used to end threads
	protected boolean interruptControl; //internal interrupt identifier, false if not interrupted, true otherwise

	
	@Override
	public void setPrevFilter(Filter prevFilter) {
		prevFilter.setNextFilter(this);
	}
	
	@Override
	public void setNextFilter(Filter nextFilter) {
		if (nextFilter instanceof ConcurrentFilter){
			ConcurrentFilter sequentialNext = (ConcurrentFilter) nextFilter;
			this.next = sequentialNext;
			sequentialNext.prev = this;
			if (this.output == null){
				this. output= new LinkedBlockingQueue<String>();
			}
			sequentialNext.input = this.output;
		} else {
			throw new RuntimeException("Should not attempt to link dissimilar filter types.");
		}
	}
	
	/**
	 * Returns next filter
	 * @return next field, type Filter
	 */
	public Filter getNext() {
		return next;
	}
	
	
	/**
	 * processes input and pushes to output
	 */
	public void process(){
		while (!isDone()) {
			String line= null;
			try {
				if (input.peek() != null && !input.peek().equals(POISONPILL)) {
					line = input.take(); //will wait if necessary
				}
			} catch (InterruptedException e1) {
				e1.printStackTrace();
				interruptControl = true;
			}
			String processedLine = processLine(line);
			if (processedLine != null){
				try {
					output.put(processedLine);
				} catch (InterruptedException e) {
					e.printStackTrace();
					interruptControl = true;
				}
			}
		}	
	}	
	
	@Override
	public boolean isDone() {
		//found poison pill, filter is done
		if(input.peek()!= null &&input.peek().equals(POISONPILL) || interruptControl) {
			return true;
		}
		return false;
	}
	
	protected abstract String processLine(String line);
	
}
