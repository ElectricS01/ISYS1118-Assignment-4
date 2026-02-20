package app.Lib.Documents;

public class Passport extends IdDocument {

  private final String authority;

  public Passport(
      String id,
      String personId,
      String name,
      String dateOfBirth,
      String country,
      String dateOfIssue,
      String dateOfExpiry,
      String authority) {
    super(id, personId, name, dateOfBirth, country, dateOfIssue, dateOfExpiry);
    this.authority = authority;
  }

  @Override
  public boolean isValid() {
    if (areDatesInvalid()) return false;

    return id.matches("^[A-Za-z]{2}\\d{6}$");
  }

  @Override
  protected String getFileName() {
    return "passports.txt";
  }

  @Override
  protected String extraCsvHeader() {
    return ",authority";
  }

  @Override
  protected String extraCsvFields() {
    return "," + authority;
  }
}
