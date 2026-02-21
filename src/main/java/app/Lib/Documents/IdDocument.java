package app.Lib.Documents;

import app.Lib.DateHelper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

public abstract class IdDocument {

  protected String id;
  protected String personID;
  protected String name;
  protected String dateOfBirth;
  protected String country;
  protected String dateOfIssue;
  protected String dateOfExpiry;

  public IdDocument(
      String id,
      String personID,
      String name,
      String dateOfBirth,
      String country,
      String dateOfIssue,
      String dateOfExpiry) {
    this.id = id;
    this.personID = personID;
    this.name = name;
    this.dateOfBirth = dateOfBirth;
    this.country = country;
    this.dateOfIssue = dateOfIssue;
    this.dateOfExpiry = dateOfExpiry;
  }

  // Shared validation logic for date fields. Checks if any date is invalid or if the dates are in an illogical order.
  protected boolean areDatesInvalid() {
    LocalDate today = LocalDate.now();

    // Parses the date strings into LocalDate objects. If any date is invalid, it will return null.
    LocalDate birthDate = DateHelper.parseDate(dateOfBirth);
    LocalDate validFrom = DateHelper.parseDate(dateOfIssue);
    LocalDate validTo = DateHelper.parseDate(dateOfExpiry);

    return birthDate == null
        || validFrom == null
        || validTo == null
        || birthDate.isAfter(today)
        || validFrom.isAfter(today)
        || validTo.isBefore(today)
        || birthDate.isAfter(validFrom)
        || validFrom.isAfter(validTo);
  }

  // Each document type will implement its own validation logic based on its specific requirements
  public abstract boolean isValid();

  // Each document type will implement this method to specify its own file name for storage
  protected abstract String getFileName();

  // Returns the column index in people.csv that this document type corresponds to
  protected abstract int getPersonCsvColumnIndex();

  public String getPersonID() {
    return personID;
  }

  // By default, documents are not children-only, but subclasses can override this if needed
  public boolean childrenOnly() {
    return false;
  }

  // These methods can be overridden by subclasses to add extra fields to the CSV output
  protected String extraCsvHeader() {
    return "";
  }

  // By default, no extra fields are added, but subclasses can override this to include additional data
  protected String extraCsvFields() {
    return "";
  }

  public final String getCsvHeader() {
    return "id,personID,name,dateOfBirth,country,dateOfIssue,dateOfExpiry" + extraCsvHeader();
  }

  public final String toCsvRow() {
    return id
        + ","
        + personID
        + ","
        + name
        + ","
        + dateOfBirth
        + ","
        + country
        + ","
        + dateOfIssue
        + ","
        + dateOfExpiry
        + extraCsvFields();
  }

  // Saves the document to a CSV file and updates people.csv. Returns true if successful, false otherwise.
  public boolean save() {
    try {
      // Gets the file name based on the document type and creates it if it doesn't exist
      File file = new File(getFileName());

      // Updates the corresponding person in people.csv with the document ID. If the person cannot be found or updated, it returns false to indicate failure.
      if (!updatePeopleCsv()) {
        return false;
      }

      boolean newFile = file.createNewFile();

      try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {

        // If the file is new, write the header first
        if (newFile) {
          writer.write(getCsvHeader());
          writer.newLine();
        }

        // Write the document data as a new row in the CSV file
        writer.write(toCsvRow());
        writer.newLine();
      }

      return true;
    } catch (IOException e) {
      return false;
    }
  }

  // Updates the corresponding column in people.csv for this person with the document ID
  private boolean updatePeopleCsv() throws IOException {
    Path peoplePath = Path.of("people.csv");
    if (!Files.exists(peoplePath)) return false;

    List<String> lines = Files.readAllLines(peoplePath);

    boolean personUpdated = false;

    for (int i = 1; i < lines.size(); i++) {
      String line = lines.get(i);
      if (line == null || line.trim().isEmpty()) continue;

      String[] cols = line.split(",", -1);
      if (cols.length < 9) continue;

      if (cols[0].equals(personID)) {
        cols[getPersonCsvColumnIndex()] = id;
        lines.set(i, String.join(",", cols));
        personUpdated = true;
        break;
      }
    }

    Files.write(peoplePath, lines);
    return personUpdated;
  }
}
