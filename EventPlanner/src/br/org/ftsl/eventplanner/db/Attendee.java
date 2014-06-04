package br.org.ftsl.eventplanner.db;

public class Attendee {
	 
    private int _id;
	private String name;
    private String mail;
 
    public Attendee(){}
 
    public Attendee(String name, String mail) {
        super();
        this.name = name;
        this.mail = mail;
    }
 
    public int getId() {
		return _id;
	}

	public void setId(int id) {
		this._id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

 
    @Override
    public String toString() {
        return "Attendee [id=" + _id + ", Name=" + name+ ", eMail=" + mail
                + "]";
    }
}
