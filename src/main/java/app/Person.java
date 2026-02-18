package app;

import app.Lib.BirthDateHelper;
import app.Lib.AddPersonHelper;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class Person {
	private String personID;
	private String firstName;
	private String lastName;
	private String address;
	private String birthDate;

	public boolean addPerson(String personID, String firstName, String lastName, String address, String birthDate) {
		// Check arguments meet conditions
		boolean condition1 = AddPersonHelper.checkAddPersonCondition1(personID);
		boolean condition2 = AddPersonHelper.checkAddPersonCondition2(address);
		boolean condition3 = AddPersonHelper.checkAddPersonCondition3(birthDate);
		if (!condition1 || !condition2 || !condition3) {
			return false;
		}

		// Extra Condition: first and last name
		if (firstName == null || lastName == null || firstName.isEmpty() || lastName.isEmpty()) {
			return false;
		}

		// Update Person
		this.firstName = firstName;
		this.lastName = lastName;
		this.personID = personID;
		this.address = address;
		this.birthDate = birthDate;

		// Insert into CSV file
		boolean fileExists = Files.exists(Path.of("./people.csv"));
        try {
			FileWriter writer = new FileWriter("./people.csv", true);
			if (!fileExists) {
				writer.write("personID,firstName,lastName,address,birthDate,passport,driversLicense,medicareCard,studentCard");
			}
			writer.append(String.format("\n%s,%s,%s,%s,%s,NULL,NULL,NULL,NULL", personID, firstName, lastName, address, birthDate));
			writer.close();
		} catch (IOException e) {
			System.out.println("An error occurred: " + e);
		}

		return true;
	}

	public static void updatePersonalDetails() {

	}

	public static void addID() {

	}
}
