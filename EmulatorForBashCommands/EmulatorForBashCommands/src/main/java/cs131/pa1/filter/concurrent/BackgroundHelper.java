package cs131.pa1.filter.concurrent;

/**
 * This is a helper class. It stores a given command and its prev Thread. 
 * It is used to more easily print background threads and kill a specific thread.
 * 
 * @author Theo
 * email: tgolob@brandeis.edu
 */
public class BackgroundHelper {
	
	/**
	 * Command, type String
	 */
	private String command;
	
	
	/**
	 * prevThread, type Thread
	 */
	private Thread prevThread;
	
	/**
	 * Constructor for Background Helper
	 * @param prevThread, type Thread
	 * @param command, type String
	 */
	public BackgroundHelper(Thread prevThread, String command) {
		this.command = command;
		this.prevThread = prevThread;
	}
	
	/**
	 * Returns command line
	 * @return command field, type String
	 */
	public String getCommand() {
		return command;
	}
	
	/**
	 * Returns previous Thread
	 * @return prevThread, type Thread
	 */
	public Thread getPrevThread() {
		return prevThread;
	}
	

}
