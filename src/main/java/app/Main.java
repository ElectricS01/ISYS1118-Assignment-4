package app;

import app.Lib.Documents.Passport;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
	public static void main(String[] args) {
		Person person = new Person();
		System.out.println(person.addPerson("22##abcdEF", "John", "Doe", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990"));
		printCSV();
		System.out.println(person.addID(new Passport("AB345678", "22##abcdEF", "John Doe", "15-11-1990", "Australia", "01-01-2020", "01-01-2030", "State")));
		printCSV();
		System.out.println(person.updatePersonDetails("22##abcdEF", "John", "Doe", "99|New Street|Melbourne|Victoria|Australia", "15-11-1990"));
		printCSV();
	}

	private static void printCSV() {
		try {
			java.util.List<String> lines = Files.readAllLines(Path.of("people.csv"));
			for (int i = 1; i < lines.size(); i++) {
				System.out.println(lines.get(i));
			}
		} catch (IOException e) {
			System.out.println("An error occurred: " + e);
		}
	}
}
