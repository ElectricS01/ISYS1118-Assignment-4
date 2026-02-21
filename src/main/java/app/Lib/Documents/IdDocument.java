package app.Lib.Documents;

import app.Lib.DateHelper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

public abstract class IdDocument {

  protected String id;
  protected String personId;
  protected String name;
  protected String dateOfBirth;
  protected String country;
  protected String dateOfIssue;
  protected String dateOfExpiry;

  public IdDocument(
      String id,
      String personId,
      String name,
      String dateOfBirth,
      String country,
      String dateOfIssue,
      String dateOfExpiry) {
    this.id = id;
    this.personId = personId;
    this.name = name;
    this.dateOfBirth = dateOfBirth;
    this.country = country;
    this.dateOfIssue = dateOfIssue;
    this.dateOfExpiry = dateOfExpiry;
  }

  protected boolean areDatesInvalid() {
    LocalDate today = LocalDate.now();

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

  public abstract boolean isValid();

  protected abstract String getFileName();

  public String getPersonID() { return personId; }

  public boolean childrenOnly() {
    return false;
  }

  protected String extraCsvHeader() {
    return "";
  }

  protected String extraCsvFields() {
    return "";
  }

  public final String getCsvHeader() {
    return "id,personId,name,dateOfBirth,country,dateOfIssue,dateOfExpiry" + extraCsvHeader();
  }

  public final String toCsvRow() {
    return id
        + ","
        + personId
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

  public boolean save() {
    try {
      File file = new File(getFileName());
      boolean newFile = file.createNewFile();

      try (BufferedWriter writer =
               new BufferedWriter(new FileWriter(file, true))) {

        if (newFile) {
          writer.write(getCsvHeader());
          writer.newLine();
        }

        writer.write(toCsvRow());
        writer.newLine();
      }

      return true;
    } catch (IOException e) {
      return false;
    }
  }
}
