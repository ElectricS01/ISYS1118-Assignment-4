package app.Lib.Documents;

public class Passport extends IdDocument {

  private final String authority;

  public Passport(
      String id,
      String personID,
      String name,
      String dateOfBirth,
      String country,
      String dateOfIssue,
      String dateOfExpiry,
      String authority) {
    super(id, personID, name, dateOfBirth, country, dateOfIssue, dateOfExpiry);
    this.authority = authority;
  }

  @Override
  public boolean isValid() {
    if (areDatesInvalid()) return false;

    if (id == null || personID == null || name == null || dateOfBirth == null || country == null || authority == null) return false;
    if (id.isEmpty() || personID.isEmpty() || name.isEmpty() || dateOfBirth.isEmpty() || country.isEmpty() || authority.isEmpty()) return false;

    return id.matches("^[A-Z]{2}\\d{6}$");
  }

  @Override
  protected String getFileName() {
    return "passports.csv";
  }

  @Override
  protected int getPersonCsvColumnIndex() {
    return 5;
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
