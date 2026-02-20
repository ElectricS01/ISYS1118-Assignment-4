package app;

import app.Lib.DateHelper;
import app.Lib.AddPersonHelper;
import app.Lib.Documents.IdDocument;

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

	public boolean updatePersonDetails(String existingPersonID, String newPersonID, String newFirstName, String newLastName, String newAddress, String newBirthDate) {
		 //Check if the conditions from addPerson are met
		boolean condition1 = AddPersonHelper.checkAddPersonCondition1(newPersonID);
		boolean condition2 = AddPersonHelper.checkAddPersonCondition2(newAddress);
		boolean condition3 = AddPersonHelper.checkAddPersonCondition3(newBirthDate);

		if (!condition1 || !condition2 || !condition3) {
			return false;
		}

		// Extra Condition: first and last name
		if (firstName == null || lastName == null || firstName.isEmpty() || lastName.isEmpty()) {
			return false;
		}

		//The file must exist to update
		Path path = Path.of("./people.csv");
		if (!Files.exists(path)) {
			return false;
		}

		// Check conditions 1, 2, and 3
		boolean isOver18 = DateHelper.isOver18(newBirthDate);
		boolean changeBirthDay = !birthDate.equals(newBirthDate);
		boolean canChangeID = !((personID.charAt(0) - '0') % 2 == 0);

		// Condition 1
		if (!isOver18 && !address.equals(newAddress)) {
			return false;
		}
		// Condition 2
		if (changeBirthDay && (!personID.equals(newPersonID) || !firstName.equals(newFirstName) || !lastName.equals(newLastName) || !address.equals(newAddress))) {
			return false;
		}
		// Condition 3
		if (!canChangeID && !personID.equals(newPersonID)) {
			return false;
		}

		try {
			java.util.List<String> lines = Files.readAllLines(path);
			if (lines.isEmpty()) return false;

			int targetIndex = -1;
			for (int i = 1; i < lines.size(); i++) {
				String line = lines.get(i);
				if (line == null || line.trim().isEmpty()) continue;

				String[] cols = line.split(",", -1);
				if(cols.length < 9) continue;

				if (cols[0].equals(existingPersonID)) {
					targetIndex = 1;
					break;
				}
			}

			if (targetIndex == -1) return false;

			String[] oldCols = lines.get(targetIndex).split(",", -1);

			String oldPersonID = oldCols[0];
			String oldFirstName = oldCols[1];
			String oldLastName = oldCols[2];
			String oldAddress = oldCols[3];
			String oldBirthDate = oldCols[4];

			// If first digit of existing ID is even, ID cannot change
			char firstChar = oldPersonID.charAt(0);
			if (Character.isDigit(firstChar)) {
				int digit = Character.getNumericValue(firstChar);
				if (digit % 2 == 0 && !oldPersonID.equals(newPersonID)) {
					return false;
				}
			}

			// If birthday changes, nothing else can change
			if (!oldBirthDate.equals(newBirthDate)) {
				if (!oldPersonID.equals(newPersonID) ||
						!oldFirstName.equals(newFirstName) ||
						!oldLastName.equals(newLastName) ||
						!oldAddress.equals(newAddress)) {
					return false;
				}
			}

			// If under 18, address cannot change
			java.time.LocalDate birth =
					java.time.LocalDate.parse(oldBirthDate,
							java.time.format.DateTimeFormatter.ofPattern("dd-MM-uuuu"));

			int age = java.time.Period.between(birth,
					java.time.LocalDate.now()).getYears();

			if (age < 18 && !oldAddress.equals(newAddress)) {
				return false;
			}


			// Preserve existing ID docs columns (passport, driversLicense, medicareCard, studentCard)

			String passport = oldCols[5];
			String driversLicense = oldCols[6];
			String medicareCard = oldCols[7];
			String studentCard = oldCols[8];

			// Update the row
			String updatedLine = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s",
					newPersonID,
					newFirstName,
					newLastName,
					newAddress,
					newBirthDate,
					passport,
					driversLicense,
					medicareCard,
					studentCard
			);

			lines.set(targetIndex, updatedLine);

			// overwrite existing details
			Files.write(path, lines);

			// Update person details
			this.personID = newPersonID;
			this.firstName = newFirstName;
			this.lastName = newLastName;
			this.address = newAddress;
			this.birthDate = newBirthDate;

			return true;
		} catch (IOException e) {
			System.out.println("An error occurred: " + e);
			return false;
		}
	}

	public boolean addID(IdDocument idDocument) {
		if (idDocument.childrenOnly() && DateHelper.isOver18(birthDate)) {
			return false;
		}

		return idDocument.isValid() && idDocument.save();
	}
}
