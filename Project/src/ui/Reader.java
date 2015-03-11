package ui;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

import ui.exception.ExitUseCaseException;

public class Reader {
	private Scanner scan;

	Reader() {
		setScan(new Scanner(System.in));
	}

	private void setScan(Scanner scan) {
		this.scan = scan;
	}

	private Scanner getScan() {
		return scan;
	}

	private String getData() throws ExitUseCaseException {
		System.out.println("Type 0 to skip/return to main menu.");
		String str = getScan().nextLine();
		if (str.equals("0"))
			throw new ExitUseCaseException("Exit signal caught.");
		return str;
	}

	void close() {
		getScan().close();
	}

	<T> T select(List<T> options) throws ExitUseCaseException {
		while (true) {
			System.out.println("select one:");
			try {
				return options.get(Integer.parseInt(getData()) - 1);
			} catch (java.lang.IndexOutOfBoundsException e) {
				System.out.println(e.getMessage());
			} catch (java.lang.NumberFormatException e) {
				System.out.println("Give an integer");
			}
		}
	}

	String getString(String querry) throws ExitUseCaseException {
		System.out.println(querry + ":");
		return getData();
	}

	boolean getBoolean(String querry) throws ExitUseCaseException {
		while (true) {
			System.out.println(querry + " (y/n)");
			switch (getData()) {
			case "Y":
			case "y":
			case "yes":
			case "Yes":
				return true;
			case "n":
			case "N":
			case "no":
			case "No":
				return false;
			default:
				System.out.println("Invalid answer, try again.");
				break;
			}
		}
	}

	double getDouble(String querry) throws ExitUseCaseException {
		while (true) {
			System.out.println(querry + " (double)");
			try {
				return Double.parseDouble(getData());
			} catch (java.lang.NumberFormatException e) {
				System.out.println("Give a double");
			}
		}
	}

	Duration getDuration(String querry) throws ExitUseCaseException {
		while (true) {
			System.out.println(querry + " (hours)");
			try {
				return Duration.ofHours(Integer.parseInt(getData()));
			} catch (java.lang.NumberFormatException e) {
				System.out.println("Give an integer");
			}
		}
	}

	LocalDateTime getDate(String querry) throws ExitUseCaseException {
		while (true) {
			System.out.println(querry + "\n(format: 'yyyy-mm-ddThh:mm:ss')\n"
					+ "(type 1 for 09/02/2015, 08:00)");
			try {
				String answer = getData();
				if (answer.equals("1"))
					return LocalDateTime.of(2015, 2, 9, 8, 0);
				return LocalDateTime.parse(answer);
			} catch (java.time.format.DateTimeParseException e) {
				System.out.println("The given date was invalid, try again.");
			}
		}
	}
}