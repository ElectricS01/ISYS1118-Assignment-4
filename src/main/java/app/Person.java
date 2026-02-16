package app;


import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;


public class Person {
	private String personID;
	private String firstName;
	private String lastName;
	private String address;
	private String birthDate;

	public Person() {
	}

	public Person(String personID, String firstName, String lastName, String address, String birthDate) {
		boolean valid = addPerson(personID, firstName, lastName, address, birthDate);
		if (!valid) {
			throw new IllegalArgumentException("Invalid Person details");
		}
	}

	public boolean addPerson(String personID, String firstName, String lastName, String address, String birthDate) {
		// Check arguments meet conditions
		boolean condition1 = checkAddPersonCondition1(personID);
		boolean condition2 = checkAddPersonCondition2(address);
		boolean condition3 = checkAddPersonCondition3(birthDate);
		if (!condition1 || !condition2 || !condition3) {
			return false;
		}

		// Extra Conditions
		String[] splitBirthDate = birthDate.split("-");
		int year = Integer.parseInt(splitBirthDate[2]);
		int month = Integer.parseInt(splitBirthDate[1]);
		int day = Integer.parseInt(splitBirthDate[0]);
		LocalDate birthDateObj = LocalDate.of(year, month, day);

		Period age = Period.between(birthDateObj, LocalDate.now());
		if (age.getYears() < 0) {
			return false;
		}
		if (firstName == null || lastName == null || firstName.isEmpty() || lastName.isEmpty()) {
			return false;
		}

		// Update Person
		this.firstName = firstName;
		this.lastName = lastName;
		this.personID = personID;
		this.address = address;
		this.birthDate = birthDate;

		// Insert into TXT file
		try {
			FileWriter writer = new FileWriter(String.format("%s.txt", personID));
			writer.write(String.format("Person ID: %s\n", personID));
			writer.write(String.format("First Name: %s\n", firstName));
			writer.write(String.format("Last Name: %s\n", lastName));
			writer.write(String.format("Address: %s\n", address));
			writer.write(String.format("Birth Date: %s\n", birthDate));
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

	// personID should be exactly 10 characters long
	// The first two characters should be numbers between 2 and 9
	// There should be at least two special characters between characters 3 and 8
	// The last two characters should be uppercase letters (A-Z)
	private boolean checkAddPersonCondition1(String personID) {
		// Ensures personID is not null
		if (personID == null) {
			return false;
		}

		// Checks that personID is exactly 10 characters long
		if (personID.length() != 10) {
			return false;
		}

		// Checks that personID's first 2 characters are between 2 and 9
		if (Character.getNumericValue(personID.charAt(0)) < 2 ||
				Character.getNumericValue(personID.charAt(0)) > 9 ||
				Character.getNumericValue(personID.charAt(1)) < 2 ||
				Character.getNumericValue(personID.charAt(1)) > 9) {
			return false;
		}

		// Checks that there are at least 2 special characters between 3 and 8
		int specialCharacters = 0;
		for (int i = 2; i <= 7; i++) {
			if (!Character.isLetterOrDigit(personID.charAt(i))) {
				specialCharacters++;
			}
		}
		if (specialCharacters < 2) {
			return false;
		}

		// Checks that the last 2 characters are upper case
		if (!Character.isUpperCase(personID.charAt(8)) || !Character.isUpperCase(personID.charAt(9))) {
			return false;
		}

		return true;
	}

	// The address of the Person should follow the following format: Street Number|Street|City|State|Country
	// The State should be only Victoria
	private boolean checkAddPersonCondition2(String address) {
		// Ensures address is not null
		if (address == null) {
			return false;
		}

		// Checks that address format is correct
		String[] splitAddress = address.split("\\|");
		if (splitAddress.length != 5) {
			return false;
		}

		// Checks that State is Victoria
		if (!splitAddress[3].equals("Victoria")) {
			return false;
		}

		return true;
	}

	// The format of the birthdate of the person should follow the following format: DD-MM-YYYY
	private boolean checkAddPersonCondition3(String birthDate) {
		// Ensures birthDate is not null
		if (birthDate == null) {
			return false;
		}

		// Checks birthDate is in DD-MM-YYY format
		String[] splitBirthDate = birthDate.split("-");
		if (splitBirthDate.length != 3) {
			return false;
		}
		if (splitBirthDate[0].length() != 2 || splitBirthDate[1].length() != 2 || splitBirthDate[2].length() != 4) {
			return false;
		}
		// Checks birthDate uses digits
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < splitBirthDate[i].length(); j++) {
				if (!Character.isDigit(splitBirthDate[i].charAt(j))) {
					return false;
				}
			}
		}

		// Checks valid date
		int day = Integer.parseInt(splitBirthDate[0]);
		int month = Integer.parseInt(splitBirthDate[1]);
		int year = Integer.parseInt(splitBirthDate[2]);
		if (month < 1 || month > 12) {
			return false;
		}
		if (day < 1) {
			return false;
		}
		if (month == 2) {
			if (year % 400 == 0 || (year % 100 != 0 && year % 4 == 0)) {
				if (day > 29) {
					return false;
				}
			} else {
				if (day > 28) {
					return false;
				}
			}
		} else if ((month == 1 || month == 3 || month == 5 || month == 7 | month == 8 || month == 10 || month == 12) &&
				day > 31) {
			return false;
		} else if (day > 30){
			return false;
		}

		return true;
	}
}
