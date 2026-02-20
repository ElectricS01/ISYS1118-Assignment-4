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
    try {
      return LocalDate.parse(birthDate, formatter);
    } catch (DateTimeParseException e) {
      return null;
    }
  }

  public static boolean isDateValid(String birthDate) {
    try {
      LocalDate.parse(birthDate, formatter);
      return true;
    } catch (DateTimeParseException e) {
      return false;
    }
  }

  public static boolean isOver18(String birthDate) {
    try {
      LocalDate dob = LocalDate.parse(birthDate, formatter);
      LocalDate today = LocalDate.now();

      int age = Period.between(dob, today).getYears();
      return age > 18;
    } catch (DateTimeParseException e) {
      return false;
    }
  }
}
