package app.Lib;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class BirthDateHelper {
	public static boolean isOver18(String birthDate) {
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			LocalDate dob = LocalDate.parse(birthDate, formatter);
			LocalDate today = LocalDate.now();

			int age = Period.between(dob, today).getYears();
			return age > 18;
		} catch (DateTimeParseException e) {
			return false;
		}
	}
}
