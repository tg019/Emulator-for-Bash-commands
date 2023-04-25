package cs131.pa1.filter.concurrent;

import cs131.pa1.filter.Message;

import java.util.LinkedList;
import java.util.Scanner;

/**
 * The main implementation of the REPL loop (read-eval-print loop).
 * It reads commands from the user, parses them, executes them and displays the result.
 *
 */
public class ConcurrentREPL {
	/**
	 * the path of the current working directory
	 */
	static String currentWorkingDirectory;
	/**
	 * The main method that will execute the REPL loop
	 * @param args not used
	 */
	public static void main(String[] args){
		currentWorkingDirectory = System.getProperty("user.dir");
		Scanner s = new Scanner(System.in);
		System.out.print(Message.WELCOME);
		String command;
		LinkedList<BackgroundHelper> backgroundHelper = new LinkedList<BackgroundHelper>();
		int counter = 1;
		boolean control = true;
		
		while (control) {
			System.out.print(Message.NEWCOMMAND);
			//user enters command
			command = s.nextLine();
			if(command.equals("exit")) {
				control = false;
				
			// kill
			} else if (command.trim().startsWith("kill")) {
				String[] commands = command.trim().split(" ");
				if (commands.length == 1) {
					System.out.print(Message.REQUIRES_PARAMETER.with_parameter(command));
				} else if (commands.length > 2 || !isInteger(commands[1])) { //check for valid parameter
					System.out.print(Message.INVALID_PARAMETER.with_parameter(command));	
				} else {
					int killIndex = Integer.parseInt(commands[1]);
					if (killIndex > backgroundHelper.size()) {
						System.out.print(Message.INVALID_PARAMETER.with_parameter(command));
					} else {
						BackgroundHelper current = backgroundHelper.get(killIndex-1);
						current.getPrevThread().interrupt();
						backgroundHelper.remove(current);
					}	
				}
				//prints out background threads that are alive
			} else if (command.trim().equals("repl_jobs")) {
				for (BackgroundHelper backgroundCommand : backgroundHelper) {
					if (backgroundCommand.getPrevThread().isAlive()) {
						System.out.println("\t" + backgroundCommand.getCommand());
					}
				}
			} else if(!command.trim().equals("")) {
				//backgroundControl is true if there is an Ampersand, false if not
				boolean backgroundControl = hasBackgroundThread(command);
				//list of filters created
				ConcurrentFilter filterlist = ConcurrentCommandBuilder.createFiltersFromCommand(command);
				if (filterlist != null) {
					while (filterlist != null) {
						Thread currentThread = new Thread(filterlist, command);
						if (filterlist.getNext() == null && !backgroundControl) {
							try {
								currentThread.start();
								currentThread.join(); //join because at the end of input
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						} else {
							currentThread.start();
							if (filterlist.getNext() == null && backgroundControl) {
								backgroundHelper.add(new BackgroundHelper(currentThread, (counter++)+". "+command));
							}
						}
						filterlist = (ConcurrentFilter) filterlist.getNext();
					}
				}
			}
		}
		s.close();
		System.out.print(Message.GOODBYE);
	}	
	
	/**
	 * Returns if a command line will have background threads
	 * @param command, type String
	 * @return true if Ampersand is at the end of the command, false otherwise
	 */
	public static boolean hasBackgroundThread(String command) {
		if (command.charAt(command.length() - 1) == '&') {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns true if integer, false if not
	 * @param string, type String
	 * @return - true if integer, false if not
	 */
	public static boolean isInteger(String string) {
	    try { 
	        Integer.parseInt(string); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } 
	    return true;
	}

}