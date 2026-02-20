package app.Lib.Documents;

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
			String dateOfExpiry
	) {
		this.id = id;
		this.name = name;
		this.dateOfBirth = dateOfBirth;
		this.country = country;
		this.dateOfIssue = dateOfIssue;
		this.dateOfExpiry = dateOfExpiry;
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
		return "id,name,dateOfBirth,country,dateOfIssue,dateOfExpiry"
				+ extraCsvHeader();
	}

	public final String toCsvRow() {
		return id + "," + name + "," + dateOfBirth + "," +
				country + "," + dateOfIssue + "," + dateOfExpiry
				+ extraCsvFields();
	}

	public boolean save() {
		//TODO: Implement save to text file or csv file
		return true;
	}
}
