package app;

import app.Lib.Documents.Passport;

public class Main {
	public static void main(String[] args) {
		Person person = new Person();
		System.out.println(person.addPerson("22##abcdEF", "John", "Doe", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990"));
		System.out.println(person.addID(new Passport("AB345678", "22##abcdEF", "John Doe", "15-11-1990", "Australia", "01-01-2020", "01-01-2030", "State")));
	}
}
