package br.org.ftsl.eventplanner.db;

public class Lecture {

	private int _id;
	private String title;
    private String date;
    private String room;
 
    public Lecture(){}
 
    public Lecture(String title, String date, String room) {
        super();
        this.title = title;
        this.date = date;
        this.room = room;
    }
    
    public Lecture(int id, String title, String date, String room) {
        super();
        this._id = id;
        this.title = title;
        this.date = date;
        this.room = room;
    }
 
    public int getId() {
		return _id;
	}

	public void setId(int id) {
		this._id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}


    public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	
    @Override
    public String toString() {
        return "Lecture [id=" + _id + ", Titel=" + title+ ", Room=" + room + " Date="+date
                + "]";
    }
}
