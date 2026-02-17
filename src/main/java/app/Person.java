package app;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.Period;


public class Person {
	private String personID;
	private String firstName;
	private String lastName;
	private String address;
	private String birthDate;

	public boolean addPerson(String personID, String firstName, String lastName, String address, String birthDate) {
		// Check arguments meet conditions
		boolean condition1 = checkAddPersonCondition1(personID);
		boolean condition2 = checkAddPersonCondition2(address);
		boolean condition3 = checkAddPersonCondition3(birthDate);
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

	// Checks for valid personID
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

	// Checks for valid address
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

	// Checks for valid birthDate
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
		} else if ((month == 4 || month == 6 || month == 9 || month == 11) && day > 30) {
			return false;
		}

		// Check birthDate is not in the future
		LocalDate birthDateObj = LocalDate.of(year, month, day);
		Period age = Period.between(birthDateObj, LocalDate.now());
		if (age.getDays() < 0) {
			return false;
		}

		return true;
	}
}
