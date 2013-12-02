public class Account {
	private String id;
	private String mail;
	private String salutation;

	public Account(String id, String salutation, String mail) {
		this.id = id;
		this.mail = mail;
		this.salutation = salutation;
	}

	public String getSalutation() {
		return salutation;
	}

	public String getId() {
		return id;
	}

	public String getMail() {
		return mail;
	}
}
