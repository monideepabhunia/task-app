import java.util.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;

public class Task {
	public static class Node {
		int priority;
		String task_name;
	}

	public class Func {
		// FIXME: copy non static ones in this class
	}

	public static ArrayList<Node> read_task() throws IOException {
		int ch;
		FileReader fr = null;
		try {
			fr = new FileReader("task.txt");
			String pendingText = "";
			int count = 0;
			while ((ch = fr.read()) != -1)
			// System.out.print((char) ch);
			{
				pendingText += (char) ch;
				count++;
			}
			if (count == 0) {
				ArrayList<Node> pending = new ArrayList<Node>();
				fr.close();
				return pending;
			}
			// System.out.println(pending);
			ArrayList<Node> pending = new ArrayList<Node>();
			String[] arrOfpending = pendingText.split("\n", pendingText.length());
			for (int i = 0; i < arrOfpending.length; i++) {
				Node temp = new Node();
				temp.priority = Integer.parseInt(String.valueOf(((arrOfpending[i]).charAt(0))));
				// temp.task_name = ((arrOfpending[i]);
				temp.task_name = arrOfpending[i].substring(2);// , arrOfpending[i].length());
				pending.add(temp);
			}
			fr.close();
			return pending;
		} catch (FileNotFoundException fe) {
			try {
				FileWriter fw = new FileWriter("task.txt");
				fw.close();
				System.out.println("File not found, so created one");
			} catch (IOException e) {
				System.out.println("File not found. cannot write file also with exception " + e);
			}
		}
		return null;

	}

	public static ArrayList<String> read_completed() throws IOException {
		int ch;
		FileReader fr = null;
		try {
			fr = new FileReader("completed.txt");
			String completedText = "";
			int count = 0;
			while ((ch = fr.read()) != -1) {
				completedText += (char) ch;
				count++;
			}

			if (count == 0) {
				ArrayList<String> completed = new ArrayList<String>();
				fr.close();
				return completed;
			}
			ArrayList<String> completed = new ArrayList<String>();
			String[] arrOfCompleted = completedText.split("\n", completedText.length());
			for (int i = 0; i < arrOfCompleted.length; i++) {
				completed.add(arrOfCompleted[i]);
			}
			fr.close();
			return completed;
		} catch (FileNotFoundException fe) {
			try {
				FileWriter fw = new FileWriter("completed.txt");
				fw.close();
				System.out.println("File not found, so created one");
			} catch (IOException e) {
				System.out.println("File not found. cannot write file also with exception " + e);
			}
		}
		return null;

	}

	public static void write_task(ArrayList<Node> pending) throws IOException {
		String pending_String = "";
		for (int i = 0; i < pending.size(); i++) {
			pending_String += String.valueOf((pending.get(i).priority)) + " ";
			pending_String += (pending.get(i).task_name);
			pending_String += "\n";
		}
		try {
			FileWriter fw = new FileWriter("task.txt");
			for (int i = 0; i < pending_String.length() - 1; i++)
				fw.write(pending_String.charAt(i));
			fw.close();

		} catch (IOException e) {
			System.out.println("File not found. cannot write file also with exception " + e);
		}
	}

	public static void write_completed(ArrayList<String> completed) throws IOException {
		String completed_String = "";
		for (int i = 0; i < completed.size(); i++) {
			completed_String += (completed.get(i));
			completed_String += "\n";
		}
		try {
			FileWriter fw = new FileWriter("completed.txt");
			for (int i = 0; i < completed_String.length() - 1; i++)
				fw.write(completed_String.charAt(i));
			fw.close();
		} catch (IOException e) {
			System.out.println("File not found. cannot write file also with exception " + e);
		}
	}

	public static void help() {
		System.out.println("Usage :-");
		System.out.println(
				"$ ./task add 2 hello world    # Add a new item with priority 2 and text \"hello world\" to the list");
		System.out.println(
				"$ ./task ls                   # Show incomplete priority list items sorted by priority in ascending order");
		System.out.println("$ ./task del INDEX            # Delete the incomplete item with the given index");
		System.out.println("$ ./task done INDEX           # Mark the incomplete item with the given index as complete");
		System.out.println("$ ./task help                 # Show usage");
		System.out.println("$ ./task report               # Statistics");
	}

	public static void add(ArrayList<Node> pending, String args[]) {
		if (args.length == 1 || args.length == 2 || args.length > 3) {
			System.out.println("Error: Missing tasks string. Nothing added!");
		} else {
			Node temp = new Node();
			temp.priority = Integer.parseInt(args[1]);
			temp.task_name = args[2];
			pending.add(temp);
			try {
				pending = pending_sort(pending);
				write_task(pending);
				// TODO: sort and set the arraylist
			} catch (IOException e) {
				System.out.println("Cannot write");
			}
			System.out.println("Added task: " + args[2] + " with priority " + args[1]);
		}
	}

	public static void del(ArrayList<Node> pending, String args[]) {

		if (args.length < 2) {
			System.out.println("Error: Missing NUMBER for deleting tasks.");
			return;
		}
		int index = Integer.parseInt(args[1]);
		int idx = index;
		index -= 1;
		int size = pending.size();
		if (index >= 0 && index < size) {
			pending.remove(index);
			try {
				write_task(pending);

				System.out.println("Deleted task #" + idx);
			} catch (IOException e) {
				System.out.println("Cannot write");
			}
		} else {
			System.out.println("Error: task with index #" + idx + " does not exist. Nothing deleted.");
		}

	}

	public static void done(ArrayList<Node> pending, ArrayList<String> completed, String args[]) {
		if (args.length < 2) {
			System.out.println("Error: Missing NUMBER for marking tasks as done.");
			return;
		}
		int index = Integer.parseInt(args[1]);
		int idx = index;
		index -= 1;
		if (index >= 0 && index < pending.size()) {

			Node temp = pending.get(index);
			pending.remove(index);
			completed.add(temp.task_name);
			try {
				write_task(pending);
				write_completed(completed);
				System.out.println("Marked item as done.");
			} catch (IOException e) {
				System.out.println("Cannot write");
			}
		} else {
			System.out.println("Error: no incomplete item with index #" + idx + " exists.");
		}
	}

	public static void ls(ArrayList<Node> pending) {
		if (pending.size() == 0) {
			System.out.println("There are no pending tasks!");
			return;
		}
		for (int i = 0; i < pending.size(); i++) {
			System.out.println((i + 1) + ". " + pending.get(i).task_name + " [" +
					pending.get(i).priority + "]");
		}
	}

	public static void report(ArrayList<Node> pending, ArrayList<String> completed) {

		System.out.println("Pending : " + pending.size());

		for (int i = 0; i < pending.size(); i++) {
			System.out.println((i + 1) + ". " + pending.get(i).task_name + " [" + pending.get(i).priority + "]");
		}
		System.out.println();
		System.out.println("Completed : " + completed.size());

		for (int i = 0; i < completed.size(); i++) {
			System.out.println(completed.get(i));
		}

	}

	public static ArrayList<Node> pending_sort(ArrayList<Node> pending) {
		ArrayList<Node> new_pending = new ArrayList<Node>();
		int size = pending.size();
		for (int j = 0; j < size; j++) {
			int min = pending.get(0).priority;
			for (int i = 0; i < pending.size(); i++) {
				if (pending.get(i).priority < min) {
					min = pending.get(i).priority;
				}
			}
			for (int i = 0; i < pending.size(); i++) {
				if (pending.get(i).priority == min) {
					new_pending.add(pending.get(i));
					pending.remove(i);
				}
			}
		}
		return new_pending;
	}

	public static void main(String[] args) {
		// ArrayList<Node> pending = new ArrayList<Node>();
		// ArrayList<String> completed = new ArrayList<String>();

		try {
			ArrayList<Node> pending = read_task();
			ArrayList<String> completed = read_completed();

			if (args.length == 0 || args[0].equals("help")) {
				help();
			} else if (args[0].equals("add")) {
				add(pending, args);
			} else if (args[0].equals("del")) {
				del(pending, args);
			} else if (args[0].equals("done")) {
				done(pending, completed, args);
			} else if (args[0].equals("ls")) {
				ls(pending);
			} else if (args[0].equals("report")) {
				report(pending, completed);
			}
		} catch (IOException e) {
			System.out.println("cannot read pending or completed\n" + e);
		}
	}
}
