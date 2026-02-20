package app.Lib.Documents;

public class DriversLicence extends IdDocument {

  private final String version;
  private final String vehicleType;

  public DriversLicence(
      String id,
      String name,
      String dateOfBirth,
      String country,
      String dateOfIssue,
      String dateOfExpiry,
      String version,
      String vehicleType) {
    super(id, name, dateOfBirth, country, dateOfIssue, dateOfExpiry);
    this.version = version;
    this.vehicleType = vehicleType;
  }

  @Override
  public boolean isValid() {
    if (!areDatesValid()) return false;

    return id.matches("^[A-Za-z]{2}\\d{8}$");
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
