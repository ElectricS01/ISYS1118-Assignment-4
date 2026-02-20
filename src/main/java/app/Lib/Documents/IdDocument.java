package app.Lib.Documents;

import app.Lib.DateHelper;

import java.time.LocalDate;

public abstract class IdDocument {

  protected String id;
  protected String name;
  protected String dateOfBirth;
  protected String country;
  protected String dateOfIssue;
  protected String dateOfExpiry;

  public IdDocument(
      String id,
      String name,
      String dateOfBirth,
      String country,
      String dateOfIssue,
      String dateOfExpiry) {
    this.id = id;
    this.name = name;
    this.dateOfBirth = dateOfBirth;
    this.country = country;
    this.dateOfIssue = dateOfIssue;
    this.dateOfExpiry = dateOfExpiry;
  }

  protected boolean areDatesValid() {
    LocalDate today = LocalDate.now();

    LocalDate birthDate = DateHelper.parseDate(dateOfBirth);
    LocalDate validFrom = DateHelper.parseDate(dateOfIssue);
    LocalDate validTo = DateHelper.parseDate(dateOfExpiry);

    return birthDate != null
        && validFrom != null
        && validTo != null
        && !birthDate.isAfter(today)
        && !validFrom.isAfter(today)
        && !birthDate.isBefore(validTo)
        && !validTo.isBefore(validFrom);
  }

  public abstract boolean isValid();

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
    return "id,name,dateOfBirth,country,dateOfIssue,dateOfExpiry" + extraCsvHeader();
  }

  public final String toCsvRow() {
    return id
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
