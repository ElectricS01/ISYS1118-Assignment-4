package app;

import java.nio.file.Files;
import java.nio.file.Paths;

public class TestHelper {
  public static void deleteCsvFiles() {
    try {
      Files.deleteIfExists(Paths.get("people.csv"));
      Files.deleteIfExists(Paths.get("passports.csv"));
      Files.deleteIfExists(Paths.get("drivers_licences.csv"));
      Files.deleteIfExists(Paths.get("medicare_cards.csv"));
      Files.deleteIfExists(Paths.get("student_cards.csv"));
    } catch (java.io.IOException e) {
      e.printStackTrace();
    }
  }

  public static void createTestPerson() {
    Person person = new Person();
    person.addPerson("22##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
  }
}
