package app.Lib.Documents;

import app.Lib.DateHelper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

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

  // Saves the document to a CSV file. Returns true if successful, false otherwise.
  public boolean save() {
    try {
      // Gets the file name based on the document type and creates it if it doesn't exist
      File file = new File(getFileName());
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
}
