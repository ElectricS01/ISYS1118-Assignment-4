package app.Lib.Documents;

public class DriversLicence extends IdDocument {

  private final String version;
  private final String vehicleType;

  public DriversLicence(
      String id,
      String personID,
      String name,
      String dateOfBirth,
      String country,
      String dateOfIssue,
      String dateOfExpiry,
      String version,
      String vehicleType) {
    super(id, personID, name, dateOfBirth, country, dateOfIssue, dateOfExpiry);
    this.version = version;
    this.vehicleType = vehicleType;
  }

  @Override
  public boolean isValid() {
    if (areDatesInvalid()) return false;

    if (id == null || personID == null || name == null || dateOfBirth == null || country == null || vehicleType == null) return false;
    if (id.isEmpty() || personID.isEmpty() || name.isEmpty() || dateOfBirth.isEmpty() || country.isEmpty() || vehicleType.isEmpty()) return false;

    return id.matches("^[A-Z]{2}\\d{8}$");
  }

  @Override
  protected String getFileName() {
    return "drivers_licences.csv";1
  }

  @Override
  protected String extraCsvHeader() {
    return ",authority,vehicleType";
  }

  @Override
  protected String extraCsvFields() {
    return "," + version + "," + vehicleType;
  }
}
