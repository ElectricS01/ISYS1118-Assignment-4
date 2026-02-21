package app;

import java.nio.file.Files;
import java.nio.file.Paths;

public class TestHelper {
    public static void clearPeopleFile() {
          try {
              Files.deleteIfExists(Paths.get("people.csv"));
          } catch (java.io.IOException e) {
              e.printStackTrace();
          }
    }
}
