package app.Lib;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class DateHelper {
  private static final DateTimeFormatter formatter =
      DateTimeFormatter.ofPattern("dd-MM-uuuu").withResolverStyle(ResolverStyle.STRICT);

  public static LocalDate parseDate(String birthDate) {
    if (birthDate == null) {
      return null;
    }

    try {
      return LocalDate.parse(birthDate, formatter);
    } catch (DateTimeParseException e) {
      return null;
    }
  }

  public static boolean isValidDate(String birthDate) {
    if (birthDate == null) {
      return false;
    }

    try {
      LocalDate.parse(birthDate, formatter);
      return true;
    } catch (DateTimeParseException e) {
      return false;
    }
  }

  public static boolean isUnder18(String birthDate, LocalDate today) {
    try {
      LocalDate dob = parseDate(birthDate);
      if (dob == null) {
        return false;
      }

      int age = Period.between(dob, today).getYears();
      return age < 18;
    } catch (DateTimeParseException e) {
      return false;
    }
  }
}
