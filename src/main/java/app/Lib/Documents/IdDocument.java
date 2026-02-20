package app.Lib.Documents;

public abstract class IdDocument {
	public IdDocument(
			String id,
			String name,
			String dateOfBirth,
			String dateOfIssue,
			String dateOfExpiry
	) {
		this.id = id;
		this.name = name;
		this.dateOfBirth = dateOfBirth;
		this.dateOfIssue = dateOfIssue;
		this.dateOfExpiry = dateOfExpiry;
	}

	String id;
	String name;
	String dateOfBirth;
	String dateOfIssue;
	String dateOfExpiry;

	public abstract boolean isValid();

	public boolean save() {
		return true;
	}
}
