package app.Lib.Documents;

import app.Lib.DateHelper;

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
    // TODO: Implement save to text file or csv file
    return true;
  }
}
