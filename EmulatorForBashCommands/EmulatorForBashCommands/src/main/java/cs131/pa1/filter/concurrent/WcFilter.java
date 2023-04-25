package cs131.pa1.filter.concurrent;

import cs131.pa1.filter.concurrent.ConcurrentFilter;

/**
 * The filter for wc command
 *
 */
public class WcFilter extends ConcurrentFilter {
	/**
	 * The count of lines found
	 */
	private int linecount;
	/**
	 * The count of words found
	 */
	private int wordcount;
	/**
	 * The count of characters found
	 */
	private int charcount;
	
	public WcFilter() {
		super();
	}
	
	public void process() {
		String line = null;
		String processedLine= null;
		while (true) {
			try {
				line = input.take();
			} catch (InterruptedException e) {
				interruptControl = true;
			}
			if (line!= null && line.equals(POISONPILL)) {
				processedLine = processLine(null);
				output.add(processedLine);
				return;
			} else if (line!= null) {
				processedLine = processLine(line);
			}
		}
	}
	/**
	 * Counts the number of lines, words and characters from the input queue
	 * @param line the line as got from the input queue
	 * @return the number of lines, words, and characters when finished, null otherwise
	 */
	public String processLine(String line) {
		//prints current result if ever passed a null
		if(line == null) {
			return linecount + " " + wordcount + " " + charcount;
		} 
		
		if(isDone()) {
			String[] wct = line.split(" ");
			wordcount += wct.length;
			String[] cct = line.split("|");
			charcount += cct.length;
			return ++linecount + " " + wordcount + " " + charcount;
		} else {
			linecount++;
			String[] wct = line.split(" ");
			wordcount += wct.length;
			String[] cct = line.split("|");
			charcount += cct.length;
			return null;
		}
	}

	@Override
	public void run() {
		process();
		try {
			output.put(POISONPILL);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
}
