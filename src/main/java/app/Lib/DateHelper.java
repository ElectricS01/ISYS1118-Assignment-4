package app.Lib;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class DateHelper {

  // Avoid declaring the formatter in multiple methods by making it a static final variable
  private static final DateTimeFormatter formatter =
      DateTimeFormatter.ofPattern("dd-MM-uuuu").withResolverStyle(ResolverStyle.STRICT);

  // Parses a date string in the format "dd-MM-yyyy" to a LocalDate object
  public static LocalDate parseDate(String birthDate) {
    // Return null if the input is null to avoid exceptions
    if (birthDate == null) {
      return null;
    }

    try {
      return LocalDate.parse(birthDate, formatter);
    } catch (DateTimeParseException e) {
      // Return null if the date string is invalid instead of throwing an exception
      return null;
    }
  }

  // Validates if a date string is in the correct format and represents a valid calendar date
  public static boolean isValidDate(String birthDate) {
    // A null date string is invalid
    if (birthDate == null) {
      return false;
    }

    try {
      LocalDate.parse(birthDate, formatter);
      return true;
    } catch (DateTimeParseException e) {
      // If parsing fails, the date is invalid
      return false;
    }
  }

  // Checks if a person is under 18 years old based on their birthdate and the current date
  public static boolean isUnder18(String birthDate, LocalDate today) {
    try {
      // Parse the birthdate and return false if it's invalid
      LocalDate dob = parseDate(birthDate);
      if (dob == null) {
        return false;
      }

      // Calculate the age by finding the period between the date of birth and today's date
      int age = Period.between(dob, today).getYears();
      return age < 18;
    } catch (DateTimeParseException e) {
      return false;
    }
  }
}
