package app.Lib;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;

public class AddPersonHelper {
    // Checks for valid personID
    public static boolean checkAddPersonCondition1(String personID) {
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
    public static boolean checkAddPersonCondition2(String address) {
        // Ensures address is not null
        if (address == null) {
            return false;
        }

        // Checks that address format is correct
        String[] splitAddress = address.split("\\|");
        if (splitAddress.length != 5) {
            return false;
        }

        // Checks that Street Number is made of digits
        for (int i = 0; i < splitAddress[0].length(); i++) {
            if (!Character.isDigit(splitAddress[0].charAt(i))) {
                return false;
            }
        }

        // Checks that State is Victoria
        if (!splitAddress[3].equals("Victoria") || !splitAddress[4].equals("Australia")) {
            return false;
        }

        return true;
    }

    // Checks for valid birthDate
    public static boolean checkAddPersonCondition3(String birthDate) {
        // Ensures birthDate is not null
        if (birthDate == null) {
            return false;
        }

        try {
            // Checks birthDate is in DD-MM-YYY format
            LocalDate birthDateObj = LocalDate.parse(birthDate, DateTimeFormatter
                    .ofPattern("dd-MM-uuuu")
                    .withResolverStyle(ResolverStyle.STRICT)
            );

            // Check birthDate is not in the future
            Period age = Period.between(birthDateObj, LocalDate.now());
            if (age.getDays() < 0) {
                return false;
            }
        } catch (DateTimeParseException e) {
            return false;
        }

        return true;
    }

    public static boolean checkPersonExists(String personID) {
        try {
            List<String> lines = Files.readAllLines(Path.of("people.csv"));
            for (String line : lines) {
                if (line.matches(personID + ",.*")) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
        }

        return false;
    }
}
