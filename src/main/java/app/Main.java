package app;

import app.Lib.Documents.Passport;

public class Main {
	public static void main(String[] args) {
		Person person = new Person();
		System.out.println(person.addPerson("12345678", "John", "Doe", "123 Main St", "01/01/1990"));
		System.out.println(person.addID(new Passport("AB345678", "John Doe", "01/01/1990", "USA", "01/01/2020", "01/01/2030", "State")));
	}
}
